package com.lupefit.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Registro de entrenamiento (cardio, fuerza, CrossFit/WOD).
 * Offline-First con sync fields.
 */
@Entity(
    tableName = "workout_logs",
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
        @Index(value = {"dateKey"})
    }
)
public class WorkoutLog {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public long userId;

    @NonNull
    public long timestamp;

    @NonNull
    public int dateKey; // YYYYMMDD

    @NonNull
    public String workoutType; // "CARDIO", "FUERZA", "CROSSFIT_AMRAP", "CROSSFIT_EMOM", "CROSSFIT_FOR_TIME", "CROSSFIT_TABATA", "RUTINA"

    @NonNull
    public String name;

    public int durationSeconds;
    public int caloriesBurned;

    // Cardio
    public double distanceKm;
    public double avgHeartRate;
    public int maxHeartRate;

    // Fuerza
    public int sets;
    public int reps;
    public double weightKg;
    public int rpe;

    // CrossFit/WOD
    public String wodFormat; // "AMRAP", "EMOM", "FOR_TIME", "TABATA"
    public int roundsCompleted;
    public String movementsJson; // JSON array con detalle

    // Offline-first
    public boolean isSynced = false;
    @NonNull
    public String syncAction = "CREATE";
    public long lastModified = System.currentTimeMillis();
    public String serverId;

    // Metadatos
    public String notes;
    public boolean fromHealthConnect = false;
    public String healthConnectSessionId;
    public long personalRecordId; // si generó PR

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkoutLog)) return false;
        WorkoutLog that = (WorkoutLog) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}