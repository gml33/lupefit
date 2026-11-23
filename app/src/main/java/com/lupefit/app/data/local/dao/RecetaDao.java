package com.lupefit.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lupefit.app.data.local.entity.RecetaEntity;

import java.util.List;

@Dao
public interface RecetaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertReceta(RecetaEntity receta);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAllRecetas(List<RecetaEntity> recetas);

    @Update
    int updateReceta(RecetaEntity receta);

    @Query("UPDATE recetas SET isSynced = 1, serverId = :serverId, lastModified = :lastModified WHERE id = :id")
    int markRecetaSynced(long id, String serverId, long lastModified);

    @Query("DELETE FROM recetas WHERE id = :id")
    int deleteRecetaById(long id);

    @Query("SELECT * FROM recetas WHERE id = :id")
    RecetaEntity getRecetaByIdSync(long id);

    @Query("SELECT * FROM recetas WHERE id = :id")
    LiveData<RecetaEntity> getRecetaById(long id);

    @Query("SELECT * FROM recetas WHERE serverId = :serverId")
    LiveData<RecetaEntity> getRecetaByServerId(String serverId);

    @Query("SELECT * FROM recetas ORDER BY timestamp DESC")
    LiveData<List<RecetaEntity>> getAllRecetas();

    @Query("SELECT * FROM recetas ORDER BY timestamp DESC")
    List<RecetaEntity> getAllRecetasSync();

    @Query("SELECT * FROM recetas WHERE userId = :userId ORDER BY timestamp DESC")
    LiveData<List<RecetaEntity>> getRecetasByUser(long userId);

    @Query("SELECT * FROM recetas WHERE categoria = :categoria ORDER BY timestamp DESC")
    List<RecetaEntity> getRecetasByCategorySync(String categoria);

    @Query("SELECT * FROM recetas WHERE categoria = :categoria ORDER BY timestamp DESC")
    LiveData<List<RecetaEntity>> getRecetasByCategory(String categoria);

    @Query("SELECT * FROM recetas WHERE esFavorita = 1 ORDER BY timestamp DESC")
    List<RecetaEntity> getFavoriteRecetasSync();

    @Query("SELECT * FROM recetas WHERE esFavorita = 1 ORDER BY timestamp DESC")
    LiveData<List<RecetaEntity>> getFavoriteRecetas();

    @Query("SELECT * FROM recetas WHERE nombre LIKE :query ORDER BY timestamp DESC LIMIT 20")
    List<RecetaEntity> searchRecetasSync(String query);

    @Query("SELECT * FROM recetas WHERE nombre LIKE :query ORDER BY timestamp DESC LIMIT 20")
    LiveData<List<RecetaEntity>> searchRecetas(String query);

    @Query("SELECT * FROM recetas WHERE isSynced = 0 ORDER BY lastModified")
    LiveData<List<RecetaEntity>> getUnsyncedRecetas();
}