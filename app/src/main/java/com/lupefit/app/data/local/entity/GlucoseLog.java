package com.lupefit.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Registro de glucosa en sangre (mg/dL).
 * Para usuarios diabéticos o control metabólico.
 */
@Entity(
    tableName = "glucose_logs",
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
public class GlucoseLog {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public long userId;

    @NonNull
    public long timestamp;

    @NonNull
    public int dateKey;

    public int glucoseMgDl; // mg/dL

    @NonNull
    public String measurementType; // "AYUNAS", "PRE_COMIDA", "POST_COMIDA", "ANTES_DORMIR", "ALEATORIO", "DURANTE_EJERCICIO"

    public int mealContextFoodLogId; // referencia opcional a FoodLog

    public String notes;

    // Offline-first
    public boolean isSynced = false;
    @NonNull
    public String syncAction = "CREATE";
    public long lastModified = System.currentTimeMillis();
    public String serverId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GlucoseLog)) return false;
        GlucoseLog that = (GlucoseLog) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}