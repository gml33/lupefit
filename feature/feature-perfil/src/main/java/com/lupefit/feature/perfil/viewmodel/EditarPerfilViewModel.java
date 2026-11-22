package com.lupefit.feature.perfil.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class EditarPerfilViewModel extends ViewModel {

    private final MutableLiveData<String> _nombre = new MutableLiveData<>("");
    public final LiveData<String> nombre = _nombre;

    private final MutableLiveData<String> _apellido = new MutableLiveData<>("");
    public final LiveData<String> apellido = _apellido;

    private final MutableLiveData<String> _email = new MutableLiveData<>("");
    public final LiveData<String> email = _email;

    private final MutableLiveData<String> _fechaNacimiento = new MutableLiveData<>("");
    public final LiveData<String> fechaNacimiento = _fechaNacimiento;

    private final MutableLiveData<String> _sexo = new MutableLiveData<>("");
    public final LiveData<String> sexo = _sexo;

    private final MutableLiveData<Float> _altura = new MutableLiveData<>(0f);
    public final LiveData<Float> altura = _altura;

    private final MutableLiveData<Float> _peso = new MutableLiveData<>(0f);
    public final LiveData<Float> peso = _peso;

    private final MutableLiveData<Integer> _nivelActividad = new MutableLiveData<>(1);
    public final LiveData<Integer> nivelActividad = _nivelActividad;

    private final MutableLiveData<String> _objetivo = new MutableLiveData<>("");
    public final LiveData<String> objetivo = _objetivo;

    private final MutableLiveData<Boolean> _guardando = new MutableLiveData<>(false);
    public final LiveData<Boolean> guardando = _guardando;

    private final MutableLiveData<Boolean> _guardado = new MutableLiveData<>(false);
    public final LiveData<Boolean> guardado = _guardado;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    public EditarPerfilViewModel() {
        // TODO: Load current user profile
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(EditarPerfilViewModel.class)) {
                return (T) new EditarPerfilViewModel();
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void guardarPerfil() {
        String nombreVal = _nombre.getValue();
        String apellidoVal = _apellido.getValue();
        String emailVal = _email.getValue();
        Float alturaVal = _altura.getValue();
        Float pesoVal = _peso.getValue();

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

        if (alturaVal == null || alturaVal <= 0 || alturaVal > 250) {
            _error.setValue("Altura inválida (50-250 cm)");
            return;
        }

        if (pesoVal == null || pesoVal <= 0 || pesoVal > 300) {
            _error.setValue("Peso inválido (20-300 kg)");
            return;
        }

        _guardando.setValue(true);
        _error.setValue(null);

        // TODO: Save to repository
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            _guardando.postValue(false);
            _guardado.postValue(true);
        }, 1000);
    }
}