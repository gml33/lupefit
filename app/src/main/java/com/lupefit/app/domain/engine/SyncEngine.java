package com.lupefit.app.domain.engine;

import android.content.Context;
import com.lupefit.app.domain.repository.FoodLogRepository;
import com.lupefit.app.domain.repository.WorkoutLogRepository;
import com.lupefit.app.domain.repository.PersonalRecordRepository;
import com.lupefit.app.domain.repository.GlucoseLogRepository;
import com.lupefit.app.domain.repository.MoodEntryRepository;
import com.lupefit.app.domain.repository.UserNoteRepository;
import com.lupefit.app.domain.repository.WeatherSnapshotRepository;
import com.lupefit.app.domain.repository.StreakRepository;
import com.lupefit.app.domain.repository.WaterLogRepository;
import com.lupefit.app.domain.repository.ProgressPhotoRepository;
import com.lupefit.app.domain.repository.RecipeRepository;

/** Motor de sincronización - stub para compilación base. */
public class SyncEngine {
    public SyncEngine(Context context,
                      FoodLogRepository foodLogRepository,
                      WorkoutLogRepository workoutLogRepository,
                      PersonalRecordRepository personalRecordRepository,
                      GlucoseLogRepository glucoseLogRepository,
                      MoodEntryRepository moodEntryRepository,
                      UserNoteRepository userNoteRepository,
                      WeatherSnapshotRepository weatherSnapshotRepository,
                      StreakRepository streakRepository,
                      WaterLogRepository waterLogRepository,
                      ProgressPhotoRepository progressPhotoRepository,
                      RecipeRepository recipeRepository) {}
}