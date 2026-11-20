package com.lupefit.core.domain.model.pr;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Personal Record (PR) para un ejercicio específico.
 * Soporta 1RM, rep max, volumen, tiempo, distancia.
 */
@Entity(
    tableName = "personal_records",
    foreignKeys = {
        @ForeignKey(
            entity = com.lupefit.core.domain.model.User.class,
            parentColumns = "id",
            childColumns = "userId",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = com.lupefit.core.domain.model.Exercise.class,
            parentColumns = "id",
            childColumns = "exerciseId",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {
        @Index(value = {"userId", "exerciseId"}),
        @Index(value = {"userId", "recordType"}),
        @Index(value = {"achievedAt"}),
        @Index(value = {"isSynced"})
    }
)
public class PersonalRecord {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public long userId;

    @NonNull
    public long exerciseId; // referencia a Exercise local

    @NonNull
    public String recordType; // "ONE_RM", "REP_MAX", "VOLUME", "TIME", "DISTANCE", "RPE"

    // Para 1RM / Rep Max
    public double weightKg;
    public int reps;

    // Para volumen
    public int sets;
    public double totalVolumeKg; // sets * reps * weight

    // Para tiempo/distancia (cardio)
    public int durationSeconds;
    public double distanceKm;
    public double paceMinPerKm;

    // Común
    public int rpe; // 1-10 opcional
    @NonNull
    public long achievedAt; // epoch millis

    // Metadatos
    public String notes; // "Post WOD", "Ciclo fuerza semana 3", etc.
    public long workoutLogId; // referencia al WorkoutLog donde se logró

    // Offline-first sync
    public boolean isSynced = false;
    @NonNull
    public String syncAction = "CREATE";
    public long lastModified = System.currentTimeMillis();
    public String serverId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonalRecord)) return false;
        PersonalRecord that = (PersonalRecord) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}