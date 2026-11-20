package com.lupefit.app.domain.repository;

import com.lupefit.app.data.local.dao.RecipeDao;
import com.lupefit.app.data.local.dao.RecipeIngredientDao;

/** Repositorio para Recipe - stub para compilación base. */
public class RecipeRepository {
    private final RecipeDao recipeDao;
    private final RecipeIngredientDao ingredientDao;
    public RecipeRepository(RecipeDao recipeDao, RecipeIngredientDao ingredientDao) {
        this.recipeDao = recipeDao;
        this.ingredientDao = ingredientDao;
    }
}