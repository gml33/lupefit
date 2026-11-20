package com.lupefit.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Ejercicio maestro para PRs, workouts y cálculos.
 */
@Entity(
    tableName = "exercises",
    indices = {
        @Index(value = {"name"}),
        @Index(value = {"category"}),
        @Index(value = {"isCustom"}),
        @Index(value = {"serverId"})
    }
)
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String name;

    @NonNull
    public String category; // "FUERZA", "OLIMPICO", "GYMNASTICS", "CARDIO", "STRONGMAN", "CROSSFIT"

    @NonNull
    public String movementPattern; // "SQUAT", "HINGE", "PUSH_HORIZONTAL", "PUSH_VERTICAL", "PULL_HORIZONTAL", "PULL_VERTICAL", "CARRY", "CORE", "LOCOMOTION"

    @NonNull
    public String equipment; // "BARRA", "MANCUERNAS", "KETTLEBELL", "MAQUINA", "CUERPO_LIBRE", "ANILLAS", "CUERDA", "CAJON", "MEDICINA"

    public boolean isBodyweight = false;
    public boolean isCustom = false;
    public long createdByUserId = 0;

    // Para cálculos 1RM
    public double defaultIncrementKg = 2.5;

    // Metadatos
    public String description;
    public String videoUrl;
    public String imageUrl;
    public String serverId;

    // Offline-first
    public boolean isSynced = false;
    @NonNull
    public String syncAction = "CREATE";
    public long lastModified = System.currentTimeMillis();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exercise)) return false;
        Exercise that = (Exercise) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}