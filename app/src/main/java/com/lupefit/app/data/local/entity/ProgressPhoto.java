package com.lupefit.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Fotos de progreso (frente, lado, espejo).
 */
@Entity(
    tableName = "progress_photos",
    foreignKeys = @ForeignKey(
        entity = User.class,
        parentColumns = "id",
        childColumns = "userId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {
        @Index(value = {"userId", "timestamp"}),
        @Index(value = {"isSynced"}),
        @Index(value = {"serverId"}),
        @Index(value = {"dateKey"}),
        @Index(value = {"photoType"})
    }
)
public class ProgressPhoto {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public long userId;

    @NonNull
    public long timestamp;

    @NonNull
    public int dateKey;

    @NonNull
    public String localUri;

    public String serverUrl;

    @NonNull
    public String photoType; // "FRENTE", "LADO", "ESPEJO", "OTRO"

    public double weightKg;
    public double bodyFatPercent;

    // Offline-first
    public boolean isSynced = false;
    @NonNull
    public String syncAction = "CREATE";
    public long lastModified = System.currentTimeMillis();
    public String serverId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProgressPhoto)) return false;
        ProgressPhoto that = (ProgressPhoto) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}