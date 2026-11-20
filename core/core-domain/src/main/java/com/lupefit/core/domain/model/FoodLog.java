package com.lupefit.core.domain.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Registro de alimento en el diario diario.
 * Incluye campos de sincronización offline-first.
 */
@Entity(
    tableName = "food_logs",
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
public class FoodLog {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public long userId;

    @NonNull
    public long timestamp; // epoch millis

    @NonNull
    public String foodName;

    @NonNull
    public String foodId; // local food DB id or remote ID

    public double quantityGrams;

    public int calories;
    public double proteinGrams;
    public double carbsGrams;
    public double fatGrams;
    public double fiberGrams;

    @NonNull
    public String mealType; // "DESAYUNO", "ALMUERZO", "CENA", "MERIENDA", "OTRO"

    // Offline-first sync fields
    public boolean isSynced = false;
    @NonNull
    public String syncAction = "CREATE"; // CREATE, UPDATE, DELETE
    public long lastModified = System.currentTimeMillis();
    public String serverId; // ID asignado por servidor tras sync exitosa

    // Metadatos extra
    public String notes;
    public String barcode;
    public boolean fromVoiceInput = false;
    public boolean fromAIFoodScan = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FoodLog)) return false;
        FoodLog that = (FoodLog) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}