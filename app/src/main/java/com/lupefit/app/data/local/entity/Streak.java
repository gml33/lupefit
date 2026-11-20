package com.lupefit.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Rachas (streaks) del usuario por tipo.
 * Actualizado diariamente por StreakManager.
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
        @Index(value = {"userId", "streakType"}, unique = true),
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
    public String streakType; // "CALORIES", "PROTEIN", "WORKOUT", "WATER", "LOGGING", "GLUCOSE"

    public int currentStreak = 0;
    public int longestStreak = 0;

    public int lastAchievedDate; // YYYYMMDD

    public long startedAt = System.currentTimeMillis();
    public long updatedAt = System.currentTimeMillis();

    // Config objetivo
    public double targetValue;
    @NonNull
    public String targetUnit; // "PERCENT", "COUNT", "MINUTES", "MG_DL"

    public int graceDays = 0; // días de gracia permitidos

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