package com.lupefit.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Base de datos local de alimentos (búsqueda offline).
 */
@Entity(
    tableName = "foods",
    indices = {
        @Index(value = {"name"}),
        @Index(value = {"barcode"}, unique = true),
        @Index(value = {"isVerified"}),
        @Index(value = {"userCreated"}),
        @Index(value = {"searchTokens"})
    }
)
public class Food {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String name;

    public String brand;
    public String barcode;

    // Info nutricional por 100g
    public int caloriesPer100g;
    public double proteinPer100g;
    public double carbsPer100g;
    public double fatPer100g;
    public double fiberPer100g;
    public double sugarPer100g;
    public double sodiumPer100g;

    public double servingSizeGrams;
    @NonNull
    public String servingDescription; // "100g", "1 taza", "1 unidad"

    public boolean isVerified = false;
    public boolean userCreated = false;
    public long createdByUserId;
    public String serverId;
    public String imageUrl;

    // Para búsqueda FTS (tokens normalizados)
    public String searchTokens;

    // Offline-first
    public boolean isSynced = false;
    @NonNull
    public String syncAction = "CREATE";
    public long lastModified = System.currentTimeMillis();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Food)) return false;
        Food food = (Food) o;
        return id == food.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}