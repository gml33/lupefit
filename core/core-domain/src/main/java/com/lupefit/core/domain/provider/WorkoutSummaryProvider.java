package com.lupefit.core.domain.provider;
import androidx.lifecycle.LiveData;
import com.lupefit.core.domain.model.WorkoutSummary;
import java.util.List;
public interface WorkoutSummaryProvider { LiveData<List<WorkoutSummary>> getRecentWorkoutSummaries(long userId, int limit); }
