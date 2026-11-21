package com.lupefit.feature.ejercicio.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lupefit.core.domain.model.Exercise;
import com.lupefit.core.domain.provider.ExerciseRepositoryProvider;
import com.lupefit.core.domain.repository.ExerciseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BuscarEjercicioViewModel extends ViewModel {

    private final ExerciseRepository exerciseRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<List<Exercise>> _searchResults = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<Exercise>> searchResults = _searchResults;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public final LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    private String lastQuery = "";
    private String currentCategory = "all";
    private String currentPattern = "all";

    public BuscarEjercicioViewModel(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(BuscarEjercicioViewModel.class)) {
                ExerciseRepositoryProvider provider =
                        (ExerciseRepositoryProvider) context.getApplicationContext();
                ExerciseRepository repository = provider.getExerciseRepository();
                return (T) new BuscarEjercicioViewModel(repository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void search(String query) {
        if (query == null || query.equals(lastQuery)) return;
        lastQuery = query;
        _isLoading.setValue(true);
        _error.setValue(null);

        executor.execute(() -> {
            exerciseRepository.searchByName(query, 50, (exercises, error) -> {
                if (error != null) {
                    _isLoading.postValue(false);
                    _error.postValue(error);
                    return;
                }
                List<Exercise> filtered = filterResults(exercises);
                _searchResults.postValue(filtered);
                _isLoading.postValue(false);
            });
        });
    }

    public void setCategory(String category) {
        if (category.equals(currentCategory)) return;
        currentCategory = category;
        if (!lastQuery.isEmpty()) {
            search(lastQuery);
        }
    }

    public void setPattern(String pattern) {
        if (pattern.equals(currentPattern)) return;
        currentPattern = pattern;
        if (!lastQuery.isEmpty()) {
            search(lastQuery);
        }
    }

    private List<Exercise> filterResults(List<Exercise> exercises) {
        List<Exercise> filtered = new ArrayList<>();
        for (Exercise exercise : exercises) {
            boolean matchCategory = "all".equals(currentCategory) || exercise.getCategory().equals(currentCategory);
            boolean matchPattern = "all".equals(currentPattern) || exercise.getMovementPattern().equals(currentPattern);
            if (matchCategory && matchPattern) {
                filtered.add(exercise);
            }
        }
        return filtered;
    }

    public void clearSearch() {
        lastQuery = "";
        _searchResults.postValue(new ArrayList<>());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}