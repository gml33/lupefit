package com.lupefit.core.network.dto.sync;

import com.lupefit.core.network.dto.food.FoodLogRequest;
import com.lupefit.core.network.dto.workout.WorkoutLogRequest;
import com.lupefit.core.network.dto.progress.WeightEntryRequest;
import com.lupefit.core.network.dto.progress.MeasurementRequest;
import com.lupefit.core.network.dto.progress.ProgressPhotoRequest;
import com.lupefit.core.network.dto.recipe.RecipeRequest;
import com.lupefit.core.network.dto.water.WaterLogRequest;
import com.lupefit.core.network.dto.exercise.ExerciseRequest;
import com.lupefit.core.network.dto.pr.PersonalRecordRequest;
import com.lupefit.core.network.dto.glucose.GlucoseLogRequest;
import com.lupefit.core.network.dto.mood.MoodEntryRequest;
import com.lupefit.core.network.dto.note.UserNoteRequest;
import com.squareup.moshi.Json;

import java.util.List;

/** Request para sync batch bidireccional */
public final class SyncBatchRequest {
    @Json(name = "client_version")
    public final int clientVersion;

    @Json(name = "last_sync_timestamp")
    public final String lastSyncTimestamp;

    @Json(name = "food_logs")
    public final List<FoodLogRequest> foodLogs;

    @Json(name = "workout_logs")
    public final List<WorkoutLogRequest> workoutLogs;

    @Json(name = "weight_entries")
    public final List<WeightEntryRequest> weightEntries;

    @Json(name = "measurements")
    public final List<MeasurementRequest> measurements;

    @Json(name = "progress_photos")
    public final List<ProgressPhotoRequest> progressPhotos;

    @Json(name = "recipes")
    public final List<RecipeRequest> recipes;

    @Json(name = "water_logs")
    public final List<WaterLogRequest> waterLogs;

    @Json(name = "exercises")
    public final List<ExerciseRequest> exercises;

    @Json(name = "personal_records")
    public final List<PersonalRecordRequest> personalRecords;

    @Json(name = "glucose_logs")
    public final List<GlucoseLogRequest> glucoseLogs;

    @Json(name = "mood_entries")
    public final List<MoodEntryRequest> moodEntries;

    @Json(name = "user_notes")
    public final List<UserNoteRequest> userNotes;

    public SyncBatchRequest(int clientVersion, String lastSyncTimestamp,
                           List<FoodLogRequest> foodLogs,
                           List<WorkoutLogRequest> workoutLogs,
                           List<WeightEntryRequest> weightEntries,
                           List<MeasurementRequest> measurements,
                           List<ProgressPhotoRequest> progressPhotos,
                           List<RecipeRequest> recipes,
                           List<WaterLogRequest> waterLogs,
                           List<ExerciseRequest> exercises,
                           List<PersonalRecordRequest> personalRecords,
                           List<GlucoseLogRequest> glucoseLogs,
                           List<MoodEntryRequest> moodEntries,
                           List<UserNoteRequest> userNotes) {
        this.clientVersion = clientVersion;
        this.lastSyncTimestamp = lastSyncTimestamp;
        this.foodLogs = foodLogs;
        this.workoutLogs = workoutLogs;
        this.weightEntries = weightEntries;
        this.measurements = measurements;
        this.progressPhotos = progressPhotos;
        this.recipes = recipes;
        this.waterLogs = waterLogs;
        this.exercises = exercises;
        this.personalRecords = personalRecords;
        this.glucoseLogs = glucoseLogs;
        this.moodEntries = moodEntries;
        this.userNotes = userNotes;
    }
}