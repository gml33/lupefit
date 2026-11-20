package com.lupefit.core.network.dto.common;

import com.squareup.moshi.Json;

/** Respuesta paginada genérica */
public final class PaginatedResponse<T> {
    @Json(name = "items")
    public final java.util.List<T> items;

    @Json(name = "total")
    public final long total;

    @Json(name = "page")
    public final int page;

    @Json(name = "size")
    public final int size;

    @Json(name = "pages")
    public final int pages;

    public PaginatedResponse(java.util.List<T> items, long total,
                            int page, int size, int pages) {
        this.items = items;
        this.total = total;
        this.page = page;
        this.size = size;
        this.pages = pages;
    }
}