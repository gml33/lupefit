package com.lupefit.core.domain.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Recetas de usuario.
 */
@Entity(
    tableName = "recipes",
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
        @Index(value = {"isPublic"})
    }
)
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public long userId;

    @NonNull
    public long timestamp;

    @NonNull
    public String name;

    public String description;
    public int servings;
    public int prepTimeMinutes;
    public int cookTimeMinutes;

    @NonNull
    public String instructionsJson; // JSON array de pasos

    // Macros totales por porción (calculados)
    public int caloriesPerServing;
    public double proteinPerServing;
    public double carbsPerServing;
    public double fatPerServing;
    public double fiberPerServing;

    // Metadatos
    public String imageUrl;
    public boolean isPublic = false;
    public String collectionId; // para agrupar en colecciones
    public String tagsJson; // JSON array de tags

    // Offline-first sync fields
    public boolean isSynced = false;
    @NonNull
    public String syncAction = "CREATE";
    public long lastModified = System.currentTimeMillis();
    public String serverId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recipe)) return false;
        Recipe that = (Recipe) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}