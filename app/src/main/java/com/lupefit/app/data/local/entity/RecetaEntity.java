package com.lupefit.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Receta entity para Room database.
 * Incluye ingredientes serializados como JSON para simplicidad.
 */
@Entity(
    tableName = "recetas",
    indices = {
        @Index(value = {"nombre"}),
        @Index(value = {"categoria"}),
        @Index(value = {"userId"}),
        @Index(value = {"esFavorita"}),
        @Index(value = {"serverId"})
    }
)
public class RecetaEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String nombre;

    public String descripcion;

    @NonNull
    public String categoria;

    public int tiempoPrepMin;
    public int tiempoCoccionMin;
    public int porciones;

    @NonNull
    public String dificultad;

    public String instrucciones;
    public String fotoUri;

    // Ingredientes serializados como JSON
    public String ingredientesJson;

    // Nutrición calculada (por porción)
    public int caloriasPorPorcion;
    public float proteinasPorPorcion;
    public float carbohidratosPorPorcion;
    public float grasasPorPorcion;
    public float fibraPorPorcion;

    public boolean esFavorita = false;
    public boolean esPublica = false;
    public long userId;

    // Sync fields
    public boolean isSynced = false;
    @NonNull
    public String syncAction = "CREATE";
    public String serverId;
    public long timestamp = System.currentTimeMillis();
    public long lastModified = System.currentTimeMillis();

    // Helper methods for JSON serialization
    private static final Gson gson = new Gson();
    private static final Type INGREDIENTES_TYPE = new TypeToken<ArrayList<IngredienteRecetaEntity>>(){}.getType();

    public List<IngredienteRecetaEntity> getIngredientes() {
        if (ingredientesJson == null || ingredientesJson.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return gson.fromJson(ingredientesJson, INGREDIENTES_TYPE);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void setIngredientes(List<IngredienteRecetaEntity> ingredientes) {
        this.ingredientesJson = gson.toJson(ingredientes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecetaEntity)) return false;
        RecetaEntity that = (RecetaEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}