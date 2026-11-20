package com.lupefit.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lupefit.app.data.local.entity.Streak;

import java.util.List;

@Dao
public interface StreakDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Streak streak);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Streak> streaks);

    @Update
    int update(Streak streak);

    @Query("UPDATE streaks SET currentStreak = :currentStreak, longestStreak = :longestStreak, lastAchievedDate = :lastAchievedDate, updatedAt = :updatedAt WHERE userId = :userId AND streakType = :streakType")
    int updateStreakStats(long userId, String streakType, int currentStreak, int longestStreak, int lastAchievedDate, long updatedAt);

    @Query("DELETE FROM streaks WHERE id = :id")
    int deleteById(long id);

    @Query("SELECT * FROM streaks WHERE userId = :userId AND streakType = :streakType")
    LiveData<Streak> getByUserAndType(long userId, String streakType);

    @Query("SELECT * FROM streaks WHERE userId = :userId")
    LiveData<List<Streak>> getAllByUser(long userId);

    @Query("SELECT * FROM streaks WHERE userId = :userId AND currentStreak > 0 ORDER BY currentStreak DESC")
    LiveData<List<Streak>> getActiveStreaksByUser(long userId);
}