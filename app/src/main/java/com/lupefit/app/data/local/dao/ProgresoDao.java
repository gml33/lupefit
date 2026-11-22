package com.lupefit.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lupefit.app.data.local.entity.PesoEntity;

import java.util.List;

@Dao
public interface ProgresoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPeso(PesoEntity peso);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAllPeso(List<PesoEntity> pesos);

    @Update
    int updatePeso(PesoEntity peso);

    @Query("UPDATE peso_entries SET isSynced = 1, serverId = :serverId, lastModified = :lastModified WHERE id = :id")
    int markPesoSynced(long id, String serverId, long lastModified);

    @Query("DELETE FROM peso_entries WHERE id = :id")
    int deletePesoById(long id);

    @Query("SELECT * FROM peso_entries WHERE id = :id")
    LiveData<PesoEntity> getPesoById(long id);

    @Query("SELECT * FROM peso_entries WHERE serverId = :serverId")
    LiveData<PesoEntity> getPesoByServerId(String serverId);

    @Query("SELECT * FROM peso_entries ORDER BY dateKey DESC, timestamp DESC")
    LiveData<List<PesoEntity>> getAllPesoEntries();

    @Query("SELECT * FROM peso_entries ORDER BY dateKey DESC, timestamp DESC")
    List<PesoEntity> getAllPesoEntriesSync();

    @Query("SELECT * FROM peso_entries WHERE isSynced = 0 ORDER BY lastModified")
    LiveData<List<PesoEntity>> getUnsyncedPeso();
}