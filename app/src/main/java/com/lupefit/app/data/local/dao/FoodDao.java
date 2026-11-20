package com.lupefit.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lupefit.app.data.local.entity.Food;

import java.util.List;

@Dao
public interface FoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Food food);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Food> foods);

    @Update
    int update(Food food);

    @Query("UPDATE foods SET isSynced = 1, serverId = :serverId, lastModified = :lastModified WHERE id = :id")
    int markSynced(long id, String serverId, long lastModified);

    @Query("DELETE FROM foods WHERE id = :id")
    int deleteById(long id);

    @Query("SELECT * FROM foods WHERE id = :id")
    LiveData<Food> getById(long id);

    @Query("SELECT * FROM foods WHERE barcode = :barcode")
    LiveData<Food> getByBarcode(String barcode);

    @Query("SELECT * FROM foods WHERE serverId = :serverId")
    LiveData<Food> getByServerId(String serverId);

    @Query("SELECT * FROM foods WHERE name LIKE :query ORDER BY isVerified DESC, userCreated ASC, name LIMIT :limit")
    LiveData<List<Food>> searchByName(String query, int limit);

    @Query("SELECT * FROM foods WHERE userCreated = 1 AND createdByUserId = :userId ORDER BY name")
    LiveData<List<Food>> getUserCreatedByUser(long userId);

    @Query("SELECT * FROM foods WHERE isVerified = 1 ORDER BY name LIMIT :limit")
    LiveData<List<Food>> getVerifiedFoods(int limit);

    @Query("SELECT * FROM foods WHERE isSynced = 0 ORDER BY lastModified")
    LiveData<List<Food>> getUnsynced();

    @Query("SELECT * FROM foods WHERE searchTokens LIKE :tokens ORDER BY isVerified DESC LIMIT :limit")
    LiveData<List<Food>> searchByTokens(String tokens, int limit);
}