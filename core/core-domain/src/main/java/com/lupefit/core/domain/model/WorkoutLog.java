package com.lupefit.core.domain.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Registro de entrenamiento (cardio, fuerza, CrossFit/WOD).
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
        @Index(value = {"serverId"})
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
    public String workoutType; // "CARDIO", "FUERZA", "CROSSFIT_AMRAP", "CROSSFIT_EMOM", "CROSSFIT_FOR_TIME", "CROSSFIT_TABATA"

    @NonNull
    public String name; // nombre del ejercicio/WOD

    public int durationSeconds; // duración total
    public int caloriesBurned;

    // Cardio
    public double distanceKm;
    public double avgHeartRate;

    // Fuerza
    public int sets;
    public int reps;
    public double weightKg;
    public int rpe; // Rate of Perceived Exertion 1-10

    // CrossFit/WOD
    public String wodFormat; // "AMRAP", "EMOM", "FOR_TIME", "TABATA"
    public int roundsCompleted;
    public String movementsJson; // JSON array de movimientos con pesos/reps

    // Offline-first sync fields
    public boolean isSynced = false;
    @NonNull
    public String syncAction = "CREATE";
    public long lastModified = System.currentTimeMillis();
    public String serverId;

    // Metadatos
    public String notes;
    public boolean fromHealthConnect = false;
    public String healthConnectSessionId;

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