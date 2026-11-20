package com.lupefit.app.data.local.entity;

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
        @Index(value = {"isPublic"}),
        @Index(value = {"collectionId"})
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
    public String instructionsJson;

    // Macros por porción
    public int caloriesPerServing;
    public double proteinPerServing;
    public double carbsPerServing;
    public double fatPerServing;
    public double fiberPerServing;

    public String imageUrl;
    public boolean isPublic = false;
    public String collectionId;
    public String tagsJson;

    // Offline-first
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