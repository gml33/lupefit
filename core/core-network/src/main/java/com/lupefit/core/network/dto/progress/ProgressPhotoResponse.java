package com.lupefit.core.network.dto.progress;

import com.squareup.moshi.Json;

/** Respuesta de foto de progreso */
public final class ProgressPhotoResponse {
    @Json(name = "id")
    public final String id;

    @Json(name = "user_id")
    public final String userId;

    @Json(name = "photo_url")
    public final String photoUrl;

    @Json(name = "thumbnail_url")
    public final String thumbnailUrl;

    @Json(name = "type")
    public final String type; // "FRONT" | "SIDE" | "BACK" | "MIRROR"

    @Json(name = "date")
    public final String date;

    @Json(name = "notes")
    public final String notes;

    @Json(name = "is_synced")
    public final boolean isSynced;

    @Json(name = "created_at")
    public final String createdAt;

    @Json(name = "updated_at")
    public final String updatedAt;

    public ProgressPhotoResponse(String id, String userId, String photoUrl,
                                String thumbnailUrl, String type, String date,
                                String notes, boolean isSynced,
                                String createdAt, String updatedAt) {
        this.id = id;
        this.userId = userId;
        this.photoUrl = photoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.type = type;
        this.date = date;
        this.notes = notes;
        this.isSynced = isSynced;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}