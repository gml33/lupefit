package com.lupefit.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lupefit.app.data.local.entity.MoodEntry;

import java.util.List;

@Dao
public interface MoodEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(MoodEntry entry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<MoodEntry> entries);

    @Update
    int update(MoodEntry entry);

    @Query("UPDATE mood_entries SET isSynced = 1, serverId = :serverId, lastModified = :lastModified WHERE id = :id")
    int markSynced(long id, String serverId, long lastModified);

    @Query("DELETE FROM mood_entries WHERE id = :id")
    int deleteById(long id);

    @Query("SELECT * FROM mood_entries WHERE id = :id")
    LiveData<MoodEntry> getById(long id);

    @Query("SELECT * FROM mood_entries WHERE userId = :userId AND dateKey = :dateKey ORDER BY timestamp")
    LiveData<List<MoodEntry>> getByUserAndDate(long userId, int dateKey);

    @Query("SELECT * FROM mood_entries WHERE userId = :userId AND dateKey BETWEEN :startDateKey AND :endDateKey ORDER BY timestamp")
    LiveData<List<MoodEntry>> getByUserAndDateRange(long userId, int startDateKey, int endDateKey);

    @Query("SELECT * FROM mood_entries WHERE userId = :userId AND context = :context ORDER BY timestamp DESC LIMIT :limit")
    LiveData<List<MoodEntry>> getByContext(long userId, String context, int limit);

    @Query("SELECT * FROM mood_entries WHERE userId = :userId AND isSynced = 0 ORDER BY timestamp")
    LiveData<List<MoodEntry>> getUnsyncedByUser(long userId);

    @Query("SELECT AVG(moodLevel) FROM mood_entries WHERE userId = :userId AND dateKey BETWEEN :startDateKey AND :endDateKey")
    LiveData<Double> getAverageMoodInRange(long userId, int startDateKey, int endDateKey);

    @Query("SELECT moodLevel, COUNT(*) as count FROM mood_entries WHERE userId = :userId AND dateKey BETWEEN :startDateKey AND :endDateKey GROUP BY moodLevel")
    LiveData<List<MoodDistribution>> getMoodDistribution(long userId, int startDateKey, int endDateKey);

    class MoodDistribution {
        public int moodLevel;
        public int count;
    }
}