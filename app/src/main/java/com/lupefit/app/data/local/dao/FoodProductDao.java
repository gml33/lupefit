package com.lupefit.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.lupefit.app.data.local.entity.FoodProduct;

import java.util.List;

/**
 * DAO para productos alimenticios del catálogo offline-first.
 */
@Dao
public interface FoodProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(FoodProduct product);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<FoodProduct> products);

    @Query("SELECT * FROM food_products WHERE id = :id")
    LiveData<FoodProduct> getById(int id);

    @Query("SELECT * FROM food_products WHERE barcode = :barcode")
    LiveData<FoodProduct> findByBarcode(String barcode);

    @Query("SELECT * FROM food_products WHERE barcode = :barcode")
    FoodProduct findByBarcodeSync(String barcode);

    @Query("SELECT * FROM food_products WHERE name LIKE '%' || :query || '%' ORDER BY " +
           "CASE WHEN name = :query THEN 0 " +
           "WHEN name LIKE :query || '%' THEN 1 " +
           "WHEN name LIKE '%' || :query || '%' THEN 2 " +
           "ELSE 3 END, " +
           "CASE WHEN is_fresh_food = 1 THEN 0 ELSE 1 END, " +
           "name ASC LIMIT :limit")
    LiveData<List<FoodProduct>> searchByName(String query, int limit);

    @Query("SELECT * FROM food_products WHERE name LIKE '%' || :query || '%' ORDER BY " +
           "CASE WHEN name = :query THEN 0 " +
           "WHEN name LIKE :query || '%' THEN 1 " +
           "WHEN name LIKE '%' || :query || '%' THEN 2 " +
           "ELSE 3 END, " +
           "CASE WHEN is_fresh_food = 1 THEN 0 ELSE 1 END, " +
           "name ASC LIMIT :limit")
    List<FoodProduct> searchByNameSync(String query, int limit);

    @Query("SELECT * FROM food_products WHERE is_fresh_food = 1 ORDER BY name ASC")
    LiveData<List<FoodProduct>> getAllFreshFoods();

    @Query("SELECT * FROM food_products WHERE is_fresh_food = 1 ORDER BY name ASC")
    List<FoodProduct> getAllFreshFoodsSync();

    @Query("SELECT * FROM food_products WHERE last_updated < :olderThan")
    LiveData<List<FoodProduct>> getStaleProducts(long olderThan);

    @Query("SELECT COUNT(*) FROM food_products WHERE is_fresh_food = 1")
    int countFreshFoods();
}