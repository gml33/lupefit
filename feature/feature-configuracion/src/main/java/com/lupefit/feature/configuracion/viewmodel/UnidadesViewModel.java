package com.lupefit.feature.configuracion.viewmodel;

import android.content.Context;

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

    public UnidadesViewModel() {
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
            if (modelClass.isAssignableFrom(UnidadesViewModel.class)) {
                return (T) new UnidadesViewModel();
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void setSistema(SistemaUnidades sistema) {
        _sistema.setValue(sistema);
        // TODO: Save to preferences
    }

    public void toggleKcal() {
        _mostrarKcal.setValue(!(_mostrarKcal.getValue() != null && _mostrarKcal.getValue()));
    }

    public void toggleKj() {
        _mostrarKj.setValue(!(_mostrarKj.getValue() != null && _mostrarKj.getValue()));
    }
}