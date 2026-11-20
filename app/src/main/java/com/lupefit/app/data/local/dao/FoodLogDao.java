package com.lupefit.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lupefit.app.data.local.entity.FoodLog;

import java.util.List;

@Dao
public interface FoodLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(FoodLog foodLog);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<FoodLog> foodLogs);

    @Update
    int update(FoodLog foodLog);

    @Query("UPDATE food_logs SET isSynced = 1, serverId = :serverId, lastModified = :lastModified WHERE id = :id")
    int markSynced(long id, String serverId, long lastModified);

    @Query("UPDATE food_logs SET isSynced = 0, syncAction = 'UPDATE', lastModified = :lastModified WHERE id = :id")
    int markDirty(long id, long lastModified);

    @Query("DELETE FROM food_logs WHERE id = :id")
    int deleteById(long id);

    @Query("SELECT * FROM food_logs WHERE id = :id")
    LiveData<FoodLog> getById(long id);

    @Query("SELECT * FROM food_logs WHERE serverId = :serverId")
    LiveData<FoodLog> getByServerId(String serverId);

    @Query("SELECT * FROM food_logs WHERE userId = :userId AND dateKey = :dateKey ORDER BY mealType, timestamp")
    LiveData<List<FoodLog>> getByUserAndDate(long userId, int dateKey);

    @Query("SELECT * FROM food_logs WHERE userId = :userId AND dateKey BETWEEN :startDateKey AND :endDateKey ORDER BY timestamp DESC")
    LiveData<List<FoodLog>> getByUserAndDateRange(long userId, int startDateKey, int endDateKey);

    @Query("SELECT * FROM food_logs WHERE userId = :userId AND isSynced = 0 ORDER BY timestamp")
    LiveData<List<FoodLog>> getUnsyncedByUser(long userId);

    @Query("SELECT * FROM food_logs WHERE userId = :userId ORDER BY timestamp DESC LIMIT :limit")
    LiveData<List<FoodLog>> getRecentByUser(long userId, int limit);

    @Query("SELECT SUM(calories) FROM food_logs WHERE userId = :userId AND dateKey = :dateKey")
    LiveData<Integer> getTotalCaloriesByDate(long userId, int dateKey);

    @Query("SELECT mealType, SUM(calories) as totalCal, SUM(proteinGrams) as totalProt, SUM(carbsGrams) as totalCarb, SUM(fatGrams) as totalFat " +
           "FROM food_logs WHERE userId = :userId AND dateKey = :dateKey GROUP BY mealType")
    LiveData<List<MealMacroSummary>> getMacrosByMealType(long userId, int dateKey);

    @Query("SELECT * FROM food_logs WHERE userId = :userId AND foodName LIKE :query ORDER BY timestamp DESC LIMIT 20")
    LiveData<List<FoodLog>> searchByFoodName(long userId, String query);

    class MealMacroSummary {
        public String mealType;
        public int totalCal;
        public double totalProt;
        public double totalCarb;
        public double totalFat;
    }
}