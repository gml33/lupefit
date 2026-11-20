package com.lupefit.core.network.dto.recipe;

import com.lupefit.core.network.dto.common.MacroBreakdown;
import com.squareup.moshi.Json;

/** Respuesta de ingrediente de receta */
public final class RecipeIngredientResponse {
    @Json(name = "id")
    public final String id;

    @Json(name = "recipe_id")
    public final String recipeId;

    @Json(name = "food_id")
    public final String foodId;

    @Json(name = "food_name")
    public final String foodName;

    @Json(name = "quantity")
    public final double quantity;

    @Json(name = "unit")
    public final String unit;

    @Json(name = "order_index")
    public final int orderIndex;

    @Json(name = "macros")
    public final MacroBreakdown macros;

    public RecipeIngredientResponse(String id, String recipeId, String foodId,
                                   String foodName, double quantity, String unit,
                                   int orderIndex, MacroBreakdown macros) {
        this.id = id;
        this.recipeId = recipeId;
        this.foodId = foodId;
        this.foodName = foodName;
        this.quantity = quantity;
        this.unit = unit;
        this.orderIndex = orderIndex;
        this.macros = macros;
    }
}