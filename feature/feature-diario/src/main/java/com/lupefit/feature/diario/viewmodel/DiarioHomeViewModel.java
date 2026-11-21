package com.lupefit.feature.diario.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lupefit.core.domain.provider.DiarioRepositoryProvider;
import com.lupefit.core.domain.model.DiarioEntry;
import com.lupefit.core.domain.model.MacroTotals;
import com.lupefit.core.domain.repository.DiarioRepository;

import java.util.ArrayList;
import java.util.List;

public class DiarioHomeViewModel extends ViewModel {

    private final DiarioRepository repository;
    private final LiveData<List<DiarioEntry>> diarioEntries;
    private final LiveData<MacroTotals> totales;

    public DiarioHomeViewModel(DiarioRepository repository, long userId) {
        this.repository = repository;
        this.diarioEntries = repository.getEntriesForDate(getTodayDateKey());
        this.totales = repository.getDailyTotals(getTodayDateKey());
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;
        private final long userId;

        public Factory(Context context, long userId) {
            this.context = context;
            this.userId = userId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(DiarioHomeViewModel.class)) {
                com.lupefit.core.domain.provider.DiarioRepositoryProvider provider =
                    (com.lupefit.core.domain.provider.DiarioRepositoryProvider) context.getApplicationContext();
                DiarioRepository repository = provider.getDiarioRepository();
                return (T) new DiarioHomeViewModel(repository, userId);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public LiveData<List<DiarioEntry>> getDiarioEntries() {
        return diarioEntries;
    }

    public LiveData<MacroTotals> getTotales() {
        return totales;
    }

    public void loadDiarioDelDia() {
        // Repository already loads data via LiveData
        // This method can be used to trigger refresh if needed
    }

    public void addEntry(DiarioEntry entry) {
        repository.insert(entry);
    }

    public void updateEntry(DiarioEntry entry) {
        repository.update(entry);
    }

    public void deleteEntry(DiarioEntry entry) {
        repository.delete(entry);
    }

    private int getTodayDateKey() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        return cal.get(java.util.Calendar.YEAR) * 10000
                + (cal.get(java.util.Calendar.MONTH) + 1) * 100
                + cal.get(java.util.Calendar.DAY_OF_MONTH);
    }
}