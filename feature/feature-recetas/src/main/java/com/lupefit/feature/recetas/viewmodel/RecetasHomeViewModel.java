package com.lupefit.feature.recetas.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lupefit.core.domain.model.Receta;
import com.lupefit.core.domain.repository.RecetaRepository;
import com.lupefit.core.domain.provider.RecetaRepositoryProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecetasHomeViewModel extends ViewModel {

    private final RecetaRepository repository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    // UI State
    public final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    public final MutableLiveData<String> currentFilter = new MutableLiveData<>("todas");
    public final MutableLiveData<String> currentSort = new MutableLiveData<>("fecha");

    // Data
    private final MutableLiveData<List<Receta>> _recetas = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<Receta>> recetas = _recetas;

    private final MutableLiveData<Boolean> _loading = new MutableLiveData<>(false);
    public final LiveData<Boolean> loading = _loading;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    public RecetasHomeViewModel(RecetaRepository repository) {
        this.repository = repository;
        loadRecetas();
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(RecetasHomeViewModel.class)) {
                RecetaRepositoryProvider provider =
                        (RecetaRepositoryProvider) context.getApplicationContext();
                RecetaRepository repository = provider.getRecetaRepository();
                return (T) new RecetasHomeViewModel(repository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void loadRecetas() {
        _loading.setValue(true);
        executor.execute(() -> {
            try {
                List<Receta> recetasList = repository.getAllRecetas();
                _recetas.postValue(recetasList != null ? recetasList : new ArrayList<>());
            } catch (Exception e) {
                _error.postValue("Error cargando recetas: " + e.getMessage());
            } finally {
                _loading.postValue(false);
            }
        });
    }

    public void toggleFavorite(Receta receta) {
        long id = receta.getId();
        executor.execute(() -> {
            try {
                Receta stored = repository.getRecetaById(id);
                if (stored == null) return;
                stored.setEsFavorita(!stored.isEsFavorita());
                stored.setLastModified(System.currentTimeMillis());
                repository.updateReceta(stored);
                _recetas.postValue(repository.getAllRecetas());
            } catch (Exception e) {
                _error.postValue("Error actualizando favorito: " + e.getMessage());
            }
        });
    }

    public void deleteReceta(long id) {
        executor.execute(() -> {
            try {
                repository.deleteReceta(id);
                _recetas.postValue(repository.getAllRecetas());
            } catch (Exception e) {
                _error.postValue("Error eliminando receta: " + e.getMessage());
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}