package com.lupefit.core.network.dto.recipe;

import com.lupefit.core.network.dto.common.MacroBreakdown;
import com.squareup.moshi.Json;

import java.util.List;

/** Respuesta de receta */
public final class RecipeResponse {
    @Json(name = "id")
    public final String id;

    @Json(name = "user_id")
    public final String userId;

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

    @Json(name = "image_url")
    public final String imageUrl;

    @Json(name = "source_url")
    public final String sourceUrl;

    @Json(name = "macros_per_serving")
    public final MacroBreakdown macrosPerServing;

    @Json(name = "ingredients")
    public final List<RecipeIngredientResponse> ingredients;

    @Json(name = "tags")
    public final List<String> tags;

    @Json(name = "collection_id")
    public final String collectionId;

    @Json(name = "is_synced")
    public final boolean isSynced;

    @Json(name = "created_at")
    public final String createdAt;

    @Json(name = "updated_at")
    public final String updatedAt;

    public RecipeResponse(String id, String userId, String name, String description,
                         String instructions, int prepTimeMin, int cookTimeMin,
                         int servings, boolean isPublic, String imageUrl,
                         String sourceUrl, MacroBreakdown macrosPerServing,
                         List<RecipeIngredientResponse> ingredients,
                         List<String> tags, String collectionId,
                         boolean isSynced, String createdAt, String updatedAt) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.instructions = instructions;
        this.prepTimeMin = prepTimeMin;
        this.cookTimeMin = cookTimeMin;
        this.servings = servings;
        this.isPublic = isPublic;
        this.imageUrl = imageUrl;
        this.sourceUrl = sourceUrl;
        this.macrosPerServing = macrosPerServing;
        this.ingredients = ingredients;
        this.tags = tags;
        this.collectionId = collectionId;
        this.isSynced = isSynced;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}