package com.lupefit.feature.configuracion.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
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

    private final SharedPreferences preferences;
    private final Context context;

    public AparienciaViewModel(Context context) {
        this.context = context.getApplicationContext();
        preferences = this.context.getSharedPreferences("lupefit_settings", Context.MODE_PRIVATE);
        _temaSeleccionado.setValue(Tema.valueOf(preferences.getString("tema", Tema.SISTEMA.name())));
        _animaciones.setValue(preferences.getBoolean("animaciones", true));
        _reduccionMovimiento.setValue(preferences.getBoolean("reduccion_movimiento", false));
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
                return (T) new AparienciaViewModel(context);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void setTema(Tema tema) {
        _temaSeleccionado.setValue(tema);
        savePreferences();
        applySavedTheme(context);
    }

    public static void applySavedTheme(Context context) {
        String theme = context.getSharedPreferences("lupefit_settings", Context.MODE_PRIVATE)
                .getString("tema", Tema.SISTEMA.name());
        int mode;
        if (Tema.CLARO.name().equals(theme)) {
            mode = AppCompatDelegate.MODE_NIGHT_NO;
        } else if (Tema.OSCURO.name().equals(theme)) {
            mode = AppCompatDelegate.MODE_NIGHT_YES;
        } else {
            mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        }
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    public void toggleAnimaciones() {
        _animaciones.setValue(!(_animaciones.getValue() != null && _animaciones.getValue()));
        savePreferences();
    }

    public void toggleReduccionMovimiento() {
        _reduccionMovimiento.setValue(!(_reduccionMovimiento.getValue() != null && _reduccionMovimiento.getValue()));
        savePreferences();
    }

    private void savePreferences() {
        preferences.edit()
                .putString("tema", _temaSeleccionado.getValue().name())
                .putBoolean("animaciones", _animaciones.getValue())
                .putBoolean("reduccion_movimiento", _reduccionMovimiento.getValue())
                .apply();
    }
}