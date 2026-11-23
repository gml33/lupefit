package com.lupefit.feature.agua.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lupefit.core.domain.model.WaterEntry;
import com.lupefit.core.domain.provider.WaterRepositoryProvider;
import com.lupefit.core.domain.repository.WaterRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AguaHomeViewModel extends ViewModel {

    private final WaterRepository repository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    // UI State
    private final MutableLiveData<Integer> _progressPercent = new MutableLiveData<>(0);
    public final LiveData<Integer> progressPercent = _progressPercent;

    private final MutableLiveData<String> _consumedStr = new MutableLiveData<>("0 ml");
    public final LiveData<String> consumedStr = _consumedStr;

    private final MutableLiveData<String> _dailyGoalStr = new MutableLiveData<>("2500 ml");
    public final LiveData<String> dailyGoalStr = _dailyGoalStr;

    private final MutableLiveData<String> _remainingStr = new MutableLiveData<>("2500 ml");
    public final LiveData<String> remainingStr = _remainingStr;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    private final MutableLiveData<Boolean> _loading = new MutableLiveData<>(false);
    public final LiveData<Boolean> loading = _loading;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    // Historial
    private final MutableLiveData<List<WaterEntry>> _historial = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<WaterEntry>> historial = _historial;

    private final SharedPreferences preferences;

    public AguaHomeViewModel(WaterRepository repository, SharedPreferences preferences) {
        this.repository = repository;
        this.preferences = preferences;
        loadTodayData();
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(AguaHomeViewModel.class)) {
                WaterRepositoryProvider provider =
                        (WaterRepositoryProvider) context.getApplicationContext();
                WaterRepository repository = provider.getWaterRepository();
                return (T) new AguaHomeViewModel(repository, context.getSharedPreferences("hydration", Context.MODE_PRIVATE));
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void loadTodayData() {
        _loading.postValue(true);
        String today = dateFormat.format(Calendar.getInstance().getTime());

        executor.execute(() -> {
            try {
                List<WaterEntry> entries = repository.getWaterEntriesByDate(today);
                int total = 0;
                if (entries != null) {
                    for (WaterEntry entry : entries) {
                        total += entry.getCantidadMl();
                    }
                }

                int goal = getDailyGoalMl();
                int progress = goal > 0 ? (total * 100 / goal) : 0;
                if (progress > 100) progress = 100;

                int remaining = Math.max(0, goal - total);

                _progressPercent.postValue(progress);
                _consumedStr.postValue(total + " ml");
                _dailyGoalStr.postValue(goal + " ml");
                _remainingStr.postValue(remaining + " ml");

                // Load full historial for home screen
                List<WaterEntry> allEntries = repository.getAllWaterEntries();
                _historial.postValue(allEntries != null ? allEntries : new ArrayList<>());

            } catch (Exception e) {
                _error.postValue("Error cargando datos: " + e.getMessage());
            } finally {
                _loading.postValue(false);
            }
        });
    }

    public void addWater(int cantidadMl) {
        String today = dateFormat.format(Calendar.getInstance().getTime());
        String time = timeFormat.format(Calendar.getInstance().getTime());

        WaterEntry entry = new WaterEntry();
        entry.setCantidadMl(cantidadMl);
        entry.setFecha(today);
        entry.setHora(time);
        entry.setTimestamp(System.currentTimeMillis());
        entry.setTipoVaso(0); // personalizado

        executor.execute(() -> {
            try {
                repository.insertWater(entry);
                loadTodayData(); // Refresh
            } catch (Exception e) {
                _error.postValue("Error agregando agua: " + e.getMessage());
            }
        });
    }

    public void addQuickWater(int cantidadMl) {
        addWater(cantidadMl);
    }

    public void navigateToConfigurarMeta() {
        // Navigation handled by fragment
    }

    public void navigateToHistorial() {
        // Navigation handled by fragment
    }

    public int getDailyGoalMl() {
        return preferences.getInt("goal_ml", 2500);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}