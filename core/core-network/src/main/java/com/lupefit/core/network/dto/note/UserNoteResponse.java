package com.lupefit.core.network.dto.note;

import com.squareup.moshi.Json;

/** Respuesta de nota de usuario */
public final class UserNoteResponse {
    @Json(name = "id")
    public final String id;

    @Json(name = "user_id")
    public final String userId;

    @Json(name = "title")
    public final String title;

    @Json(name = "content")
    public final String content;

    @Json(name = "tags")
    public final java.util.List<String> tags;

    @Json(name = "timestamp")
    public final String timestamp;

    @Json(name = "is_synced")
    public final boolean isSynced;

    @Json(name = "created_at")
    public final String createdAt;

    @Json(name = "updated_at")
    public final String updatedAt;

    public UserNoteResponse(String id, String userId, String title,
                           String content, java.util.List<String> tags,
                           String timestamp, boolean isSynced,
                           String createdAt, String updatedAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.timestamp = timestamp;
        this.isSynced = isSynced;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}