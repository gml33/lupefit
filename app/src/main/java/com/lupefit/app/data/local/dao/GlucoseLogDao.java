package com.lupefit.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lupefit.app.data.local.entity.GlucoseLog;

import java.util.List;

@Dao
public interface GlucoseLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(GlucoseLog log);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<GlucoseLog> logs);

    @Update
    int update(GlucoseLog log);

    @Query("UPDATE glucose_logs SET isSynced = 1, serverId = :serverId, lastModified = :lastModified WHERE id = :id")
    int markSynced(long id, String serverId, long lastModified);

    @Query("DELETE FROM glucose_logs WHERE id = :id")
    int deleteById(long id);

    @Query("SELECT * FROM glucose_logs WHERE id = :id")
    LiveData<GlucoseLog> getById(long id);

    @Query("SELECT * FROM glucose_logs WHERE userId = :userId AND dateKey = :dateKey ORDER BY timestamp")
    LiveData<List<GlucoseLog>> getByUserAndDate(long userId, int dateKey);

    @Query("SELECT * FROM glucose_logs WHERE userId = :userId AND dateKey BETWEEN :startDateKey AND :endDateKey ORDER BY timestamp")
    LiveData<List<GlucoseLog>> getByUserAndDateRange(long userId, int startDateKey, int endDateKey);

    @Query("SELECT * FROM glucose_logs WHERE userId = :userId AND isSynced = 0 ORDER BY timestamp")
    LiveData<List<GlucoseLog>> getUnsyncedByUser(long userId);

    @Query("SELECT AVG(glucoseMgDl) FROM glucose_logs WHERE userId = :userId AND dateKey = :dateKey")
    LiveData<Double> getAverageByDate(long userId, int dateKey);

    @Query("SELECT MIN(glucoseMgDl) as min, MAX(glucoseMgDl) as max FROM glucose_logs WHERE userId = :userId AND dateKey = :dateKey")
    LiveData<GlucoseRange> getRangeByDate(long userId, int dateKey);

    @Query("SELECT * FROM glucose_logs WHERE userId = :userId ORDER BY timestamp DESC LIMIT :limit")
    LiveData<List<GlucoseLog>> getRecentByUser(long userId, int limit);

    class GlucoseRange {
        public int min;
        public int max;
    }
}