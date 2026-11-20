package com.lupefit.core.network.dto.workout;

import com.lupefit.core.network.dto.common.MacroBreakdown;
import com.squareup.moshi.Json;

import java.util.List;

/** Respuesta de workout log */
public final class WorkoutLogResponse {
    @Json(name = "id")
    public final String id;

    @Json(name = "user_id")
    public final String userId;

    @Json(name = "name")
    public final String name;

    @Json(name = "type")
    public final String type; // "STRENGTH" | "CARDIO" | "WOD" | "FLEXIBILITY"

    @Json(name = "started_at")
    public final String startedAt;

    @Json(name = "ended_at")
    public final String endedAt;

    @Json(name = "duration_seconds")
    public final int durationSeconds;

    @Json(name = "calories_burned")
    public final int caloriesBurned;

    @Json(name = "rpe")
    public final Integer rpe; // 1-10

    @Json(name = "notes")
    public final String notes;

    @Json(name = "sets")
    public final List<WorkoutSetResponse> sets;

    @Json(name = "is_synced")
    public final boolean isSynced;

    @Json(name = "created_at")
    public final String createdAt;

    @Json(name = "updated_at")
    public final String updatedAt;

    public WorkoutLogResponse(String id, String userId, String name, String type,
                             String startedAt, String endedAt, int durationSeconds,
                             int caloriesBurned, Integer rpe, String notes,
                             List<WorkoutSetResponse> sets, boolean isSynced,
                             String createdAt, String updatedAt) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.durationSeconds = durationSeconds;
        this.caloriesBurned = caloriesBurned;
        this.rpe = rpe;
        this.notes = notes;
        this.sets = sets;
        this.isSynced = isSynced;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}