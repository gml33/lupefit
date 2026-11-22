package com.lupefit.feature.configuracion.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MetasConfigViewModel extends ViewModel {

    private final MutableLiveData<Integer> _calorias = new MutableLiveData<>(2000);
    public final LiveData<Integer> calorias = _calorias;

    private final MutableLiveData<Integer> _proteinas = new MutableLiveData<>(150);
    public final LiveData<Integer> proteinas = _proteinas;

    private final MutableLiveData<Integer> _carbohidratos = new MutableLiveData<>(250);
    public final LiveData<Integer> carbohidratos = _carbohidratos;

    private final MutableLiveData<Integer> _grasas = new MutableLiveData<>(65);
    public final LiveData<Integer> grasas = _grasas;

    private final MutableLiveData<Integer> _fibra = new MutableLiveData<>(25);
    public final LiveData<Integer> fibra = _fibra;

    private final MutableLiveData<Integer> _agua = new MutableLiveData<>(2500);
    public final LiveData<Integer> agua = _agua;

    private final MutableLiveData<Boolean> _metasPorComida = new MutableLiveData<>(false);
    public final LiveData<Boolean> metasPorComida = _metasPorComida;

    private final MutableLiveData<Boolean> _netCarbs = new MutableLiveData<>(false);
    public final LiveData<Boolean> netCarbs = _netCarbs;

    private final MutableLiveData<Boolean> _saved = new MutableLiveData<>(false);
    public final LiveData<Boolean> saved = _saved;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    public MetasConfigViewModel() {
        // TODO: Load current goals from preferences
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(MetasConfigViewModel.class)) {
                return (T) new MetasConfigViewModel();
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void saveMetas() {
        Integer cal = _calorias.getValue();
        Integer prot = _proteinas.getValue();
        Integer carb = _carbohidratos.getValue();
        Integer grasa = _grasas.getValue();

        if (cal == null || cal < 1000 || cal > 5000) {
            _error.setValue("Calorías deben estar entre 1000 y 5000");
            return;
        }

        if (prot == null || prot < 30 || prot > 400) {
            _error.setValue("Proteínas deben estar entre 30 y 400g");
            return;
        }

        if (carb == null || carb < 50 || carb > 600) {
            _error.setValue("Carbohidratos deben estar entre 50 y 600g");
            return;
        }

        if (grasa == null || grasa < 20 || grasa > 200) {
            _error.setValue("Grasas deben estar entre 20 y 200g");
            return;
        }

        int totalMacros = (prot * 4) + (carb * 4) + (grasa * 9);
        if (Math.abs(totalMacros - cal) > 200) {
            _error.setValue("La suma de macros no coincide con las calorías");
            return;
        }

        _saved.setValue(false);
        _error.setValue(null);

        // TODO: Save to preferences
        _saved.postValue(true);
    }

    public void setPreset(int calorias, int proteinas, int carbohidratos, int grasas) {
        _calorias.setValue(calorias);
        _proteinas.setValue(proteinas);
        _carbohidratos.setValue(carbohidratos);
        _grasas.setValue(grasas);
    }

    public void toggleMetasPorComida() {
        _metasPorComida.setValue(!(_metasPorComida.getValue() != null && _metasPorComida.getValue()));
    }

    public void toggleNetCarbs() {
        _netCarbs.setValue(!(_netCarbs.getValue() != null && _netCarbs.getValue()));
    }
}