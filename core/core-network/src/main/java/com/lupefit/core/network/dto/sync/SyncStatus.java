package com.lupefit.core.network.dto.sync;

import com.squareup.moshi.Json;

/** Estado de sincronización */
public final class SyncStatus {
    @Json(name = "last_sync_timestamp")
    public final String lastSyncTimestamp;

    @Json(name = "pending_count")
    public final int pendingCount;

    @Json(name = "server_version")
    public final int serverVersion;

    @Json(name = "is_syncing")
    public final boolean isSyncing;

    @Json(name = "last_error")
    public final String lastError;

    public SyncStatus(String lastSyncTimestamp, int pendingCount,
                     int serverVersion, boolean isSyncing, String lastError) {
        this.lastSyncTimestamp = lastSyncTimestamp;
        this.pendingCount = pendingCount;
        this.serverVersion = serverVersion;
        this.isSyncing = isSyncing;
        this.lastError = lastError;
    }
}