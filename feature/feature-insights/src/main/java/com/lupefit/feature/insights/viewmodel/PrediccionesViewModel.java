package com.lupefit.feature.insights.viewmodel;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class PrediccionesViewModel extends ViewModel {

    private final MutableLiveData<String> _proyeccionPeso = new MutableLiveData<>("—");
    public final LiveData<String> proyeccionPeso = _proyeccionPeso;

    private final MutableLiveData<String> _fechaObjetivo = new MutableLiveData<>("—");
    public final LiveData<String> fechaObjetivo = _fechaObjetivo;

    private final MutableLiveData<String> _proyeccionCalorias = new MutableLiveData<>("—");
    public final LiveData<String> proyeccionCalorias = _proyeccionCalorias;

    private final MutableLiveData<String> _tendenciaProteinas = new MutableLiveData<>("—");
    public final LiveData<String> tendenciaProteinas = _tendenciaProteinas;

    private final MutableLiveData<Boolean> _cargando = new MutableLiveData<>(false);
    public final LiveData<Boolean> cargando = _cargando;

    private final MutableLiveData<String> _rachaActual = new MutableLiveData<>("0 días");
    public final LiveData<String> rachaActual = _rachaActual;

    public PrediccionesViewModel() {
        cargarPrediccionesSimuladas();
    }

    private void cargarPrediccionesSimuladas() {
        _proyeccionPeso.setValue("73.5 kg");
        _fechaObjetivo.setValue("15/02/2027");
        _proyeccionCalorias.setValue("2.150 kcal/día");
        _tendenciaProteinas.setValue("En rango óptimo");
        _rachaActual.setValue("12 días");
    }

    public void recargar() {
        _cargando.setValue(true);
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            cargarPrediccionesSimuladas();
            _cargando.postValue(false);
        }, 500);
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(PrediccionesViewModel.class)) {
                return (T) new PrediccionesViewModel();
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}