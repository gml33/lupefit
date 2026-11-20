package com.lupefit.core.domain.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Ingredientes de una receta.
 */
@Entity(
    tableName = "recipe_ingredients",
    foreignKeys = {
        @ForeignKey(
            entity = Recipe.class,
            parentColumns = "id",
            childColumns = "recipeId",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = Food.class,
            parentColumns = "id",
            childColumns = "foodId",
            onDelete = ForeignKey.RESTRICT
        )
    },
    indices = {
        @Index(value = {"recipeId", "orderIndex"}),
        @Index(value = {"foodId"})
    }
)
public class RecipeIngredient {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public long recipeId;

    @NonNull
    public long foodId; // referencia a Food local

    public double quantityGrams;

    @NonNull
    public String quantityDisplay; // "200g", "1 taza", "2 unidades"

    public int orderIndex; // orden en la lista

    // Para alimentos custom que no están en DB local
    public String customFoodName;
    public int customCaloriesPer100g;
    public double customProteinPer100g;
    public double customCarbsPer100g;
    public double customFatPer100g;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeIngredient)) return false;
        RecipeIngredient that = (RecipeIngredient) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}