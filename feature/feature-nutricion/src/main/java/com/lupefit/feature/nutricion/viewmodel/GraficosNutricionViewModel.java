package com.lupefit.feature.nutricion.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lupefit.core.domain.provider.DiarioRepositoryProvider;
import com.lupefit.core.domain.repository.DiarioRepository;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GraficosNutricionViewModel extends ViewModel {

    private final DiarioRepository diarioRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<Integer> _selectedPeriod = new MutableLiveData<>(7);
    public final LiveData<Integer> selectedPeriod = _selectedPeriod;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public final LiveData<Boolean> isLoading = _isLoading;

    public GraficosNutricionViewModel(DiarioRepository diarioRepository) {
        this.diarioRepository = diarioRepository;
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(GraficosNutricionViewModel.class)) {
                DiarioRepositoryProvider provider =
                        (DiarioRepositoryProvider) context.getApplicationContext();
                DiarioRepository repository = provider.getDiarioRepository();
                return (T) new GraficosNutricionViewModel(repository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void setPeriod(int days) {
        _selectedPeriod.setValue(days);
    }

    public void refresh() {
        // TODO: Load chart data
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}