package com.lupefit.core.network.dto.workout;

import com.squareup.moshi.Json;

/** Request para set de ejercicio */
public final class WorkoutSetRequest {
    @Json(name = "exercise_id")
    public final String exerciseId;

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

    public WorkoutSetRequest(String exerciseId, int setNumber, int reps,
                            double weightKg, Integer rpe, boolean isWarmup, int restSeconds) {
        this.exerciseId = exerciseId;
        this.setNumber = setNumber;
        this.reps = reps;
        this.weightKg = weightKg;
        this.rpe = rpe;
        this.isWarmup = isWarmup;
        this.restSeconds = restSeconds;
    }
}