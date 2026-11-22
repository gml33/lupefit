package com.lupefit.feature.configuracion.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CostosViewModel extends ViewModel {

    private final MutableLiveData<Boolean> _enabled = new MutableLiveData<>(true);
    public final LiveData<Boolean> enabled = _enabled;

    private final MutableLiveData<String> _moneda = new MutableLiveData<>("ARS");
    public final LiveData<String> moneda = _moneda;

    private final MutableLiveData<Integer> _diasAlertaPrecio = new MutableLiveData<>(30);
    public final LiveData<Integer> diasAlertaPrecio = _diasAlertaPrecio;

    public CostosViewModel() {
        // TODO: Load from UserPreferencesManager
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(CostosViewModel.class)) {
                return (T) new CostosViewModel();
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void setEnabled(boolean enabled) {
        _enabled.setValue(enabled);
        // TODO: Save to UserPreferencesManager
    }

    public void setMoneda(String moneda) {
        _moneda.setValue(moneda);
    }

    public void setDiasAlerta(int dias) {
        if (dias > 0 && dias <= 365) {
            _diasAlertaPrecio.setValue(dias);
        }
    }
}