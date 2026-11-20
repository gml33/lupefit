package com.lupefit.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lupefit.app.data.local.entity.ProgressPhoto;

import java.util.List;

@Dao
public interface ProgressPhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ProgressPhoto photo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<ProgressPhoto> photos);

    @Update
    int update(ProgressPhoto photo);

    @Query("UPDATE progress_photos SET isSynced = 1, serverId = :serverId, serverUrl = :serverUrl, lastModified = :lastModified WHERE id = :id")
    int markSynced(long id, String serverId, String serverUrl, long lastModified);

    @Query("DELETE FROM progress_photos WHERE id = :id")
    int deleteById(long id);

    @Query("SELECT * FROM progress_photos WHERE id = :id")
    LiveData<ProgressPhoto> getById(long id);

    @Query("SELECT * FROM progress_photos WHERE serverId = :serverId")
    LiveData<ProgressPhoto> getByServerId(String serverId);

    @Query("SELECT * FROM progress_photos WHERE userId = :userId ORDER BY timestamp DESC")
    LiveData<List<ProgressPhoto>> getByUser(long userId);

    @Query("SELECT * FROM progress_photos WHERE userId = :userId AND photoType = :photoType ORDER BY timestamp DESC")
    LiveData<List<ProgressPhoto>> getByUserAndType(long userId, String photoType);

    @Query("SELECT * FROM progress_photos WHERE userId = :userId AND dateKey = :dateKey ORDER BY timestamp DESC")
    LiveData<List<ProgressPhoto>> getByUserAndDate(long userId, int dateKey);

    @Query("SELECT * FROM progress_photos WHERE userId = :userId AND isSynced = 0 ORDER BY timestamp")
    LiveData<List<ProgressPhoto>> getUnsyncedByUser(long userId);

    @Query("SELECT * FROM progress_photos WHERE userId = :userId ORDER BY timestamp DESC LIMIT :limit")
    LiveData<List<ProgressPhoto>> getRecentByUser(long userId, int limit);
}