package com.lupefit.feature.configuracion.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class UnidadesViewModel extends ViewModel {

    public enum SistemaUnidades {
        METRICO, IMPERIAL
    }

    private final MutableLiveData<SistemaUnidades> _sistema = new MutableLiveData<>(SistemaUnidades.METRICO);
    public final LiveData<SistemaUnidades> sistema = _sistema;

    private final MutableLiveData<Boolean> _mostrarKcal = new MutableLiveData<>(true);
    public final LiveData<Boolean> mostrarKcal = _mostrarKcal;

    private final MutableLiveData<Boolean> _mostrarKj = new MutableLiveData<>(false);
    public final LiveData<Boolean> mostrarKj = _mostrarKj;

    private final SharedPreferences preferences;

    public UnidadesViewModel(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences("lupefit_settings", Context.MODE_PRIVATE);
        _sistema.setValue(SistemaUnidades.valueOf(preferences.getString("unidades", SistemaUnidades.METRICO.name())));
        _mostrarKcal.setValue(preferences.getBoolean("mostrar_kcal", true));
        _mostrarKj.setValue(preferences.getBoolean("mostrar_kj", false));
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(UnidadesViewModel.class)) {
                return (T) new UnidadesViewModel(context);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void setSistema(SistemaUnidades sistema) {
        _sistema.setValue(sistema);
        savePreferences();
    }

    public void toggleKcal() {
        _mostrarKcal.setValue(!(_mostrarKcal.getValue() != null && _mostrarKcal.getValue()));
        savePreferences();
    }

    public void toggleKj() {
        _mostrarKj.setValue(!(_mostrarKj.getValue() != null && _mostrarKj.getValue()));
        savePreferences();
    }

    private void savePreferences() {
        preferences.edit()
                .putString("unidades", _sistema.getValue().name())
                .putBoolean("mostrar_kcal", _mostrarKcal.getValue())
                .putBoolean("mostrar_kj", _mostrarKj.getValue())
                .apply();
    }
}