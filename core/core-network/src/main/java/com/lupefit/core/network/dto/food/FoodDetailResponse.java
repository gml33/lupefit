package com.lupefit.core.network.dto.food;

import com.lupefit.core.network.dto.common.MacroBreakdown;
import com.squareup.moshi.Json;

/** Respuesta de detalle de alimento */
public final class FoodDetailResponse {
    @Json(name = "id")
    public final String id;

    @Json(name = "name")
    public final String name;

    @Json(name = "brand")
    public final String brand;

    @Json(name = "category")
    public final String category;

    @Json(name = "description")
    public final String description;

    @Json(name = "serving_size")
    public final double servingSize;

    @Json(name = "serving_unit")
    public final String servingUnit;

    @Json(name = "macros_per_serving")
    public final MacroBreakdown macrosPerServing;

    @Json(name = "calories_per_serving")
    public final int caloriesPerServing;

    @Json(name = "micronutrients")
    public final Micronutrients micronutrients;

    @Json(name = "is_verified")
    public final boolean isVerified;

    @Json(name = "barcode")
    public final String barcode;

    @Json(name = "image_url")
    public final String imageUrl;

    public FoodDetailResponse(String id, String name, String brand,
                             String category, String description,
                             double servingSize, String servingUnit,
                             MacroBreakdown macrosPerServing,
                             int caloriesPerServing,
                             Micronutrients micronutrients,
                             boolean isVerified, String barcode, String imageUrl) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.description = description;
        this.servingSize = servingSize;
        this.servingUnit = servingUnit;
        this.macrosPerServing = macrosPerServing;
        this.caloriesPerServing = caloriesPerServing;
        this.micronutrients = micronutrients;
        this.isVerified = isVerified;
        this.barcode = barcode;
        this.imageUrl = imageUrl;
    }
}