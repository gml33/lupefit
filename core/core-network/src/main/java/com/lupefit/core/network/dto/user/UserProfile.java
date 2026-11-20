package com.lupefit.core.network.dto.user;

import com.squareup.moshi.Json;

/** Perfil de usuario */
public final class UserProfile {
    @Json(name = "id")
    public final String id;

    @Json(name = "email")
    public final String email;

    @Json(name = "full_name")
    public final String fullName;

    @Json(name = "avatar_url")
    public final String avatarUrl;

    @Json(name = "birth_date")
    public final String birthDate;

    @Json(name = "gender")
    public final String gender;

    @Json(name = "height_cm")
    public final double heightCm;

    @Json(name = "weight_kg")
    public final double weightKg;

    @Json(name = "activity_level")
    public final String activityLevel;

    @Json(name = "goal")
    public final String goal;

    @Json(name = "daily_calorie_target")
    public final int dailyCalorieTarget;

    @Json(name = "macro_targets")
    public final MacroTargets macroTargets;

    @Json(name = "water_target_ml")
    public final int waterTargetMl;

    @Json(name = "created_at")
    public final String createdAt;

    @Json(name = "updated_at")
    public final String updatedAt;

    public UserProfile(String id, String email, String fullName, String avatarUrl,
                      String birthDate, String gender, double heightCm, double weightKg,
                      String activityLevel, String goal, int dailyCalorieTarget,
                      MacroTargets macroTargets, int waterTargetMl,
                      String createdAt, String updatedAt) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.birthDate = birthDate;
        this.gender = gender;
        this.heightCm = heightCm;
        this.weightKg = weightKg;
        this.activityLevel = activityLevel;
        this.goal = goal;
        this.dailyCalorieTarget = dailyCalorieTarget;
        this.macroTargets = macroTargets;
        this.waterTargetMl = waterTargetMl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}