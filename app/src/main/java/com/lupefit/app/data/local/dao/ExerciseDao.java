package com.lupefit.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lupefit.app.data.local.entity.Exercise;

import java.util.List;

@Dao
public interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Exercise exercise);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Exercise> exercises);

    @Update
    int update(Exercise exercise);

    @Query("UPDATE exercises SET isSynced = 1, serverId = :serverId, lastModified = :lastModified WHERE id = :id")
    int markSynced(long id, String serverId, long lastModified);

    @Query("DELETE FROM exercises WHERE id = :id")
    int deleteById(long id);

    @Query("SELECT * FROM exercises WHERE id = :id")
    LiveData<Exercise> getById(long id);

    @Query("SELECT * FROM exercises WHERE serverId = :serverId")
    LiveData<Exercise> getByServerId(String serverId);

    @Query("SELECT * FROM exercises WHERE category = :category ORDER BY name")
    LiveData<List<Exercise>> getByCategory(String category);

    @Query("SELECT * FROM exercises WHERE movementPattern = :pattern ORDER BY name")
    LiveData<List<Exercise>> getByMovementPattern(String pattern);

    @Query("SELECT * FROM exercises WHERE isCustom = 0 ORDER BY category, name")
    LiveData<List<Exercise>> getOfficialExercises();

    @Query("SELECT * FROM exercises WHERE isCustom = 1 AND createdByUserId = :userId ORDER BY name")
    LiveData<List<Exercise>> getCustomByUser(long userId);

    @Query("SELECT * FROM exercises WHERE name LIKE :query ORDER BY CASE WHEN isCustom = 0 THEN 0 ELSE 1 END, name LIMIT :limit")
    LiveData<List<Exercise>> searchByName(String query, int limit);

    @Query("SELECT * FROM exercises WHERE name LIKE :query ORDER BY CASE WHEN isCustom = 0 THEN 0 ELSE 1 END, name LIMIT :limit")
    List<Exercise> searchByNameSync(String query, int limit);

    @Query("SELECT * FROM exercises WHERE isSynced = 0 ORDER BY lastModified")
    LiveData<List<Exercise>> getUnsynced();

    @Query("SELECT * FROM exercises ORDER BY category, name")
    LiveData<List<Exercise>> getAll();
}