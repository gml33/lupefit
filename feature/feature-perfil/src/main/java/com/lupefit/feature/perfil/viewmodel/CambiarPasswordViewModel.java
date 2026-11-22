package com.lupefit.feature.perfil.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CambiarPasswordViewModel extends ViewModel {

    private final MutableLiveData<String> _passwordActual = new MutableLiveData<>("");
    public final LiveData<String> passwordActual = _passwordActual;

    private final MutableLiveData<String> _passwordNueva = new MutableLiveData<>("");
    public final LiveData<String> passwordNueva = _passwordNueva;

    private final MutableLiveData<String> _confirmPasswordNueva = new MutableLiveData<>("");
    public final LiveData<String> confirmPasswordNueva = _confirmPasswordNueva;

    private final MutableLiveData<Boolean> _cargando = new MutableLiveData<>(false);
    public final LiveData<Boolean> cargando = _cargando;

    private final MutableLiveData<Boolean> _cambiado = new MutableLiveData<>(false);
    public final LiveData<Boolean> cambiado = _cambiado;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    public CambiarPasswordViewModel() {}

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(CambiarPasswordViewModel.class)) {
                return (T) new CambiarPasswordViewModel();
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void cambiarPassword() {
        String actual = _passwordActual.getValue();
        String nueva = _passwordNueva.getValue();
        String confirm = _confirmPasswordNueva.getValue();

        if (actual == null || actual.isEmpty()) {
            _error.setValue("Contraseña actual obligatoria");
            return;
        }

        if (nueva == null || nueva.length() < 6) {
            _error.setValue("La nueva contraseña debe tener al menos 6 caracteres");
            return;
        }

        if (!nueva.equals(confirm)) {
            _error.setValue("Las contraseñas no coinciden");
            return;
        }

        if (actual.equals(nueva)) {
            _error.setValue("La nueva contraseña debe ser diferente a la actual");
            return;
        }

        _cargando.setValue(true);
        _error.setValue(null);

        // TODO: Call AuthRepository.changePassword()
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            _cargando.postValue(false);
            _cambiado.postValue(true);
        }, 1500);
    }
}