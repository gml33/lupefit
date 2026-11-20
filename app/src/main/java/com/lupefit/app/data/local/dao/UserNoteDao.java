package com.lupefit.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lupefit.app.data.local.entity.UserNote;

import java.util.List;

@Dao
public interface UserNoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(UserNote note);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<UserNote> notes);

    @Update
    int update(UserNote note);

    @Query("UPDATE user_notes SET isSynced = 1, serverId = :serverId, lastModified = :lastModified WHERE id = :id")
    int markSynced(long id, String serverId, long lastModified);

    @Query("DELETE FROM user_notes WHERE id = :id")
    int deleteById(long id);

    @Query("SELECT * FROM user_notes WHERE id = :id")
    LiveData<UserNote> getById(long id);

    @Query("SELECT * FROM user_notes WHERE entityType = :entityType AND entityId = :entityId ORDER BY timestamp DESC")
    LiveData<List<UserNote>> getByEntity(long entityId, String entityType);

    @Query("SELECT * FROM user_notes WHERE userId = :userId AND dateKey = :dateKey ORDER BY timestamp DESC")
    LiveData<List<UserNote>> getByUserAndDate(long userId, int dateKey);

    @Query("SELECT * FROM user_notes WHERE userId = :userId AND isSynced = 0 ORDER BY timestamp")
    LiveData<List<UserNote>> getUnsyncedByUser(long userId);

    @Query("SELECT * FROM user_notes WHERE userId = :userId ORDER BY timestamp DESC LIMIT :limit")
    LiveData<List<UserNote>> getRecentByUser(long userId, int limit);
}