package com.lupefit.core.network.dto.recipe;

import com.lupefit.core.network.dto.common.MacroBreakdown;
import com.squareup.moshi.Json;

/** Request para ingrediente de receta */
public final class RecipeIngredientRequest {
    @Json(name = "food_id")
    public final String foodId;

    @Json(name = "quantity")
    public final double quantity;

    @Json(name = "unit")
    public final String unit;

    @Json(name = "order_index")
    public final int orderIndex;

    @Json(name = "macros")
    public final MacroBreakdown macros;

    public RecipeIngredientRequest(String foodId, double quantity,
                                  String unit, int orderIndex,
                                  MacroBreakdown macros) {
        this.foodId = foodId;
        this.quantity = quantity;
        this.unit = unit;
        this.orderIndex = orderIndex;
        this.macros = macros;
    }
}