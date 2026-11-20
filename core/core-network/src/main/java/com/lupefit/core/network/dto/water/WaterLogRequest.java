package com.lupefit.core.network.dto.water;

import com.squareup.moshi.Json;

/** Request para log de agua */
public final class WaterLogRequest {
    @Json(name = "amount_ml")
    public final int amountMl;

    @Json(name = "type")
    public final String type;

    @Json(name = "timestamp")
    public final String timestamp;

    public WaterLogRequest(int amountMl, String type, String timestamp) {
        this.amountMl = amountMl;
        this.type = type;
        this.timestamp = timestamp;
    }
}