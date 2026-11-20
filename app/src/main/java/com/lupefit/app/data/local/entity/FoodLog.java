package com.lupefit.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Registro de alimento en el diario (Offline-First).
 * @see com.lupefit.app.data.local.dao.FoodLogDao
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
        @Index(value = {"serverId"}),
        @Index(value = {"dateKey"}) // YYYYMMDD para queries rápidas por día
    }
)
public class FoodLog {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public long userId;

    @NonNull
    public long timestamp; // epoch millis UTC

    @NonNull
    public int dateKey; // YYYYMMDD para queries por día sin conversión

    @NonNull
    public String foodName;

    @NonNull
    public String foodId; // local Food.id o serverId

    public double quantityGrams;

    public int calories;
    public double proteinGrams;
    public double carbsGrams;
    public double fatGrams;
    public double fiberGrams;
    public double sugarGrams;
    public double sodiumMg;

    @NonNull
    public String mealType; // "DESAYUNO", "ALMUERZO", "CENA", "MERIENDA", "OTRO"

    // Offline-first sync fields
    public boolean isSynced = false;
    @NonNull
    public String syncAction = "CREATE"; // CREATE, UPDATE, DELETE
    public long lastModified = System.currentTimeMillis();
    public String serverId;

    // Metadatos extra
    public String notes;
    public String barcode;
    public boolean fromVoiceInput = false;
    public boolean fromAIFoodScan = false;
    public String sourceType; // "MANUAL", "BARCODE", "AI_SCAN", "VOICE", "RECIPE", "QUICK_ADD"

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