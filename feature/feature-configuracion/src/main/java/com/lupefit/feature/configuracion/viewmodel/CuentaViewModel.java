package com.lupefit.feature.configuracion.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CuentaViewModel extends ViewModel {

    private final MutableLiveData<String> _email = new MutableLiveData<>("");
    public final LiveData<String> email = _email;

    private final MutableLiveData<String> _nombre = new MutableLiveData<>("");
    public final LiveData<String> nombre = _nombre;

    private final MutableLiveData<Boolean> _saved = new MutableLiveData<>(false);
    public final LiveData<Boolean> saved = _saved;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    private final SharedPreferences preferences;

    public CuentaViewModel(Context context) {
        preferences = context.getApplicationContext()
                .getSharedPreferences("lupefit_local_profile", Context.MODE_PRIVATE);
        _nombre.setValue(preferences.getString("nombre", ""));
        _email.setValue(preferences.getString("email", ""));
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(CuentaViewModel.class)) {
                return (T) new CuentaViewModel(context);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void saveCambios() {
        String emailVal = _email.getValue();
        String nombreVal = _nombre.getValue();

        if (emailVal == null || emailVal.trim().isEmpty()) {
            _error.setValue("El email es obligatorio");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailVal).matches()) {
            _error.setValue("Email inválido");
            return;
        }

        if (nombreVal == null || nombreVal.trim().isEmpty()) {
            _error.setValue("El nombre es obligatorio");
            return;
        }

        _saved.setValue(false);
        _error.setValue(null);

        preferences.edit()
                .putString("nombre", _nombre.getValue())
                .putString("email", _email.getValue())
                .apply();
        _saved.setValue(true);
    }

    public void setEmail(String email) {
        _email.setValue(email);
    }

    public void setNombre(String nombre) {
        _nombre.setValue(nombre);
    }
}