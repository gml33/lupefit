package com.lupefit.core.domain.model.pr;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Ejercicio maestro para PRs y cálculos.
 * Incluye categoría, patrón de movimiento, equipamiento.
 */
@Entity(
    tableName = "exercises",
    indices = {
        @Index(value = {"name"}),
        @Index(value = {"category"}),
        @Index(value = {"isCustom"})
    }
)
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String name; // "Sentadilla trasera", "Press banca", "Peso muerto"

    @NonNull
    public String category; // "FUERZA", "OLIMPICO", "GYMNASTICS", "CARDIO", "STRONGMAN"

    @NonNull
    public String movementPattern; // "SQUAT", "HINGE", "PUSH_HORIZONTAL", "PUSH_VERTICAL", "PULL_HORIZONTAL", "PULL_VERTICAL", "CARRY", "CORE"

    @NonNull
    public String equipment; // "BARRA", "MANCUERNAS", "KETTLEBELL", "MAQUINA", "CUERPO_LIBRE", "ANILLAS", "CUERDA"

    public boolean isBodyweight = false;
    public boolean isCustom = false; // creado por usuario

    // Para cálculos de 1RM
    public double defaultIncrementKg = 2.5; // incremento estándar por lado

    // Metadatos
    public String description;
    public String videoUrl; // enlace a demo técnica
    public String serverId; // si viene de base oficial

    // Offline-first sync
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