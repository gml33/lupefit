package com.lupefit.feature.agua.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ConfigurarMetaViewModel extends ViewModel {

    private final SharedPreferences preferences;

    private final MutableLiveData<String> _metaMlStr = new MutableLiveData<>("");
    public final LiveData<String> metaMlStr = _metaMlStr;

    private final MutableLiveData<String> _currentGoalStr = new MutableLiveData<>("2500 ml");
    public final LiveData<String> currentGoalStr = _currentGoalStr;

    private final MutableLiveData<Boolean> _saved = new MutableLiveData<>(false);
    public final LiveData<Boolean> saved = _saved;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    public ConfigurarMetaViewModel(SharedPreferences preferences) {
        this.preferences = preferences;
        int goal = preferences.getInt("goal_ml", 2500);
        _metaMlStr.setValue(String.valueOf(goal));
        _currentGoalStr.setValue(goal + " ml");
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ConfigurarMetaViewModel.class)) {
                return (T) new ConfigurarMetaViewModel(
                        context.getSharedPreferences("hydration", Context.MODE_PRIVATE));
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void saveMeta() {
        String input = _metaMlStr.getValue();
        if (input == null || input.trim().isEmpty()) {
            _error.setValue("Ingresá una meta válida");
            return;
        }

        int meta;
        try {
            meta = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            _error.setValue("Meta inválida");
            return;
        }

        if (meta <= 0 || meta > 10000) {
            _error.setValue("La meta debe estar entre 1 y 10000 ml");
            return;
        }

        _saved.setValue(false);
        _error.setValue(null);

        preferences.edit().putInt("goal_ml", meta).apply();
        _saved.setValue(true);
    }

    public void setPreset(int ml) {
        _metaMlStr.setValue(String.valueOf(ml));
    }

    public void updateMetaInput(String value) {
        _metaMlStr.setValue(value);
    }
}