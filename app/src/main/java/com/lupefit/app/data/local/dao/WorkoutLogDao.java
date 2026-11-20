package com.lupefit.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lupefit.app.data.local.entity.WorkoutLog;

import java.util.List;

@Dao
public interface WorkoutLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(WorkoutLog workoutLog);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<WorkoutLog> workoutLogs);

    @Update
    int update(WorkoutLog workoutLog);

    @Query("UPDATE workout_logs SET isSynced = 1, serverId = :serverId, lastModified = :lastModified WHERE id = :id")
    int markSynced(long id, String serverId, long lastModified);

    @Query("UPDATE workout_logs SET isSynced = 0, syncAction = 'UPDATE', lastModified = :lastModified WHERE id = :id")
    int markDirty(long id, long lastModified);

    @Query("DELETE FROM workout_logs WHERE id = :id")
    int deleteById(long id);

    @Query("SELECT * FROM workout_logs WHERE id = :id")
    LiveData<WorkoutLog> getById(long id);

    @Query("SELECT * FROM workout_logs WHERE serverId = :serverId")
    LiveData<WorkoutLog> getByServerId(String serverId);

    @Query("SELECT * FROM workout_logs WHERE userId = :userId AND dateKey = :dateKey ORDER BY timestamp")
    LiveData<List<WorkoutLog>> getByUserAndDate(long userId, int dateKey);

    @Query("SELECT * FROM workout_logs WHERE userId = :userId AND dateKey BETWEEN :startDateKey AND :endDateKey ORDER BY timestamp DESC")
    LiveData<List<WorkoutLog>> getByUserAndDateRange(long userId, int startDateKey, int endDateKey);

    @Query("SELECT * FROM workout_logs WHERE userId = :userId AND isSynced = 0 ORDER BY timestamp")
    LiveData<List<WorkoutLog>> getUnsyncedByUser(long userId);

    @Query("SELECT * FROM workout_logs WHERE userId = :userId AND workoutType LIKE :type ORDER BY timestamp DESC LIMIT :limit")
    LiveData<List<WorkoutLog>> getByType(long userId, String type, int limit);

    @Query("SELECT SUM(caloriesBurned) FROM workout_logs WHERE userId = :userId AND dateKey = :dateKey")
    LiveData<Integer> getTotalCaloriesBurnedByDate(long userId, int dateKey);

    @Query("SELECT SUM(durationSeconds) FROM workout_logs WHERE userId = :userId AND dateKey = :dateKey")
    LiveData<Integer> getTotalDurationByDate(long userId, int dateKey);

    @Query("SELECT * FROM workout_logs WHERE userId = :userId ORDER BY timestamp DESC LIMIT :limit")
    LiveData<List<WorkoutLog>> getRecentByUser(long userId, int limit);
}