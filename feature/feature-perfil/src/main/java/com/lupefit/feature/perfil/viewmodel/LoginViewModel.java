package com.lupefit.feature.perfil.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<String> _email = new MutableLiveData<>("");
    public final LiveData<String> email = _email;

    private final MutableLiveData<String> _password = new MutableLiveData<>("");
    public final LiveData<String> password = _password;

    private final MutableLiveData<Boolean> _recordar = new MutableLiveData<>(true);
    public final LiveData<Boolean> recordar = _recordar;

    private final MutableLiveData<Boolean> _cargando = new MutableLiveData<>(false);
    public final LiveData<Boolean> cargando = _cargando;

    private final MutableLiveData<Boolean> _autenticado = new MutableLiveData<>(false);
    public final LiveData<Boolean> autenticado = _autenticado;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    private final LocalAccount account;

    public LoginViewModel(Context context) {
        account = new LocalAccount(context);
        _email.setValue(account.rememberedEmail());
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(LoginViewModel.class)) {
                return (T) new LoginViewModel(context);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void login() {
        String emailVal = _email.getValue();
        String passVal = _password.getValue();

        if (emailVal == null || emailVal.trim().isEmpty()) {
            _error.setValue("El email es obligatorio");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailVal).matches()) {
            _error.setValue("Email inválido");
            return;
        }

        if (passVal == null || passVal.length() < 6) {
            _error.setValue("La contraseña debe tener al menos 6 caracteres");
            return;
        }

        _cargando.setValue(true);
        _error.setValue(null);

        boolean recordarEmail = Boolean.TRUE.equals(_recordar.getValue());
        LocalAccount.EXECUTOR.execute(() -> {
            try {
                if (account.login(emailVal, passVal, recordarEmail)) {
                    _password.postValue("");
                    _autenticado.postValue(true);
                } else {
                    _error.postValue("Email o contraseña incorrectos");
                }
            } catch (java.security.GeneralSecurityException | IllegalArgumentException | IllegalStateException failure) {
                _error.postValue("No se pudo completar la operación de cuenta local");
            } finally {
                _cargando.postValue(false);
            }
        });
    }

    public void setEmail(String email) {
        _email.setValue(email);
    }

    public void setPassword(String password) {
        _password.setValue(password);
    }
    public void setRecordar(boolean value) {
        _recordar.setValue(value);
    }
}
