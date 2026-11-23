package com.lupefit.feature.ejercicio.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import com.lupefit.core.domain.model.WorkoutSummary;
import com.lupefit.core.domain.provider.WorkoutSummaryProvider;
import java.util.List;

public class EjercicioHomeViewModel extends ViewModel {
    public final LiveData<List<WorkoutSummary>> recentWorkouts;
    public EjercicioHomeViewModel(WorkoutSummaryProvider provider, long userId) { recentWorkouts = provider.getRecentWorkoutSummaries(userId, 10); }
}
