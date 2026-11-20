package com.lupefit.core.network.dto.sync;

import com.lupefit.core.network.dto.food.FoodLogResponse;
import com.lupefit.core.network.dto.workout.WorkoutLogResponse;
import com.lupefit.core.network.dto.progress.WeightEntryResponse;
import com.lupefit.core.network.dto.progress.MeasurementResponse;
import com.lupefit.core.network.dto.progress.ProgressPhotoResponse;
import com.lupefit.core.network.dto.recipe.RecipeResponse;
import com.lupefit.core.network.dto.water.WaterLogResponse;
import com.lupefit.core.network.dto.exercise.ExerciseResponse;
import com.lupefit.core.network.dto.pr.PersonalRecordResponse;
import com.lupefit.core.network.dto.glucose.GlucoseLogResponse;
import com.lupefit.core.network.dto.mood.MoodEntryResponse;
import com.lupefit.core.network.dto.note.UserNoteResponse;
import com.squareup.moshi.Json;

import java.util.List;

/** Respuesta de sync batch */
public final class SyncBatchResponse {
    @Json(name = "server_version")
    public final int serverVersion;

    @Json(name = "sync_timestamp")
    public final String syncTimestamp;

    @Json(name = "conflicts")
    public final List<SyncConflict> conflicts;

    @Json(name = "food_logs")
    public final List<FoodLogResponse> foodLogs;

    @Json(name = "workout_logs")
    public final List<WorkoutLogResponse> workoutLogs;

    @Json(name = "weight_entries")
    public final List<WeightEntryResponse> weightEntries;

    @Json(name = "measurements")
    public final List<MeasurementResponse> measurements;

    @Json(name = "progress_photos")
    public final List<ProgressPhotoResponse> progressPhotos;

    @Json(name = "recipes")
    public final List<RecipeResponse> recipes;

    @Json(name = "water_logs")
    public final List<WaterLogResponse> waterLogs;

    @Json(name = "exercises")
    public final List<ExerciseResponse> exercises;

    @Json(name = "personal_records")
    public final List<PersonalRecordResponse> personalRecords;

    @Json(name = "glucose_logs")
    public final List<GlucoseLogResponse> glucoseLogs;

    @Json(name = "mood_entries")
    public final List<MoodEntryResponse> moodEntries;

    @Json(name = "user_notes")
    public final List<UserNoteResponse> userNotes;

    public SyncBatchResponse(int serverVersion, String syncTimestamp,
                            List<SyncConflict> conflicts,
                            List<FoodLogResponse> foodLogs,
                            List<WorkoutLogResponse> workoutLogs,
                            List<WeightEntryResponse> weightEntries,
                            List<MeasurementResponse> measurements,
                            List<ProgressPhotoResponse> progressPhotos,
                            List<RecipeResponse> recipes,
                            List<WaterLogResponse> waterLogs,
                            List<ExerciseResponse> exercises,
                            List<PersonalRecordResponse> personalRecords,
                            List<GlucoseLogResponse> glucoseLogs,
                            List<MoodEntryResponse> moodEntries,
                            List<UserNoteResponse> userNotes) {
        this.serverVersion = serverVersion;
        this.syncTimestamp = syncTimestamp;
        this.conflicts = conflicts;
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