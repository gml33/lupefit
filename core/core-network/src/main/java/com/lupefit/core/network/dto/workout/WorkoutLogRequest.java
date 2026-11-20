package com.lupefit.core.network.dto.workout;

import com.squareup.moshi.Json;

import java.util.List;

/** Request para crear/actualizar workout log */
public final class WorkoutLogRequest {
    @Json(name = "name")
    public final String name;

    @Json(name = "type")
    public final String type;

    @Json(name = "started_at")
    public final String startedAt;

    @Json(name = "ended_at")
    public final String endedAt;

    @Json(name = "duration_seconds")
    public final int durationSeconds;

    @Json(name = "calories_burned")
    public final int caloriesBurned;

    @Json(name = "rpe")
    public final Integer rpe;

    @Json(name = "notes")
    public final String notes;

    @Json(name = "sets")
    public final List<WorkoutSetRequest> sets;

    public WorkoutLogRequest(String name, String type, String startedAt,
                            String endedAt, int durationSeconds, int caloriesBurned,
                            Integer rpe, String notes, List<WorkoutSetRequest> sets) {
        this.name = name;
        this.type = type;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.durationSeconds = durationSeconds;
        this.caloriesBurned = caloriesBurned;
        this.rpe = rpe;
        this.notes = notes;
        this.sets = sets;
    }
}