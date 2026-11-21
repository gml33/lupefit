package com.lupefit.feature.nutricion.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lupefit.core.domain.provider.DiarioRepositoryProvider;
import com.lupefit.core.domain.repository.DiarioRepository;
import com.lupefit.core.domain.model.MacroTotals;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NutricionHomeViewModel extends ViewModel {

    private final DiarioRepository diarioRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<MacroTotals> _totales = new MutableLiveData<>();
    public final LiveData<MacroTotals> totales = _totales;

    private final MutableLiveData<Boolean> _hasData = new MutableLiveData<>(false);
    public final LiveData<Boolean> hasData = _hasData;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public final LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    private int currentDateKey;

    public NutricionHomeViewModel(DiarioRepository diarioRepository) {
        this.diarioRepository = diarioRepository;
        currentDateKey = getTodayDateKey();
        loadTotales();
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(NutricionHomeViewModel.class)) {
                DiarioRepositoryProvider provider =
                        (DiarioRepositoryProvider) context.getApplicationContext();
                DiarioRepository repository = provider.getDiarioRepository();
                return (T) new NutricionHomeViewModel(repository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    private void loadTotales() {
        _isLoading.setValue(true);
        _error.setValue(null);

        executor.execute(() -> {
            LiveData<MacroTotals> liveData = diarioRepository.getDailyTotals(currentDateKey);
            MacroTotals totales = liveData.getValue();
            if (totales != null && totales.getCalorias() > 0) {
                _totales.postValue(totales);
                _hasData.postValue(true);
            } else {
                _hasData.postValue(false);
            }
            _isLoading.postValue(false);
        });
    }

    public void refresh() {
        loadTotales();
    }

    private int getTodayDateKey() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR) * 10000
                + (cal.get(Calendar.MONTH) + 1) * 100
                + cal.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}