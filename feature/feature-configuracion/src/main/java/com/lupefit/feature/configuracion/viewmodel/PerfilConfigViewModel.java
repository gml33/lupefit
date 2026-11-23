package com.lupefit.feature.configuracion.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;

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

    private final SharedPreferences preferences;

    public PerfilConfigViewModel(Context context) {
        preferences = context.getApplicationContext()
                .getSharedPreferences("lupefit_local_profile", Context.MODE_PRIVATE);
        _nombre.setValue(preferences.getString("nombre", ""));
        _apellido.setValue(preferences.getString("apellido", ""));
        _fechaNacimiento.setValue(preferences.getString("fechaNacimiento", ""));
        _sexo.setValue(preferences.getString("sexo", ""));
        _objetivo.setValue(preferences.getString("objetivo", ""));
        _altura.setValue(preferences.getFloat("altura", 0f));
        _peso.setValue(preferences.getFloat("peso", 0f));
        _grasaCorporal.setValue(preferences.getFloat("grasaCorporal", 0f));
        _nivelActividad.setValue(preferences.getInt("nivelActividad", 1));
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
                return (T) new PerfilConfigViewModel(context);
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

        if (alturaVal == null || !Float.isFinite(alturaVal) || alturaVal < 50) {
            _error.setValue("Altura inválida");
            return;
        }

        if (pesoVal == null || !Float.isFinite(pesoVal) || pesoVal < 20) {
            _error.setValue("Peso inválido");
            return;
        }

        _saved.setValue(false);
        _error.setValue(null);

        preferences.edit()
                .putString("nombre", _nombre.getValue())
                .putString("apellido", _apellido.getValue())
                .putString("fechaNacimiento", _fechaNacimiento.getValue())
                .putString("sexo", _sexo.getValue())
                .putString("objetivo", _objetivo.getValue())
                .putFloat("altura", _altura.getValue())
                .putFloat("peso", _peso.getValue())
                .putFloat("grasaCorporal", _grasaCorporal.getValue())
                .putInt("nivelActividad", _nivelActividad.getValue())
                .apply();
        _saved.setValue(true);
    }
    public void setNombre(String value) {
        _nombre.setValue(value);
    }

    public void setApellido(String value) {
        _apellido.setValue(value);
    }

    public void setFechaNacimiento(String value) {
        _fechaNacimiento.setValue(value);
    }

    public void setSexo(String value) {
        _sexo.setValue(value);
    }

    public void setObjetivo(String value) {
        _objetivo.setValue(value);
    }

    public void setAltura(float value) {
        _altura.setValue(value);
    }

    public void setPeso(float value) {
        _peso.setValue(value);
    }

    public void setGrasaCorporal(float value) {
        _grasaCorporal.setValue(value);
    }

    public void setNivelActividad(int value) {
        _nivelActividad.setValue(value);
    }

}
