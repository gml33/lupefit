package com.lupefit.feature.escaneo.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lupefit.core.domain.model.EscaneoEntry;
import com.lupefit.core.domain.provider.EscaneoRepositoryProvider;
import com.lupefit.core.domain.repository.EscaneoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EscanearHomeViewModel extends ViewModel {

    private final EscaneoRepository repository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    // UI State
    private final MutableLiveData<Boolean> _loading = new MutableLiveData<>(false);
    public final LiveData<Boolean> loading = _loading;

    // Data
    private final MutableLiveData<List<EscaneoEntry>> _historial = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<EscaneoEntry>> historial = _historial;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    public EscanearHomeViewModel(EscaneoRepository repository) {
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
            if (modelClass.isAssignableFrom(EscanearHomeViewModel.class)) {
                EscaneoRepositoryProvider provider =
                        (EscaneoRepositoryProvider) context.getApplicationContext();
                EscaneoRepository repository = provider.getEscaneoRepository();
                return (T) new EscanearHomeViewModel(repository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void loadHistorial() {
        _loading.setValue(true);
        executor.execute(() -> {
            try {
                List<EscaneoEntry> escaneos = repository.getAllEscaneos();
                _historial.postValue(escaneos != null ? escaneos : new ArrayList<>());
            } catch (Exception e) {
                _error.postValue("Error cargando historial: " + e.getMessage());
            } finally {
                _loading.postValue(false);
            }
        });
    }

    public void loadEscaneosByType(String tipo) {
        _loading.setValue(true);
        executor.execute(() -> {
            try {
                List<EscaneoEntry> escaneos = repository.getEscaneosByType(tipo);
                _historial.postValue(escaneos != null ? escaneos : new ArrayList<>());
            } catch (Exception e) {
                _error.postValue("Error cargando escaneos: " + e.getMessage());
            } finally {
                _loading.postValue(false);
            }
        });
    }

    public void deleteEscaneo(EscaneoEntry escaneo) {
        executor.execute(() -> {
            try {
                repository.deleteEscaneo(escaneo.getId());
                List<EscaneoEntry> current = _historial.getValue();
                if (current != null) {
                    current.remove(escaneo);
                    _historial.postValue(new ArrayList<>(current));
                }
            } catch (Exception e) {
                _error.postValue("Error eliminando escaneo: " + e.getMessage());
            }
        });
    }

    public void markAsAddedToDiario(EscaneoEntry escaneo) {
        escaneo.setAgregadoAlDiario(true);
        executor.execute(() -> {
            try {
                repository.updateEscaneo(escaneo);
                // Refresh the list
                loadHistorial();
            } catch (Exception e) {
                _error.postValue("Error actualizando: " + e.getMessage());
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}