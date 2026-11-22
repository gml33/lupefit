package com.lupefit.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Registro de consumo de agua para seguimiento de hidratación.
 */
@Entity(
    tableName = "water_entries",
    indices = {
        @Index(value = {"fecha"}),
        @Index(value = {"timestamp"}),
        @Index(value = {"serverId"})
    }
)
public class WaterEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public int cantidadMl;

    @NonNull
    public String fecha;

    @NonNull
    public String hora;

    public long timestamp;

    public int tipoVaso;

    // Sync fields
    public boolean isSynced = false;
    @NonNull
    public String syncAction = "CREATE";
    public String serverId;
    public long lastModified = System.currentTimeMillis();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WaterEntity)) return false;
        WaterEntity that = (WaterEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}