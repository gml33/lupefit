package com.lupefit.core.domain.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Cola de sincronización offline-first.
 * Cada operación pendiente se encola aquí y WorkManager la procesa.
 */
@Entity(
    tableName = "sync_queue",
    indices = {
        @Index(value = {"entityType", "entityId"}),
        @Index(value = {"priority", "nextRetryAt"}),
        @Index(value = {"attempts"})
    }
)
public class SyncQueueItem {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String entityType; // "FOOD_LOG", "WORKOUT_LOG", "MOOD_ENTRY", "USER_NOTE", "WATER_LOG", "PROGRESS_PHOTO", "RECIPE", "USER"

    @NonNull
    public long entityId; // ID local de la entidad

    @NonNull
    public String operation; // "CREATE", "UPDATE", "DELETE"

    @NonNull
    public String payloadJson; // JSON serializado de la entidad para enviar al servidor

    public int priority = 0; // mayor = más prioritario (ej. fotos = 1, logs = 0)

    public int attempts = 0;
    public int maxAttempts = 5;

    public long createdAt = System.currentTimeMillis();
    public long nextRetryAt = System.currentTimeMillis(); // backoff exponencial

    @NonNull
    public String status = "PENDING"; // "PENDING", "IN_PROGRESS", "SUCCESS", "FAILED", "CONFLICT"

    public String lastError; // mensaje de error del último intento
    public String serverResponse; // respuesta del servidor si hubo conflicto

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