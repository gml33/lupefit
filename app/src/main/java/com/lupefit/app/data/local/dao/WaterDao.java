package com.lupefit.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lupefit.app.data.local.entity.WaterEntity;

import java.util.List;

@Dao
public interface WaterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertWater(WaterEntity water);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAllWater(List<WaterEntity> waters);

    @Update
    int updateWater(WaterEntity water);

    @Query("UPDATE water_entries SET isSynced = 1, serverId = :serverId, lastModified = :lastModified WHERE id = :id")
    int markWaterSynced(long id, String serverId, long lastModified);

    @Query("DELETE FROM water_entries WHERE id = :id")
    int deleteWaterById(long id);

    @Query("SELECT * FROM water_entries WHERE id = :id")
    LiveData<WaterEntity> getWaterById(long id);

    @Query("SELECT * FROM water_entries WHERE serverId = :serverId")
    LiveData<WaterEntity> getWaterByServerId(String serverId);

    @Query("SELECT * FROM water_entries ORDER BY timestamp DESC")
    LiveData<List<WaterEntity>> getAllWaterEntries();

    @Query("SELECT * FROM water_entries ORDER BY timestamp DESC")
    List<WaterEntity> getAllWaterEntriesSync();

    @Query("SELECT * FROM water_entries WHERE fecha = :fecha ORDER BY timestamp DESC")
    LiveData<List<WaterEntity>> getWaterEntriesByDate(String fecha);

    @Query("SELECT * FROM water_entries WHERE fecha = :fecha ORDER BY timestamp DESC")
    List<WaterEntity> getWaterEntriesByDateSync(String fecha);

    @Query("SELECT SUM(cantidadMl) FROM water_entries WHERE fecha = :fecha")
    LiveData<Integer> getTotalWaterToday(String fecha);

    @Query("SELECT SUM(cantidadMl) FROM water_entries WHERE fecha = :fecha")
    Integer getTotalWaterTodaySync(String fecha);

    @Query("SELECT * FROM water_entries WHERE isSynced = 0 ORDER BY lastModified")
    LiveData<List<WaterEntity>> getUnsyncedWater();
}