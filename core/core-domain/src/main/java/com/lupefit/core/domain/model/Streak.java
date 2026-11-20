package com.lupefit.core.domain.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Registro de rachas (streaks) del usuario.
 * Se actualiza diariamente por StreakManager.
 */
@Entity(
    tableName = "streaks",
    foreignKeys = @ForeignKey(
        entity = User.class,
        parentColumns = "id",
        childColumns = "userId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {
        @Index(value = {"userId", "streakType"}),
        @Index(value = {"currentStreak"}),
        @Index(value = {"lastAchievedDate"})
    }
)
public class Streak {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public long userId;

    @NonNull
    public String streakType; // "CALORIES", "PROTEIN", "WORKOUT", "WATER", "LOGGING"

    public int currentStreak = 0; // días consecutivos actuales
    public int longestStreak = 0; // récord histórico

    public long lastAchievedDate; // epoch days (yyyyMMdd) último día que cumplió

    public long startedAt = System.currentTimeMillis();
    public long updatedAt = System.currentTimeMillis();

    // Configuración del objetivo para esta racha
    public double targetValue; // ej. 85% de calorías, 1 entrenamiento, etc.
    @NonNull
    public String targetUnit; // "PERCENT", "COUNT", "MINUTES"

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Streak)) return false;
        Streak streak = (Streak) o;
        return id == streak.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}