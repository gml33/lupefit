package com.lupefit.core.network.dto.workout;

import com.squareup.moshi.Json;

/** Respuesta de set de ejercicio */
public final class WorkoutSetResponse {
    @Json(name = "id")
    public final String id;

    @Json(name = "exercise_id")
    public final String exerciseId;

    @Json(name = "exercise_name")
    public final String exerciseName;

    @Json(name = "set_number")
    public final int setNumber;

    @Json(name = "reps")
    public final int reps;

    @Json(name = "weight_kg")
    public final double weightKg;

    @Json(name = "rpe")
    public final Integer rpe;

    @Json(name = "is_warmup")
    public final boolean isWarmup;

    @Json(name = "rest_seconds")
    public final int restSeconds;

    public WorkoutSetResponse(String id, String exerciseId, String exerciseName,
                             int setNumber, int reps, double weightKg,
                             Integer rpe, boolean isWarmup, int restSeconds) {
        this.id = id;
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.setNumber = setNumber;
        this.reps = reps;
        this.weightKg = weightKg;
        this.rpe = rpe;
        this.isWarmup = isWarmup;
        this.restSeconds = restSeconds;
    }
}