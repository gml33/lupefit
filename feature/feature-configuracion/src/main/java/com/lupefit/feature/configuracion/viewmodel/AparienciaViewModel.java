package com.lupefit.feature.configuracion.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AparienciaViewModel extends ViewModel {

    public enum Tema {
        SISTEMA, CLARO, OSCURO
    }

    private final MutableLiveData<Tema> _temaSeleccionado = new MutableLiveData<>(Tema.SISTEMA);
    public final LiveData<Tema> temaSeleccionado = _temaSeleccionado;

    private final MutableLiveData<Boolean> _animaciones = new MutableLiveData<>(true);
    public final LiveData<Boolean> animaciones = _animaciones;

    private final MutableLiveData<Boolean> _reduccionMovimiento = new MutableLiveData<>(false);
    public final LiveData<Boolean> reduccionMovimiento = _reduccionMovimiento;

    public AparienciaViewModel() {
        // TODO: Load from preferences
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(AparienciaViewModel.class)) {
                return (T) new AparienciaViewModel();
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void setTema(Tema tema) {
        _temaSeleccionado.setValue(tema);
        // TODO: Apply theme immediately
    }

    public void toggleAnimaciones() {
        _animaciones.setValue(!(_animaciones.getValue() != null && _animaciones.getValue()));
    }

    public void toggleReduccionMovimiento() {
        _reduccionMovimiento.setValue(!(_reduccionMovimiento.getValue() != null && _reduccionMovimiento.getValue()));
    }
}