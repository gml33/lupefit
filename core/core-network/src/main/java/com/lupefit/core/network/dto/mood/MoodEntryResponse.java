package com.lupefit.core.network.dto.mood;

import com.squareup.moshi.Json;

/** Respuesta de entrada de mood */
public final class MoodEntryResponse {
    @Json(name = "id")
    public final String id;

    @Json(name = "user_id")
    public final String userId;

    @Json(name = "level")
    public final int level; // 1-5 (Muy bajo → Excelente)

    @Json(name = "emoji")
    public final String emoji;

    @Json(name = "note")
    public final String note;

    @Json(name = "timestamp")
    public final String timestamp;

    @Json(name = "is_synced")
    public final boolean isSynced;

    @Json(name = "created_at")
    public final String createdAt;

    @Json(name = "updated_at")
    public final String updatedAt;

    public MoodEntryResponse(String id, String userId, int level,
                            String emoji, String note, String timestamp,
                            boolean isSynced, String createdAt, String updatedAt) {
        this.id = id;
        this.userId = userId;
        this.level = level;
        this.emoji = emoji;
        this.note = note;
        this.timestamp = timestamp;
        this.isSynced = isSynced;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}