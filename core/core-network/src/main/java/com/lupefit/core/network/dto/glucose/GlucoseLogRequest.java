package com.lupefit.core.network.dto.glucose;

import com.squareup.moshi.Json;

/** Request para log de glucosa */
public final class GlucoseLogRequest {
    @Json(name = "glucose_mg_dl")
    public final int glucoseMgDl;

    @Json(name = "type")
    public final String type;

    @Json(name = "meal_type")
    public final String mealType;

    @Json(name = "timestamp")
    public final String timestamp;

    @Json(name = "notes")
    public final String notes;

    public GlucoseLogRequest(int glucoseMgDl, String type, String mealType,
                            String timestamp, String notes) {
        this.glucoseMgDl = glucoseMgDl;
        this.type = type;
        this.mealType = mealType;
        this.timestamp = timestamp;
        this.notes = notes;
    }
}