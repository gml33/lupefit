package com.lupefit.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Registro de peso del usuario para seguimiento de progreso.
 */
@Entity(
    tableName = "peso_entries",
    indices = {
        @Index(value = {"dateKey"}),
        @Index(value = {"timestamp"}),
        @Index(value = {"serverId"})
    }
)
public class PesoEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public float peso;

    @NonNull
    public String fecha;

    public String nota;

    public int dateKey;

    public long timestamp;

    public double diferencia;

    // Offline-first
    public boolean isSynced = false;
    @NonNull
    public String syncAction = "CREATE";
    public String serverId;
    public long lastModified = System.currentTimeMillis();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PesoEntity)) return false;
        PesoEntity that = (PesoEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}