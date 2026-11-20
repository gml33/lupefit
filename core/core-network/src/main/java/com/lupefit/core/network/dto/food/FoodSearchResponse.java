package com.lupefit.core.network.dto.food;

import com.lupefit.core.network.dto.common.MacroBreakdown;
import com.squareup.moshi.Json;

/** Respuesta de búsqueda de alimentos */
public final class FoodSearchResponse {
    @Json(name = "id")
    public final String id;

    @Json(name = "name")
    public final String name;

    @Json(name = "brand")
    public final String brand;

    @Json(name = "category")
    public final String category;

    @Json(name = "serving_size")
    public final double servingSize;

    @Json(name = "serving_unit")
    public final String servingUnit;

    @Json(name = "macros_per_serving")
    public final MacroBreakdown macrosPerServing;

    @Json(name = "calories_per_serving")
    public final int caloriesPerServing;

    @Json(name = "is_verified")
    public final boolean isVerified;

    public FoodSearchResponse(String id, String name, String brand,
                             String category, double servingSize,
                             String servingUnit, MacroBreakdown macrosPerServing,
                             int caloriesPerServing, boolean isVerified) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.servingSize = servingSize;
        this.servingUnit = servingUnit;
        this.macrosPerServing = macrosPerServing;
        this.caloriesPerServing = caloriesPerServing;
        this.isVerified = isVerified;
    }
}