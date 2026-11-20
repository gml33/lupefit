package com.lupefit.core.network.dto.exercise;

import com.squareup.moshi.Json;

/** Respuesta de ejercicio */
public final class ExerciseResponse {
    @Json(name = "id")
    public final String id;

    @Json(name = "name")
    public final String name;

    @Json(name = "category")
    public final String category; // "STRENGTH" | "OLYMPIC" | "GYMNASTICS" | "CARDIO" | "STRONGMAN" | "CROSSFIT"

    @Json(name = "movement_pattern")
    public final String movementPattern; // "SQUAT" | "HINGE" | "PUSH_H" | "PUSH_V" | "PULL_H" | "PULL_V" | "CARRY" | "CORE" | "LOCOMOTION"

    @Json(name = "equipment")
    public final String equipment; // "BARBELL" | "DUMBBELL" | "KETTLEBELL" | "MACHINE" | "BODYWEIGHT" | "RINGS" | "ROPE" | "BOX" | "MEDICINE_BALL"

    @Json(name = "is_bodyweight")
    public final boolean isBodyweight;

    @Json(name = "is_custom")
    public final boolean isCustom;

    @Json(name = "default_increment_kg")
    public final double defaultIncrementKg;

    @Json(name = "description")
    public final String description;

    @Json(name = "video_url")
    public final String videoUrl;

    @Json(name = "image_url")
    public final String imageUrl;

    @Json(name = "is_synced")
    public final boolean isSynced;

    @Json(name = "created_at")
    public final String createdAt;

    @Json(name = "updated_at")
    public final String updatedAt;

    public ExerciseResponse(String id, String name, String category,
                           String movementPattern, String equipment,
                           boolean isBodyweight, boolean isCustom,
                           double defaultIncrementKg, String description,
                           String videoUrl, String imageUrl,
                           boolean isSynced, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.movementPattern = movementPattern;
        this.equipment = equipment;
        this.isBodyweight = isBodyweight;
        this.isCustom = isCustom;
        this.defaultIncrementKg = defaultIncrementKg;
        this.description = description;
        this.videoUrl = videoUrl;
        this.imageUrl = imageUrl;
        this.isSynced = isSynced;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}