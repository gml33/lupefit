package com.lupefit.core.network.dto.recipe;

import com.lupefit.core.network.dto.common.MacroBreakdown;
import com.squareup.moshi.Json;

import java.util.List;

/** Request para crear/actualizar receta */
public final class RecipeRequest {
    @Json(name = "name")
    public final String name;

    @Json(name = "description")
    public final String description;

    @Json(name = "instructions")
    public final String instructions;

    @Json(name = "prep_time_min")
    public final int prepTimeMin;

    @Json(name = "cook_time_min")
    public final int cookTimeMin;

    @Json(name = "servings")
    public final int servings;

    @Json(name = "is_public")
    public final boolean isPublic;

    @Json(name = "image_base64")
    public final String imageBase64;

    @Json(name = "source_url")
    public final String sourceUrl;

    @Json(name = "macros_per_serving")
    public final MacroBreakdown macrosPerServing;

    @Json(name = "ingredients")
    public final List<RecipeIngredientRequest> ingredients;

    @Json(name = "tags")
    public final List<String> tags;

    @Json(name = "collection_id")
    public final String collectionId;

    public RecipeRequest(String name, String description, String instructions,
                        int prepTimeMin, int cookTimeMin, int servings,
                        boolean isPublic, String imageBase64, String sourceUrl,
                        MacroBreakdown macrosPerServing,
                        List<RecipeIngredientRequest> ingredients,
                        List<String> tags, String collectionId) {
        this.name = name;
        this.description = description;
        this.instructions = instructions;
        this.prepTimeMin = prepTimeMin;
        this.cookTimeMin = cookTimeMin;
        this.servings = servings;
        this.isPublic = isPublic;
        this.imageBase64 = imageBase64;
        this.sourceUrl = sourceUrl;
        this.macrosPerServing = macrosPerServing;
        this.ingredients = ingredients;
        this.tags = tags;
        this.collectionId = collectionId;
    }
}