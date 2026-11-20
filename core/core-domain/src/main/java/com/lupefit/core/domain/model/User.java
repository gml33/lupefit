package com.lupefit.core.domain.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Usuario local (auth offline-first).
 * La sincronización con servidor es opcional.
 */
@Entity(tableName = "users")
public class User {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String email;

    @NonNull
    public String passwordHash; // bcrypt

    @NonNull
    public String fullName;

    public String avatarUrl; // local path o remote URL

    // Perfil físico
    public long birthDate; // epoch millis
    @NonNull
    public String gender; // "MASCULINO", "FEMENINO", "OTRO"
    public int heightCm;
    public double currentWeightKg;
    public double targetWeightKg;
    @NonNull
    public String activityLevel; // "SEDENTARIO", "LIGERO", "MODERADO", "ACTIVO", "MUY_ACTIVO"

    // Metas nutricionales calculadas
    public int dailyCaloriesTarget;
    public double proteinTargetGrams;
    public double carbsTargetGrams;
    public double fatTargetGrams;
    public double waterTargetMl;

    // Configuración
    @NonNull
    public String unitsSystem = "METRIC"; // "METRIC", "IMPERIAL"
    @NonNull
    public String language = "es"; // "es", "en", "pt"
    @NonNull
    public String theme = "SYSTEM"; // "LIGHT", "DARK", "SYSTEM"

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