package com.lupefit.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.lupefit.app.data.local.entity.Recipe;
import com.lupefit.app.data.local.entity.RecipeIngredient;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Recipe recipe);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Recipe> recipes);

    @Update
    int update(Recipe recipe);

    @Query("UPDATE recipes SET isSynced = 1, serverId = :serverId, lastModified = :lastModified WHERE id = :id")
    int markSynced(long id, String serverId, long lastModified);

    @Query("DELETE FROM recipes WHERE id = :id")
    int deleteById(long id);

    @Query("SELECT * FROM recipes WHERE id = :id")
    LiveData<Recipe> getById(long id);

    @Query("SELECT * FROM recipes WHERE serverId = :serverId")
    LiveData<Recipe> getByServerId(String serverId);

    @Query("SELECT * FROM recipes WHERE userId = :userId ORDER BY timestamp DESC")
    LiveData<List<Recipe>> getByUser(long userId);

    @Query("SELECT * FROM recipes WHERE isPublic = 1 ORDER BY timestamp DESC LIMIT :limit")
    LiveData<List<Recipe>> getPublicRecipes(int limit);

    @Query("SELECT * FROM recipes WHERE collectionId = :collectionId ORDER BY name")
    LiveData<List<Recipe>> getByCollection(String collectionId);

    @Query("SELECT * FROM recipes WHERE name LIKE :query ORDER BY timestamp DESC LIMIT :limit")
    LiveData<List<Recipe>> searchByName(String query, int limit);

    @Query("SELECT * FROM recipes WHERE userId = :userId AND isSynced = 0 ORDER BY lastModified")
    LiveData<List<Recipe>> getUnsyncedByUser(long userId);

    // Ingredients - Transaction para insertar receta + ingredientes
    @Transaction
    default long insertWithIngredients(RecipeIngredientDao ingredientDao, Recipe recipe, List<RecipeIngredient> ingredients) {
        long recipeId = insert(recipe);
        for (int i = 0; i < ingredients.size(); i++) {
            ingredients.get(i).recipeId = recipeId;
            ingredients.get(i).orderIndex = i;
        }
        ingredientDao.insertAll(ingredients);
        return recipeId;
    }

    @Transaction
    default void updateWithIngredients(RecipeIngredientDao ingredientDao, Recipe recipe, List<RecipeIngredient> ingredients) {
        update(recipe);
        ingredientDao.deleteByRecipeId(recipe.id);
        for (int i = 0; i < ingredients.size(); i++) {
            ingredients.get(i).recipeId = recipe.id;
            ingredients.get(i).orderIndex = i;
        }
        ingredientDao.insertAll(ingredients);
    }
}