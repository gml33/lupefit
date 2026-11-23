package com.lupefit.feature.nutricion.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lupefit.core.domain.provider.DiarioRepositoryProvider;
import com.lupefit.core.domain.repository.DiarioRepository;
import com.lupefit.core.domain.model.MacroTotals;

import java.util.Calendar;

public class NutricionHomeViewModel extends ViewModel {

    public final LiveData<MacroTotals> totales;
    public final LiveData<Boolean> hasData;

    public NutricionHomeViewModel(DiarioRepository diarioRepository) {
        int today = getTodayDateKey();
        totales = diarioRepository.getDailyTotals(today);
        hasData = Transformations.map(diarioRepository.getEntriesForDate(today),
                entries -> entries != null && !entries.isEmpty());
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

    private int getTodayDateKey() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR) * 10000
                + (cal.get(Calendar.MONTH) + 1) * 100
                + cal.get(Calendar.DAY_OF_MONTH);
    }

}
