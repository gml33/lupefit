package com.lupefit.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lupefit.app.data.local.entity.PersonalRecordEntity;

import java.util.List;

@Dao
public interface PersonalRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(PersonalRecordEntity record);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<PersonalRecordEntity> records);

    @Update
    int update(PersonalRecordEntity record);

    @Query("UPDATE personal_records SET isSynced = 1, serverId = :serverId, lastModified = :lastModified WHERE id = :id")
    int markSynced(long id, String serverId, long lastModified);

    @Query("DELETE FROM personal_records WHERE id = :id")
    int deleteById(long id);

    @Query("SELECT * FROM personal_records WHERE id = :id")
    LiveData<PersonalRecordEntity> getById(long id);

    @Query("SELECT * FROM personal_records WHERE serverId = :serverId")
    LiveData<PersonalRecordEntity> getByServerId(String serverId);

    @Query("SELECT * FROM personal_records WHERE userId = :userId AND exerciseId = :exerciseId AND recordType = :recordType ORDER BY achievedAt DESC")
    LiveData<List<PersonalRecordEntity>> getByUserExerciseAndType(long userId, long exerciseId, String recordType);

    @Query("SELECT * FROM personal_records WHERE userId = :userId AND exerciseId = :exerciseId ORDER BY achievedAt DESC")
    LiveData<List<PersonalRecordEntity>> getByUserAndExercise(long userId, long exerciseId);

    @Query("SELECT * FROM personal_records WHERE userId = :userId AND recordType = :recordType ORDER BY achievedAt DESC LIMIT :limit")
    LiveData<List<PersonalRecordEntity>> getByUserAndType(long userId, String recordType, int limit);

    @Query("SELECT * FROM personal_records WHERE userId = :userId ORDER BY achievedAt DESC")
    LiveData<List<PersonalRecordEntity>> getAllByUser(long userId);

    @Query("SELECT * FROM personal_records WHERE userId = :userId AND isSynced = 0 ORDER BY achievedAt")
    LiveData<List<PersonalRecordEntity>> getUnsyncedByUser(long userId);

    @Query("SELECT * FROM personal_records WHERE userId = :userId AND achievedAt >= :since ORDER BY achievedAt DESC")
    LiveData<List<PersonalRecordEntity>> getSinceDate(long userId, long since);

    @Query("SELECT MAX(weightKg) FROM personal_records WHERE userId = :userId AND exerciseId = :exerciseId AND recordType = 'ONE_RM'")
    LiveData<Double> getMaxOneRM(long userId, long exerciseId);

    @Query("SELECT MAX(totalVolumeKg) FROM personal_records WHERE userId = :userId AND exerciseId = :exerciseId AND recordType = 'VOLUME'")
    LiveData<Double> getMaxVolume(long userId, long exerciseId);

    @Query("SELECT * FROM personal_records WHERE userId = :userId AND isSynced = 0")
    LiveData<List<PersonalRecordEntity>> getAllUnsynced(long userId);
}