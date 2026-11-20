package com.lupefit.core.domain.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Registro de estado de ánimo/energía (1-5).
 * Integrado en Diario y post-entrenamiento.
 */
@Entity(
    tableName = "mood_entries",
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
public class MoodEntry {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public long userId;

    @NonNull
    public long timestamp;

    public int moodLevel; // 1 (muy bajo) a 5 (excelente)
    @NonNull
    public String moodLabel; // "Muy bajo", "Bajo", "Neutro", "Bueno", "Excelente"

    @NonNull
    public String context; // "DIARIO", "POST_ENTRENO", "MANUAL"

    public long relatedWorkoutId; // si context == "POST_ENTRENO"
    public long relatedFoodLogId; // si context == "DIARIO"

    public String notes; // texto libre opcional

    // Offline-first sync fields
    public boolean isSynced = false;
    @NonNull
    public String syncAction = "CREATE";
    public long lastModified = System.currentTimeMillis();
    public String serverId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MoodEntry)) return false;
        MoodEntry that = (MoodEntry) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}