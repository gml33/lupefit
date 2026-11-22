package com.lupefit.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lupefit.app.data.local.entity.EscaneoEntity;

import java.util.List;

@Dao
public interface EscaneoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertEscaneo(EscaneoEntity escaneo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAllEscaneos(List<EscaneoEntity> escaneos);

    @Update
    int updateEscaneo(EscaneoEntity escaneo);

    @Query("UPDATE escaneo_entries SET isSynced = 1, serverId = :serverId, lastModified = :lastModified WHERE id = :id")
    int markEscaneoSynced(long id, String serverId, long lastModified);

    @Query("DELETE FROM escaneo_entries WHERE id = :id")
    int deleteEscaneoById(long id);

    @Query("SELECT * FROM escaneo_entries WHERE id = :id")
    LiveData<EscaneoEntity> getEscaneoById(long id);

    @Query("SELECT * FROM escaneo_entries WHERE serverId = :serverId")
    LiveData<EscaneoEntity> getEscaneoByServerId(String serverId);

    @Query("SELECT * FROM escaneo_entries ORDER BY timestamp DESC")
    LiveData<List<EscaneoEntity>> getAllEscaneos();

    @Query("SELECT * FROM escaneo_entries ORDER BY timestamp DESC")
    List<EscaneoEntity> getAllEscaneosSync();

    @Query("SELECT * FROM escaneo_entries WHERE tipo = :tipo ORDER BY timestamp DESC")
    LiveData<List<EscaneoEntity>> getEscaneosByType(String tipo);

    @Query("SELECT * FROM escaneo_entries WHERE codigoBarras = :codigoBarras ORDER BY timestamp DESC LIMIT 1")
    LiveData<EscaneoEntity> getLatestByBarcode(String codigoBarras);

    @Query("SELECT * FROM escaneo_entries WHERE isSynced = 0 ORDER BY lastModified")
    LiveData<List<EscaneoEntity>> getUnsyncedEscaneos();
}