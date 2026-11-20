package com.lupefit.core.domain.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Registro de hidratación (agua).
 */
@Entity(
    tableName = "water_logs",
    foreignKeys = @ForeignKey(
        entity = User.class,
        parentColumns = "id",
        childColumns = "userId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {
        @Index(value = {"userId", "timestamp"}),
        @Index(value = {"isSynced"}),
        @Index(value = {"serverId"})
    }
)
public class WaterLog {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public long userId;

    @NonNull
    public long timestamp;

    public int quantityMl;

    @NonNull
    public String source; // "VASO", "BOTELLA", "PERSONALIZADO", "QUICK_ADD"

    // Offline-first sync fields
    public boolean isSynced = false;
    @NonNull
    public String syncAction = "CREATE";
    public long lastModified = System.currentTimeMillis();
    public String serverId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WaterLog)) return false;
        WaterLog that = (WaterLog) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}