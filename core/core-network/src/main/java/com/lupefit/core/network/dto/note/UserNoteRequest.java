package com.lupefit.core.network.dto.note;

import com.squareup.moshi.Json;

import java.util.List;

/** Request para nota de usuario */
public final class UserNoteRequest {
    @Json(name = "title")
    public final String title;

    @Json(name = "content")
    public final String content;

    @Json(name = "tags")
    public final List<String> tags;

    @Json(name = "timestamp")
    public final String timestamp;

    public UserNoteRequest(String title, String content, List<String> tags,
                          String timestamp) {
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.timestamp = timestamp;
    }
}