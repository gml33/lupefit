package com.lupefit.feature.configuracion.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class NotificacionesViewModel extends ViewModel {

    private final MutableLiveData<Boolean> _pushEnabled = new MutableLiveData<>(true);
    public final LiveData<Boolean> pushEnabled = _pushEnabled;

    private final MutableLiveData<Boolean> _recordatorioAgua = new MutableLiveData<>(true);
    public final LiveData<Boolean> recordatorioAgua = _recordatorioAgua;

    private final MutableLiveData<Boolean> _recordatorioComida = new MutableLiveData<>(true);
    public final LiveData<Boolean> recordatorioComida = _recordatorioComida;

    private final MutableLiveData<Boolean> _recordatorioEjercicio = new MutableLiveData<>(false);
    public final LiveData<Boolean> recordatorioEjercicio = _recordatorioEjercicio;

    private final MutableLiveData<Boolean> _recordatorioPeso = new MutableLiveData<>(false);
    public final LiveData<Boolean> recordatorioPeso = _recordatorioPeso;

    private final MutableLiveData<Boolean> _resumenSemanal = new MutableLiveData<>(true);
    public final LiveData<Boolean> resumenSemanal = _resumenSemanal;

    private final MutableLiveData<Boolean> _logros = new MutableLiveData<>(true);
    public final LiveData<Boolean> logros = _logros;

    private final MutableLiveData<String> _horaRecordatorioAgua = new MutableLiveData<>("09:00");
    public final LiveData<String> horaRecordatorioAgua = _horaRecordatorioAgua;

    private final MutableLiveData<String> _horaRecordatorioComida = new MutableLiveData<>("12:00");
    public final LiveData<String> horaRecordatorioComida = _horaRecordatorioComida;

    public NotificacionesViewModel() {
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
            if (modelClass.isAssignableFrom(NotificacionesViewModel.class)) {
                return (T) new NotificacionesViewModel();
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void savePreferencias() {
        // TODO: Save all preferences
    }
}