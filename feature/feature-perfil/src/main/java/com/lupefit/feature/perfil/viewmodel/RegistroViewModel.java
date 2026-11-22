package com.lupefit.feature.perfil.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class RegistroViewModel extends ViewModel {

    private final MutableLiveData<String> _nombre = new MutableLiveData<>("");
    public final LiveData<String> nombre = _nombre;

    private final MutableLiveData<String> _apellido = new MutableLiveData<>("");
    public final LiveData<String> apellido = _apellido;

    private final MutableLiveData<String> _email = new MutableLiveData<>("");
    public final LiveData<String> email = _email;

    private final MutableLiveData<String> _password = new MutableLiveData<>("");
    public final LiveData<String> password = _password;

    private final MutableLiveData<String> _confirmPassword = new MutableLiveData<>("");
    public final LiveData<String> confirmPassword = _confirmPassword;

    private final MutableLiveData<Boolean> _terminos = new MutableLiveData<>(false);
    public final LiveData<Boolean> terminos = _terminos;

    private final MutableLiveData<Boolean> _cargando = new MutableLiveData<>(false);
    public final LiveData<Boolean> cargando = _cargando;

    private final MutableLiveData<Boolean> _registrado = new MutableLiveData<>(false);
    public final LiveData<Boolean> registrado = _registrado;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    public RegistroViewModel() {}

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(RegistroViewModel.class)) {
                return (T) new RegistroViewModel();
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void registrar() {
        String nombreVal = _nombre.getValue();
        String apellidoVal = _apellido.getValue();
        String emailVal = _email.getValue();
        String passVal = _password.getValue();
        String confirmVal = _confirmPassword.getValue();
        Boolean terminosVal = _terminos.getValue();

        if (nombreVal == null || nombreVal.trim().isEmpty()) {
            _error.setValue("El nombre es obligatorio");
            return;
        }

        if (apellidoVal == null || apellidoVal.trim().isEmpty()) {
            _error.setValue("El apellido es obligatorio");
            return;
        }

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

        if (!passVal.equals(confirmVal)) {
            _error.setValue("Las contraseñas no coinciden");
            return;
        }

        if (terminosVal == null || !terminosVal) {
            _error.setValue("Debes aceptar los términos y condiciones");
            return;
        }

        _cargando.setValue(true);
        _error.setValue(null);

        // TODO: Call AuthRepository.register()
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            _cargando.postValue(false);
            _registrado.postValue(true);
        }, 1500);
    }
}