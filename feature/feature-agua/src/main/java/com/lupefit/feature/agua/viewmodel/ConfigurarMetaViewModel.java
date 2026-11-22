package com.lupefit.feature.agua.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lupefit.core.domain.provider.WaterRepositoryProvider;
import com.lupefit.core.domain.repository.WaterRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConfigurarMetaViewModel extends ViewModel {

    private final WaterRepository repository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<String> _metaMlStr = new MutableLiveData<>("");
    public final LiveData<String> metaMlStr = _metaMlStr;

    private final MutableLiveData<String> _currentGoalStr = new MutableLiveData<>("2500 ml");
    public final LiveData<String> currentGoalStr = _currentGoalStr;

    private final MutableLiveData<Boolean> _saved = new MutableLiveData<>(false);
    public final LiveData<Boolean> saved = _saved;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    private int currentGoalMl = 2500;

    public ConfigurarMetaViewModel(WaterRepository repository) {
        this.repository = repository;
        // TODO: Load current goal from preferences
        _currentGoalStr.setValue(currentGoalMl + " ml");
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
                WaterRepositoryProvider provider =
                        (WaterRepositoryProvider) context.getApplicationContext();
                WaterRepository repository = provider.getWaterRepository();
                return (T) new ConfigurarMetaViewModel(repository);
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

        // TODO: Save to preferences
        _saved.postValue(true);
    }

    public void setPreset(int ml) {
        currentGoalMl = ml;
        _metaMlStr.setValue(String.valueOf(ml));
        _currentGoalStr.setValue(ml + " ml");
    }

    public void updateMetaInput(String value) {
        _metaMlStr.postValue(value);
    }
}