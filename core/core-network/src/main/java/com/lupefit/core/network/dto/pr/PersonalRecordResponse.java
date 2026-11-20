package com.lupefit.core.network.dto.pr;

import com.squareup.moshi.Json;

/** Respuesta de Personal Record */
public final class PersonalRecordResponse {
    @Json(name = "id")
    public final String id;

    @Json(name = "user_id")
    public final String userId;

    @Json(name = "exercise_id")
    public final String exerciseId;

    @Json(name = "exercise_name")
    public final String exerciseName;

    @Json(name = "type")
    public final String type; // "ONE_REP_MAX" | "VOLUME" | "TIME" | "REPS" | "DISTANCE"

    @Json(name = "value")
    public final double value;

    @Json(name = "unit")
    public final String unit; // "KG" | "LBS" | "REPS" | "SECONDS" | "METERS"

    @Json(name = "date")
    public final String date;

    @Json(name = "workout_log_id")
    public final String workoutLogId;

    @Json(name = "notes")
    public final String notes;

    @Json(name = "is_synced")
    public final boolean isSynced;

    @Json(name = "created_at")
    public final String createdAt;

    @Json(name = "updated_at")
    public final String updatedAt;

    public PersonalRecordResponse(String id, String userId, String exerciseId,
                                 String exerciseName, String type, double value,
                                 String unit, String date, String workoutLogId,
                                 String notes, boolean isSynced,
                                 String createdAt, String updatedAt) {
        this.id = id;
        this.userId = userId;
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.type = type;
        this.value = value;
        this.unit = unit;
        this.date = date;
        this.workoutLogId = workoutLogId;
        this.notes = notes;
        this.isSynced = isSynced;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}