package com.lupefit.core.network.dto.auth;

import com.squareup.moshi.Json;

/** Request para registro de usuario */
public final class RegisterRequest {
    @Json(name = "email")
    public final String email;

    @Json(name = "password")
    public final String password;

    @Json(name = "full_name")
    public final String fullName;

    @Json(name = "birth_date")
    public final String birthDate; // ISO 8601

    @Json(name = "gender")
    public final String gender; // "M" | "F" | "O"

    @Json(name = "height_cm")
    public final double heightCm;

    @Json(name = "weight_kg")
    public final double weightKg;

    @Json(name = "activity_level")
    public final String activityLevel; // "SEDENTARY" | "LIGHT" | "MODERATE" | "VERY_ACTIVE" | "EXTRA_ACTIVE"

    @Json(name = "goal")
    public final String goal; // "LOSE_WEIGHT" | "MAINTAIN" | "GAIN_MUSCLE"

    public RegisterRequest(String email, String password, String fullName,
                          String birthDate, String gender, double heightCm,
                          double weightKg, String activityLevel, String goal) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.heightCm = heightCm;
        this.weightKg = weightKg;
        this.activityLevel = activityLevel;
        this.goal = goal;
    }
}