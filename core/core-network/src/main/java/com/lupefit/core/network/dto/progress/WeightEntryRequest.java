package com.lupefit.core.network.dto.progress;

import com.squareup.moshi.Json;

/** Request para peso */
public final class WeightEntryRequest {
    @Json(name = "weight_kg")
    public final double weightKg;

    @Json(name = "body_fat_pct")
    public final Double bodyFatPct;

    @Json(name = "muscle_mass_kg")
    public final Double muscleMassKg;

    @Json(name = "date")
    public final String date;

    @Json(name = "notes")
    public final String notes;

    public WeightEntryRequest(double weightKg, Double bodyFatPct,
                             Double muscleMassKg, String date, String notes) {
        this.weightKg = weightKg;
        this.bodyFatPct = bodyFatPct;
        this.muscleMassKg = muscleMassKg;
        this.date = date;
        this.notes = notes;
    }
}