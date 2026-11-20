package com.lupefit.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lupefit.app.data.local.entity.WaterLog;

import java.util.List;

@Dao
public interface WaterLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(WaterLog log);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<WaterLog> logs);

    @Update
    int update(WaterLog log);

    @Query("UPDATE water_logs SET isSynced = 1, serverId = :serverId, lastModified = :lastModified WHERE id = :id")
    int markSynced(long id, String serverId, long lastModified);

    @Query("DELETE FROM water_logs WHERE id = :id")
    int deleteById(long id);

    @Query("SELECT * FROM water_logs WHERE id = :id")
    LiveData<WaterLog> getById(long id);

    @Query("SELECT * FROM water_logs WHERE userId = :userId AND dateKey = :dateKey ORDER BY timestamp")
    LiveData<List<WaterLog>> getByUserAndDate(long userId, int dateKey);

    @Query("SELECT SUM(quantityMl) FROM water_logs WHERE userId = :userId AND dateKey = :dateKey")
    LiveData<Integer> getTotalMlByDate(long userId, int dateKey);

    @Query("SELECT * FROM water_logs WHERE userId = :userId AND dateKey BETWEEN :startDateKey AND :endDateKey ORDER BY timestamp")
    LiveData<List<WaterLog>> getByUserAndDateRange(long userId, int startDateKey, int endDateKey);

    @Query("SELECT * FROM water_logs WHERE userId = :userId AND isSynced = 0 ORDER BY timestamp")
    LiveData<List<WaterLog>> getUnsyncedByUser(long userId);

    @Query("SELECT * FROM water_logs WHERE userId = :userId ORDER BY timestamp DESC LIMIT :limit")
    LiveData<List<WaterLog>> getRecentByUser(long userId, int limit);
}