package com.lupefit.core.network.dto.exercise;

import com.squareup.moshi.Json;

/** Request para crear/actualizar ejercicio */
public final class ExerciseRequest {
    @Json(name = "name")
    public final String name;

    @Json(name = "category")
    public final String category;

    @Json(name = "movement_pattern")
    public final String movementPattern;

    @Json(name = "equipment")
    public final String equipment;

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

    public ExerciseRequest(String name, String category, String movementPattern,
                          String equipment, boolean isBodyweight, boolean isCustom,
                          double defaultIncrementKg, String description,
                          String videoUrl, String imageUrl) {
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
    }
}