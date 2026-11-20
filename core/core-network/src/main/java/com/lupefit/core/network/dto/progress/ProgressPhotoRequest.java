package com.lupefit.core.network.dto.progress;

import com.squareup.moshi.Json;

/** Request para foto de progreso */
public final class ProgressPhotoRequest {
    @Json(name = "photo_base64")
    public final String photoBase64;

    @Json(name = "type")
    public final String type;

    @Json(name = "date")
    public final String date;

    @Json(name = "notes")
    public final String notes;

    public ProgressPhotoRequest(String photoBase64, String type,
                               String date, String notes) {
        this.photoBase64 = photoBase64;
        this.type = type;
        this.date = date;
        this.notes = notes;
    }
}