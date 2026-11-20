package com.lupefit.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lupefit.app.data.local.entity.RecipeIngredient;

import java.util.List;

@Dao
public interface RecipeIngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(RecipeIngredient ingredient);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<RecipeIngredient> ingredients);

    @Update
    int update(RecipeIngredient ingredient);

    @Query("DELETE FROM recipe_ingredients WHERE id = :id")
    int deleteById(long id);

    @Query("DELETE FROM recipe_ingredients WHERE recipeId = :recipeId")
    int deleteByRecipeId(long recipeId);

    @Query("SELECT * FROM recipe_ingredients WHERE recipeId = :recipeId ORDER BY orderIndex")
    LiveData<List<RecipeIngredient>> getByRecipeId(long recipeId);

    @Query("SELECT * FROM recipe_ingredients WHERE foodId = :foodId")
    LiveData<List<RecipeIngredient>> getByFoodId(long foodId);
}