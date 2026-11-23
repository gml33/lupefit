package com.lupefit.feature.configuracion.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;

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

    private final SharedPreferences preferences;

    public NotificacionesViewModel(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences("lupefit_settings", Context.MODE_PRIVATE);
        _pushEnabled.setValue(preferences.getBoolean("pushEnabled", true));
        _recordatorioAgua.setValue(preferences.getBoolean("recordatorioAgua", true));
        _recordatorioComida.setValue(preferences.getBoolean("recordatorioComida", true));
        _recordatorioEjercicio.setValue(preferences.getBoolean("recordatorioEjercicio", false));
        _recordatorioPeso.setValue(preferences.getBoolean("recordatorioPeso", false));
        _resumenSemanal.setValue(preferences.getBoolean("resumenSemanal", true));
        _logros.setValue(preferences.getBoolean("logros", true));
        _horaRecordatorioAgua.setValue(preferences.getString("horaRecordatorioAgua", "09:00"));
        _horaRecordatorioComida.setValue(preferences.getString("horaRecordatorioComida", "12:00"));
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
                return (T) new NotificacionesViewModel(context);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void setPushEnabled(boolean value) {
        _pushEnabled.setValue(value);
        savePreferencias();
    }

    public void setRecordatorioAgua(boolean value) {
        _recordatorioAgua.setValue(value);
        savePreferencias();
    }

    public void setRecordatorioComida(boolean value) {
        _recordatorioComida.setValue(value);
        savePreferencias();
    }

    public void setRecordatorioEjercicio(boolean value) {
        _recordatorioEjercicio.setValue(value);
        savePreferencias();
    }

    public void setRecordatorioPeso(boolean value) {
        _recordatorioPeso.setValue(value);
        savePreferencias();
    }

    public void setResumenSemanal(boolean value) {
        _resumenSemanal.setValue(value);
        savePreferencias();
    }

    public void setLogros(boolean value) {
        _logros.setValue(value);
        savePreferencias();
    }

    public void setHoraRecordatorioAgua(String value) {
        _horaRecordatorioAgua.setValue(value);
        savePreferencias();
    }

    public void setHoraRecordatorioComida(String value) {
        _horaRecordatorioComida.setValue(value);
        savePreferencias();
    }

    public void savePreferencias() {
        preferences.edit()
                .putBoolean("pushEnabled", _pushEnabled.getValue())
                .putBoolean("recordatorioAgua", _recordatorioAgua.getValue())
                .putBoolean("recordatorioComida", _recordatorioComida.getValue())
                .putBoolean("recordatorioEjercicio", _recordatorioEjercicio.getValue())
                .putBoolean("recordatorioPeso", _recordatorioPeso.getValue())
                .putBoolean("resumenSemanal", _resumenSemanal.getValue())
                .putBoolean("logros", _logros.getValue())
                .putString("horaRecordatorioAgua", _horaRecordatorioAgua.getValue())
                .putString("horaRecordatorioComida", _horaRecordatorioComida.getValue())
                .apply();
    }
}