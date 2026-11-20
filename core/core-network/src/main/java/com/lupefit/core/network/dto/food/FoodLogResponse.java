package com.lupefit.core.network.dto.food;

import com.lupefit.core.network.dto.common.MacroBreakdown;
import com.squareup.moshi.Json;

/** Respuesta de food log */
public final class FoodLogResponse {
    @Json(name = "id")
    public final String id;

    @Json(name = "user_id")
    public final String userId;

    @Json(name = "food_id")
    public final String foodId;

    @Json(name = "food_name")
    public final String foodName;

    @Json(name = "quantity")
    public final double quantity;

    @Json(name = "unit")
    public final String unit;

    @Json(name = "meal_type")
    public final String mealType; // "BREAKFAST" | "LUNCH" | "DINNER" | "SNACK"

    @Json(name = "timestamp")
    public final String timestamp; // ISO 8601

    @Json(name = "macros")
    public final MacroBreakdown macros;

    @Json(name = "calories")
    public final int calories;

    @Json(name = "notes")
    public final String notes;

    @Json(name = "is_synced")
    public final boolean isSynced;

    @Json(name = "created_at")
    public final String createdAt;

    @Json(name = "updated_at")
    public final String updatedAt;

    public FoodLogResponse(String id, String userId, String foodId, String foodName,
                          double quantity, String unit, String mealType, String timestamp,
                          MacroBreakdown macros, int calories, String notes,
                          boolean isSynced, String createdAt, String updatedAt) {
        this.id = id;
        this.userId = userId;
        this.foodId = foodId;
        this.foodName = foodName;
        this.quantity = quantity;
        this.unit = unit;
        this.mealType = mealType;
        this.timestamp = timestamp;
        this.macros = macros;
        this.calories = calories;
        this.notes = notes;
        this.isSynced = isSynced;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}