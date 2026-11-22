package com.lupefit.feature.configuracion.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class PresupuestoViewModel extends ViewModel {

    private final MutableLiveData<Float> _metaSemanal = new MutableLiveData<>(0f);
    public final LiveData<Float> metaSemanal = _metaSemanal;

    private final MutableLiveData<Float> _gastoActual = new MutableLiveData<>(0f);
    public final LiveData<Float> gastoActual = _gastoActual;

    private final MutableLiveData<Integer> _itemsSinPrecio = new MutableLiveData<>(0);
    public final LiveData<Integer> itemsSinPrecio = _itemsSinPrecio;

    private final MutableLiveData<Boolean> _cargando = new MutableLiveData<>(false);
    public final LiveData<Boolean> cargando = _cargando;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    public PresupuestoViewModel() {
        cargarPresupuesto();
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(PresupuestoViewModel.class)) {
                return (T) new PresupuestoViewModel();
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void cargarPresupuesto() {
        _cargando.setValue(true);
        // TODO: Load from CostBudgetEngine / UserPreferencesManager
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            _metaSemanal.postValue(5000f);
            _gastoActual.postValue(3200f);
            _itemsSinPrecio.postValue(3);
            _cargando.postValue(false);
        }, 500);
    }

    public void setMetaSemanal(float meta) {
        if (meta >= 0) {
            _metaSemanal.setValue(meta);
            // TODO: Save to UserPreferencesManager
        }
    }

    public float getPorcentajeGasto() {
        Float meta = _metaSemanal.getValue();
        Float gasto = _gastoActual.getValue();
        if (meta != null && gasto != null && meta > 0) {
            return (gasto / meta) * 100f;
        }
        return 0f;
    }

    public String getEstadoPresupuesto() {
        float pct = getPorcentajeGasto();
        if (pct >= 100) return "Excedido";
        if (pct >= 80) return "Cerca del límite";
        return "Dentro del presupuesto";
    }

    public int getEstadoColorRes() {
        float pct = getPorcentajeGasto();
        if (pct >= 100) return com.lupefit.core.ui.R.color.status_error;
        if (pct >= 80) return com.lupefit.core.ui.R.color.status_warning;
        return com.lupefit.core.ui.R.color.status_success;
    }
}