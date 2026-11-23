package com.lupefit.feature.configuracion.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;

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

    private final SharedPreferences preferences;

    public CostosViewModel(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences("lupefit_user_preferences", Context.MODE_PRIVATE);
        _enabled.setValue(preferences.getBoolean("enable_cost_tracking", true));
        _moneda.setValue(preferences.getString("currency_code", "ARS"));
        _diasAlertaPrecio.setValue(preferences.getInt("days_price_alert", 30));
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
                return (T) new CostosViewModel(context);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void setEnabled(boolean enabled) {
        _enabled.setValue(enabled);
        savePreferences();
    }

    public void setMoneda(String moneda) {
        _moneda.setValue(moneda);
        savePreferences();
    }

    public void setDiasAlerta(int dias) {
        if (dias > 0 && dias <= 365) {
            _diasAlertaPrecio.setValue(dias);
        savePreferences();
        }
    }

    private void savePreferences() {
        preferences.edit()
                .putBoolean("enable_cost_tracking", _enabled.getValue())
                .putString("currency_code", _moneda.getValue())
                .putInt("days_price_alert", _diasAlertaPrecio.getValue())
                .apply();
    }
}