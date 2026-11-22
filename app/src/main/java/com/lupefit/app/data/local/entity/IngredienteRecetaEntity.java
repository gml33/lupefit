package com.lupefit.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Ingrediente de receta entity para Room database.
 */
@Entity(
    tableName = "receta_ingredientes",
    indices = {
        @Index(value = {"recetaId"}),
        @Index(value = {"alimentoId"}),
        @Index(value = {"serverId"})
    }
)
public class IngredienteRecetaEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long recetaId;

    public long alimentoId;

    @NonNull
    public String alimentoNombre;

    public float cantidad;

    @NonNull
    public String unidad;

    public String nota;

    public int orden;

    // Sync fields
    public boolean isSynced = false;
    @NonNull
    public String syncAction = "CREATE";
    public String serverId;
    public long lastModified = System.currentTimeMillis();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IngredienteRecetaEntity)) return false;
        IngredienteRecetaEntity that = (IngredienteRecetaEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}