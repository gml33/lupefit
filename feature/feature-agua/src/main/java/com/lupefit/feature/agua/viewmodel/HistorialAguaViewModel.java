package com.lupefit.feature.agua.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lupefit.core.domain.model.WaterEntry;
import com.lupefit.core.domain.provider.WaterRepositoryProvider;
import com.lupefit.core.domain.repository.WaterRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HistorialAguaViewModel extends ViewModel {

    private final WaterRepository repository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<List<WaterEntry>> _historial = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<WaterEntry>> historial = _historial;

    private final MutableLiveData<Boolean> _loading = new MutableLiveData<>(false);
    public final LiveData<Boolean> loading = _loading;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    public HistorialAguaViewModel(WaterRepository repository) {
        this.repository = repository;
        loadHistorial();
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(HistorialAguaViewModel.class)) {
                WaterRepositoryProvider provider =
                        (WaterRepositoryProvider) context.getApplicationContext();
                WaterRepository repository = provider.getWaterRepository();
                return (T) new HistorialAguaViewModel(repository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void loadHistorial() {
        _loading.setValue(true);
        executor.execute(() -> {
            try {
                List<WaterEntry> entries = repository.getAllWaterEntries();
                _historial.postValue(entries != null ? entries : new ArrayList<>());
            } catch (Exception e) {
                _error.postValue("Error cargando historial: " + e.getMessage());
            } finally {
                _loading.postValue(false);
            }
        });
    }

    public void deleteEntry(WaterEntry entry) {
        executor.execute(() -> {
            try {
                repository.deleteWater(entry.getId());
            } catch (Exception e) {
                _error.postValue("Error eliminando: " + e.getMessage());
            }
            _historial.postValue(repository.getAllWaterEntries());
        });
    }

    @Override
    protected void onCleared() {
        executor.shutdown();
    }
}
