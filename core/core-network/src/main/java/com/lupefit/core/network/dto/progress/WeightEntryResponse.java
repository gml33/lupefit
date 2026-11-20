package com.lupefit.core.network.dto.progress;

import com.squareup.moshi.Json;

/** Respuesta de entrada de peso */
public final class WeightEntryResponse {
    @Json(name = "id")
    public final String id;

    @Json(name = "user_id")
    public final String userId;

    @Json(name = "weight_kg")
    public final double weightKg;

    @Json(name = "body_fat_pct")
    public final Double bodyFatPct;

    @Json(name = "muscle_mass_kg")
    public final Double muscleMassKg;

    @Json(name = "date")
    public final String date; // YYYY-MM-DD

    @Json(name = "notes")
    public final String notes;

    @Json(name = "is_synced")
    public final boolean isSynced;

    @Json(name = "created_at")
    public final String createdAt;

    @Json(name = "updated_at")
    public final String updatedAt;

    public WeightEntryResponse(String id, String userId, double weightKg,
                              Double bodyFatPct, Double muscleMassKg,
                              String date, String notes, boolean isSynced,
                              String createdAt, String updatedAt) {
        this.id = id;
        this.userId = userId;
        this.weightKg = weightKg;
        this.bodyFatPct = bodyFatPct;
        this.muscleMassKg = muscleMassKg;
        this.date = date;
        this.notes = notes;
        this.isSynced = isSynced;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}