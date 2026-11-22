package com.lupefit.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Escaneo entry entity para Room database.
 * Registra tanto escaneos de código de barras como de comida (IA).
 */
@Entity(
    tableName = "escaneo_entries",
    indices = {
        @Index(value = {"tipo"}),
        @Index(value = {"codigoBarras"}),
        @Index(value = {"timestamp"}),
        @Index(value = {"serverId"})
    }
)
public class EscaneoEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String tipo; // "codigo_barras" | "comida_ia"

    @NonNull
    public String productoNombre;

    public String marca;

    public String codigoBarras;

    public String fotoUri;

    @NonNull
    public String fecha;

    public long timestamp;

    public boolean agregadoAlDiario = false;

    public long diarioEntryId = 0;

    // Nutrición (para código de barras)
    public int caloriasPor100g;
    public float proteinasPor100g;
    public float carbohidratosPor100g;
    public float grasasPor100g;

    // Para comida IA - items detectados serializados como JSON
    public String alimentosDetectadosJson;

    // Sync fields
    public boolean isSynced = false;
    @NonNull
    public String syncAction = "CREATE";
    public String serverId;
    public long lastModified = System.currentTimeMillis();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EscaneoEntity)) return false;
        EscaneoEntity that = (EscaneoEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}