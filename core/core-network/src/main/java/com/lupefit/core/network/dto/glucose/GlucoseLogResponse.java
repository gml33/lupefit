package com.lupefit.core.network.dto.glucose;

import com.squareup.moshi.Json;

/** Respuesta de log de glucosa */
public final class GlucoseLogResponse {
    @Json(name = "id")
    public final String id;

    @Json(name = "user_id")
    public final String userId;

    @Json(name = "glucose_mg_dl")
    public final int glucoseMgDl;

    @Json(name = "type")
    public final String type; // "FASTING" | "PRE_MEAL" | "POST_MEAL" | "BEDTIME" | "RANDOM"

    @Json(name = "meal_type")
    public final String mealType; // "BREAKFAST" | "LUNCH" | "DINNER" | "SNACK"

    @Json(name = "timestamp")
    public final String timestamp;

    @Json(name = "notes")
    public final String notes;

    @Json(name = "is_synced")
    public final boolean isSynced;

    @Json(name = "created_at")
    public final String createdAt;

    @Json(name = "updated_at")
    public final String updatedAt;

    public GlucoseLogResponse(String id, String userId, int glucoseMgDl,
                             String type, String mealType, String timestamp,
                             String notes, boolean isSynced,
                             String createdAt, String updatedAt) {
        this.id = id;
        this.userId = userId;
        this.glucoseMgDl = glucoseMgDl;
        this.type = type;
        this.mealType = mealType;
        this.timestamp = timestamp;
        this.notes = notes;
        this.isSynced = isSynced;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}