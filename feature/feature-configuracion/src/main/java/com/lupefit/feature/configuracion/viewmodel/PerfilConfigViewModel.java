package com.lupefit.feature.configuracion.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class PerfilConfigViewModel extends ViewModel {

    private final MutableLiveData<String> _nombre = new MutableLiveData<>("");
    public final LiveData<String> nombre = _nombre;

    private final MutableLiveData<String> _apellido = new MutableLiveData<>("");
    public final LiveData<String> apellido = _apellido;

    private final MutableLiveData<String> _fechaNacimiento = new MutableLiveData<>("");
    public final LiveData<String> fechaNacimiento = _fechaNacimiento;

    private final MutableLiveData<String> _sexo = new MutableLiveData<>("");
    public final LiveData<String> sexo = _sexo;

    private final MutableLiveData<Float> _altura = new MutableLiveData<>(0f);
    public final LiveData<Float> altura = _altura;

    private final MutableLiveData<Float> _peso = new MutableLiveData<>(0f);
    public final LiveData<Float> peso = _peso;

    private final MutableLiveData<Float> _grasaCorporal = new MutableLiveData<>(0f);
    public final LiveData<Float> grasaCorporal = _grasaCorporal;

    private final MutableLiveData<Integer> _nivelActividad = new MutableLiveData<>(1);
    public final LiveData<Integer> nivelActividad = _nivelActividad;

    private final MutableLiveData<String> _objetivo = new MutableLiveData<>("");
    public final LiveData<String> objetivo = _objetivo;

    private final MutableLiveData<Boolean> _saved = new MutableLiveData<>(false);
    public final LiveData<Boolean> saved = _saved;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    public PerfilConfigViewModel() {
        // TODO: Load current profile data
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(PerfilConfigViewModel.class)) {
                return (T) new PerfilConfigViewModel();
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void savePerfil() {
        String nombreVal = _nombre.getValue();
        String apellidoVal = _apellido.getValue();

        if (nombreVal == null || nombreVal.trim().isEmpty()) {
            _error.setValue("El nombre es obligatorio");
            return;
        }

        if (apellidoVal == null || apellidoVal.trim().isEmpty()) {
            _error.setValue("El apellido es obligatorio");
            return;
        }

        Float alturaVal = _altura.getValue();
        Float pesoVal = _peso.getValue();

        if (alturaVal == null || alturaVal <= 0) {
            _error.setValue("Altura inválida");
            return;
        }

        if (pesoVal == null || pesoVal <= 0) {
            _error.setValue("Peso inválido");
            return;
        }

        _saved.setValue(false);
        _error.setValue(null);

        // TODO: Save to repository/preferences
        _saved.postValue(true);
    }
}