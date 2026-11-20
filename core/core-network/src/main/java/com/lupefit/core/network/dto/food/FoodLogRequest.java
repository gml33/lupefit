package com.lupefit.core.network.dto.food;

import com.lupefit.core.network.dto.common.MacroBreakdown;
import com.squareup.moshi.Json;

/** Request para crear/actualizar food log */
public final class FoodLogRequest {
    @Json(name = "food_id")
    public final String foodId;

    @Json(name = "quantity")
    public final double quantity;

    @Json(name = "unit")
    public final String unit;

    @Json(name = "meal_type")
    public final String mealType;

    @Json(name = "timestamp")
    public final String timestamp;

    @Json(name = "macros")
    public final MacroBreakdown macros;

    @Json(name = "calories")
    public final int calories;

    @Json(name = "notes")
    public final String notes;

    public FoodLogRequest(String foodId, double quantity, String unit,
                         String mealType, String timestamp,
                         MacroBreakdown macros, int calories, String notes) {
        this.foodId = foodId;
        this.quantity = quantity;
        this.unit = unit;
        this.mealType = mealType;
        this.timestamp = timestamp;
        this.macros = macros;
        this.calories = calories;
        this.notes = notes;
    }
}