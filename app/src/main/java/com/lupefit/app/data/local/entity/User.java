package com.lupefit.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Usuario local (auth offline-first).
 * Sync con servidor opcional.
 */
@Entity(tableName = "users", indices = {
    @Index(value = {"email"}, unique = true),
    @Index(value = {"serverId"}, unique = true),
    @Index(value = {"isSynced"})
})
public class User {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String email;

    @NonNull
    public String passwordHash; // bcrypt

    @NonNull
    public String fullName;

    public String avatarUrl;
    public String avatarLocalPath;

    // Perfil físico
    public long birthDate;
    @NonNull
    public String gender; // "MASCULINO", "FEMENINO", "OTRO"
    public int heightCm;
    public double currentWeightKg;
    public double targetWeightKg;
    @NonNull
    public String activityLevel; // "SEDENTARIO", "LIGERO", "MODERADO", "ACTIVO", "MUY_ACTIVO"

    // Metas calculadas
    public int dailyCaloriesTarget;
    public double proteinTargetGrams;
    public double carbsTargetGrams;
    public double fatTargetGrams;
    public double fiberTargetGrams;
    public double waterTargetMl;

    // Configuración
    @NonNull
    public String unitsSystem = "METRIC"; // "METRIC", "IMPERIAL"
    @NonNull
    public String language = "es"; // "es", "en", "pt"
    @NonNull
    public String theme = "SYSTEM"; // "LIGHT", "DARK", "SYSTEM"

    // Net Carbs mode
    public boolean netCarbsEnabled = false;

    // Health Connect
    public boolean healthConnectEnabled = false;
    public long healthConnectLastSyncAt;

    // Sync con servidor (opcional)
    public boolean hasCloudAccount = false;
    public String serverId;
    public String accessToken;
    public String refreshToken;
    public long tokenExpiresAt;
    public boolean isSynced = false;
    public long lastSyncAt;

    // Timestamps
    public long createdAt = System.currentTimeMillis();
    public long updatedAt = System.currentTimeMillis();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}