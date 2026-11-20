package com.lupefit.core.network.dto.water;

import com.squareup.moshi.Json;

/** Respuesta de log de agua */
public final class WaterLogResponse {
    @Json(name = "id")
    public final String id;

    @Json(name = "user_id")
    public final String userId;

    @Json(name = "amount_ml")
    public final int amountMl;

    @Json(name = "type")
    public final String type; // "WATER" | "COFFEE" | "TEA" | "OTHER"

    @Json(name = "timestamp")
    public final String timestamp;

    @Json(name = "is_synced")
    public final boolean isSynced;

    @Json(name = "created_at")
    public final String createdAt;

    @Json(name = "updated_at")
    public final String updatedAt;

    public WaterLogResponse(String id, String userId, int amountMl,
                           String type, String timestamp, boolean isSynced,
                           String createdAt, String updatedAt) {
        this.id = id;
        this.userId = userId;
        this.amountMl = amountMl;
        this.type = type;
        this.timestamp = timestamp;
        this.isSynced = isSynced;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}