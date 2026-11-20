package com.lupefit.core.network.dto.sync;

import com.squareup.moshi.Json;

/** Conflicto de sincronización */
public final class SyncConflict {
    @Json(name = "entity_type")
    public final String entityType;

    @Json(name = "entity_id")
    public final String entityId;

    @Json(name = "client_version")
    public final int clientVersion;

    @Json(name = "server_version")
    public final int serverVersion;

    @Json(name = "resolution")
    public final String resolution; // "SERVER_WINS" | "CLIENT_WINS" | "MERGE"

    @Json(name = "merged_data")
    public final Object mergedData; // Datos mergeados si aplica

    public SyncConflict(String entityType, String entityId,
                       int clientVersion, int serverVersion,
                       String resolution, Object mergedData) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.clientVersion = clientVersion;
        this.serverVersion = serverVersion;
        this.resolution = resolution;
        this.mergedData = mergedData;
    }
}