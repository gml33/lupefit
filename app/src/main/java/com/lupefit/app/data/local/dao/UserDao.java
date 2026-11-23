package com.lupefit.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lupefit.app.data.local.entity.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(User user);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertIfMissing(User user);

    @Update
    int update(User user);

    @Query("UPDATE users SET isSynced = 1, serverId = :serverId, accessToken = :accessToken, refreshToken = :refreshToken, tokenExpiresAt = :tokenExpiresAt, lastSyncAt = :lastSyncAt WHERE id = :id")
    int markSynced(long id, String serverId, String accessToken, String refreshToken, long tokenExpiresAt, long lastSyncAt);

    @Query("DELETE FROM users WHERE id = :id")
    int deleteById(long id);

    @Query("SELECT * FROM users WHERE id = :id")
    LiveData<User> getById(long id);

    @Query("SELECT * FROM users WHERE email = :email")
    LiveData<User> getByEmail(String email);

    @Query("SELECT * FROM users WHERE serverId = :serverId")
    LiveData<User> getByServerId(String serverId);

    @Query("SELECT * FROM users WHERE hasCloudAccount = 1 AND isSynced = 0")
    LiveData<List<User>> getUnsyncedUsers();

    @Query("SELECT * FROM users WHERE hasCloudAccount = 1 ORDER BY lastSyncAt DESC LIMIT 1")
    LiveData<User> getLastSyncedUser();

    @Query("SELECT * FROM users ORDER BY createdAt DESC LIMIT 1")
    LiveData<User> getMostRecentUser();
}