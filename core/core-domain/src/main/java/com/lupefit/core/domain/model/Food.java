package com.lupefit.core.domain.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Base de datos local de alimentos (para búsqueda offline).
 * Se popula desde servidor y permite alimentos creados por usuario.
 */
@Entity(
    tableName = "foods",
    indices = {
        @Index(value = {"name"}),
        @Index(value = {"barcode"}),
        @Index(value = {"isVerified"}),
        @Index(value = {"userCreated"})
    }
)
public class Food {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String name;

    public String brand;
    public String barcode; // EAN-13, UPC, etc.

    // Info nutricional por 100g
    public int caloriesPer100g;
    public double proteinPer100g;
    public double carbsPer100g;
    public double fatPer100g;
    public double fiberPer100g;
    public double sugarPer100g;
    public double sodiumPer100g;

    public double servingSizeGrams; // porción base (ej. 100g)
    @NonNull
    public String servingDescription; // "100g", "1 taza", "1 unidad"

    // Metadatos
    public boolean isVerified = false; // verificado por equipo nutricional
    public boolean userCreated = false; // creado por usuario local
    public long createdByUserId; // 0 si es base oficial
    public String serverId; // ID remoto si sincronizado
    public String imageUrl;

    // Para búsqueda FTS (se popula en trigger o app)
    public String searchTokens; // nombre + marca + tokens normalizados

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