package com.lupefit.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Personal Record (PR) para ejercicios.
 * Tipos: ONE_RM, REP_MAX, VOLUME, TIME, DISTANCE.
 */
@Entity(
    tableName = "personal_records",
    foreignKeys = {
        @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Exercise.class, parentColumns = "id", childColumns = "exerciseId", onDelete = ForeignKey.CASCADE)
    },
    indices = {
        @Index(value = {"userId", "exerciseId"}),
        @Index(value = {"userId", "recordType"}),
        @Index(value = {"achievedAt"}),
        @Index(value = {"isSynced"}),
        @Index(value = {"serverId"})
    }
)
public class PersonalRecordEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public long userId;

    @NonNull
    public long exerciseId;

    @NonNull
    public String recordType; // "ONE_RM", "REP_MAX", "VOLUME", "TIME", "DISTANCE", "RPE"

    // 1RM / Rep Max
    public double weightKg;
    public int reps;

    // Volumen
    public int sets;
    public double totalVolumeKg;

    // Tiempo/Distancia
    public int durationSeconds;
    public double distanceKm;
    public double paceMinPerKm;

    public int rpe;
    @NonNull
    public long achievedAt;

    public String notes;
    public long workoutLogId;

    // Offline-first
    public boolean isSynced = false;
    @NonNull
    public String syncAction = "CREATE";
    public long lastModified = System.currentTimeMillis();
    public String serverId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonalRecordEntity)) return false;
        PersonalRecordEntity that = (PersonalRecordEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}