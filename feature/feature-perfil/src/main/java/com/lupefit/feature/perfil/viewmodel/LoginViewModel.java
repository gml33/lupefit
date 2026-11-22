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

    public LoginViewModel() {}

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(LoginViewModel.class)) {
                return (T) new LoginViewModel();
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

        // TODO: Call AuthRepository.login()
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            _cargando.postValue(false);
            _autenticado.postValue(true);
        }, 1500);
    }

    public void setEmail(String email) {
        _email.setValue(email);
    }

    public void setPassword(String password) {
        _password.setValue(password);
    }
}