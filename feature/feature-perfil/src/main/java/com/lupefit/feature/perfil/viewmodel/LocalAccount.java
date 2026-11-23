package com.lupefit.feature.perfil.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

final class LocalAccount {

    static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private static final int ITERATIONS = 600_000;

    private final SharedPreferences account;
    private final SharedPreferences profile;

    LocalAccount(Context context) {
        Context application = context.getApplicationContext();
        account = application.getSharedPreferences("lupefit_local_account", Context.MODE_PRIVATE);
        profile = application.getSharedPreferences("lupefit_local_profile", Context.MODE_PRIVATE);
    }

    boolean register(String nombre, String apellido, String email, String password)
            throws GeneralSecurityException {
        if (account.contains("email")) {
            return false;
        }
        SharedPreferences.Editor editor = account.edit()
                .putString("email", normalizeEmail(email));
        if (!savePassword(editor, password)) {
            throw new IllegalStateException("No se pudo guardar la cuenta local");
        }
        profile.edit()
                .putString("nombre", nombre.trim())
                .putString("apellido", apellido.trim())
                .putString("email", email.trim())
                .apply();
        return true;
    }

    boolean login(String email, String password, boolean rememberEmail) throws GeneralSecurityException {
        if (!normalizeEmail(email).equals(account.getString("email", ""))
                || !checkPassword(password)) {
            return false;
        }
        account.edit().putString("remembered_email", rememberEmail ? normalizeEmail(email) : "").apply();
        return true;
    }

    String rememberedEmail() {
        return account.getString("remembered_email", "");
    }

    boolean changePassword(String currentPassword, String newPassword) throws GeneralSecurityException {
        if (!checkPassword(currentPassword)) {
            return false;
        }
        if (!savePassword(account.edit(), newPassword)) {
            throw new IllegalStateException("No se pudo guardar la contraseña");
        }
        return true;
    }

    private boolean checkPassword(String password) throws GeneralSecurityException {
        String salt = account.getString("salt", null);
        String hash = account.getString("hash", null);
        if (salt == null || hash == null) {
            return false;
        }
        return MessageDigest.isEqual(Base64.getDecoder().decode(hash),
                derive(password, Base64.getDecoder().decode(salt)));
    }

    private static boolean savePassword(SharedPreferences.Editor editor, String password)
            throws GeneralSecurityException {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        Base64.Encoder encoder = Base64.getEncoder();
        return editor.putString("salt", encoder.encodeToString(salt))
                .putString("hash", encoder.encodeToString(derive(password, salt)))
                .commit();
    }

    private static byte[] derive(String password, byte[] salt) throws GeneralSecurityException {
        char[] chars = password.toCharArray();
        PBEKeySpec parameters = new PBEKeySpec(chars, salt, ITERATIONS, 256);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(parameters)
                    .getEncoded();
        } finally {
            Arrays.fill(chars, '\0');
            parameters.clearPassword();
        }
    }

    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
