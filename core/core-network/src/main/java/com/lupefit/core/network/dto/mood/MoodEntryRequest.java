package com.lupefit.core.network.dto.mood;

import com.squareup.moshi.Json;

/** Request para mood entry */
public final class MoodEntryRequest {
    @Json(name = "level")
    public final int level;

    @Json(name = "emoji")
    public final String emoji;

    @Json(name = "note")
    public final String note;

    @Json(name = "timestamp")
    public final String timestamp;

    public MoodEntryRequest(int level, String emoji, String note, String timestamp) {
        this.level = level;
        this.emoji = emoji;
        this.note = note;
        this.timestamp = timestamp;
    }
}