package com.lupefit.core.sync.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Cola de sincronización - almacena operaciones pendientes de sincronizar con el backend.
 * Cada entrada representa una operación CREATE/UPDATE/DELETE en una entidad.
 */
@Entity(
    tableName = "sync_queue",
    indices = {
        @Index(value = {"entity_type", "entity_id"}, unique = true),
        @Index(value = {"status"}),
        @Index(value = {"retry_count"}),
        @Index(value = {"priority"})
    }
)
public final class SyncQueueItem {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @NonNull
    @ColumnInfo(name = "entity_type")
    public String entityType; // "FOOD_LOG", "WORKOUT_LOG", "WEIGHT_ENTRY", etc.

    @NonNull
    @ColumnInfo(name = "entity_id")
    public String entityId; // UUID de la entidad local

    @ColumnInfo(name = "server_id")
    public String serverId; // ID en servidor (null si CREATE pendiente)

    @NonNull
    @ColumnInfo(name = "action")
    public String action; // "CREATE", "UPDATE", "DELETE"

    @NonNull
    @ColumnInfo(name = "payload_json")
    public String payloadJson; // JSON serializado de la entidad

    @NonNull
    @ColumnInfo(name = "status")
    public String status; // "PENDING", "SYNCING", "SYNCED", "FAILED", "CONFLICT"

    @ColumnInfo(name = "priority")
    public int priority = 0; // Mayor prioridad = se sincroniza primero

    @ColumnInfo(name = "retry_count")
    public int retryCount = 0;

    @ColumnInfo(name = "created_at")
    public long createdAt = System.currentTimeMillis();

    @ColumnInfo(name = "updated_at")
    public long updatedAt = System.currentTimeMillis();

    @ColumnInfo(name = "next_retry_at")
    public long nextRetryAt = 0; // Timestamp para próximo reintento (backoff exponencial)

    @NonNull
    @ColumnInfo(name = "last_error")
    public String lastError; // Último error si FAILED

    @ColumnInfo(name = "conflict_resolution")
    public int conflictResolution; // 0=SERVER_WINS, 1=CLIENT_WINS, 2=MERGE

    @ColumnInfo(name = "merged_payload_json")
    public String mergedPayloadJson; // Payload mergeado si aplica

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SyncQueueItem)) return false;
        SyncQueueItem that = (SyncQueueItem) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}