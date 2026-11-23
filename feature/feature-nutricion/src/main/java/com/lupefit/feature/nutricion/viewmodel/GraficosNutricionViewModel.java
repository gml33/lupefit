package com.lupefit.feature.nutricion.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;

import com.lupefit.core.domain.provider.DiarioRepositoryProvider;
import com.lupefit.core.domain.repository.DiarioRepository;

import com.lupefit.core.domain.model.DiarioEntry;
import com.lupefit.core.domain.model.MacroTotals;

import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

public class GraficosNutricionViewModel extends ViewModel {

    private final MutableLiveData<Integer> selectedPeriod = new MutableLiveData<>(7);
    public final LiveData<Map<Integer, MacroTotals>> history;

    public GraficosNutricionViewModel(DiarioRepository diarioRepository) {
        history = Transformations.switchMap(selectedPeriod, days -> {
            Calendar date = Calendar.getInstance();
            int end = dateKey(date);
            date.add(Calendar.DAY_OF_YEAR, 1 - days);
            return Transformations.map(diarioRepository.getEntriesForDateRange(dateKey(date), end), entries -> {
                Map<Integer, MacroTotals> daily = new TreeMap<>();
                if (entries == null) return daily;
                for (DiarioEntry entry : entries) {
                    MacroTotals previous = daily.getOrDefault(entry.getDateKey(), new MacroTotals());
                    daily.put(entry.getDateKey(), new MacroTotals(
                            previous.getCalorias() + entry.getCalorias(),
                            previous.getProteinas() + entry.getProteinas(),
                            previous.getCarbohidratos() + entry.getCarbohidratos(),
                            previous.getGrasas() + entry.getGrasas()));
                }
                return daily;
            });
        });
    }

    private static int dateKey(Calendar date) {
        return date.get(Calendar.YEAR) * 10000
                + (date.get(Calendar.MONTH) + 1) * 100 + date.get(Calendar.DAY_OF_MONTH);
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
        selectedPeriod.setValue(days);
    }
}
