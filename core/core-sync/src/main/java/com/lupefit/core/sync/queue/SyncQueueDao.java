package com.lupefit.core.sync.queue;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lupefit.core.sync.model.SyncQueueItem;

import java.util.List;

/**
 * DAO para la cola de sincronización
 */
@Dao
public interface SyncQueueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(SyncQueueItem item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<SyncQueueItem> items);

    @Update
    int update(SyncQueueItem item);

    @Update
    int updateAll(List<SyncQueueItem> items);

    @Query("DELETE FROM sync_queue WHERE id = :id")
    int deleteById(long id);

    @Query("DELETE FROM sync_queue WHERE entity_type = :entityType AND entity_id = :entityId")
    int deleteByEntity(String entityType, String entityId);

    @Query("SELECT * FROM sync_queue WHERE id = :id")
    LiveData<SyncQueueItem> getById(long id);

    @Query("SELECT * FROM sync_queue WHERE entity_type = :entityType AND entity_id = :entityId")
    LiveData<SyncQueueItem> getByEntity(String entityType, String entityId);

    @Query("SELECT * FROM sync_queue WHERE entity_type = :entityType AND entity_id = :entityId")
    SyncQueueItem getByEntitySync(String entityType, String entityId);

    @Query("SELECT * FROM sync_queue WHERE status = :status ORDER BY priority DESC, created_at ASC")
    LiveData<List<SyncQueueItem>> getByStatus(String status);

    @Query("SELECT * FROM sync_queue WHERE status IN ('PENDING', 'FAILED') AND (next_retry_at = 0 OR next_retry_at <= :now) ORDER BY priority DESC, created_at ASC LIMIT :limit")
    LiveData<List<SyncQueueItem>> getReadyForSync(long now, int limit);

    @Query("SELECT * FROM sync_queue WHERE status IN ('PENDING', 'FAILED') AND (next_retry_at = 0 OR next_retry_at <= :now) ORDER BY priority DESC, created_at ASC LIMIT :limit")
    List<SyncQueueItem> getReadyForSyncSync(long now, int limit);

    @Query("SELECT * FROM sync_queue WHERE status = 'SYNCING'")
    LiveData<List<SyncQueueItem>> getSyncing();

    @Query("SELECT * FROM sync_queue WHERE status = 'CONFLICT'")
    LiveData<List<SyncQueueItem>> getConflicts();

    @Query("SELECT COUNT(*) FROM sync_queue WHERE status = 'PENDING'")
    LiveData<Integer> getPendingCount();

    @Query("SELECT COUNT(*) FROM sync_queue WHERE status = 'PENDING'")
    int getPendingCountSync();

    @Query("SELECT COUNT(*) FROM sync_queue WHERE status = 'FAILED' AND retry_count >= :maxRetries")
    LiveData<Integer> getMaxRetriesExceededCount(int maxRetries);

    @Query("SELECT COUNT(*) FROM sync_queue WHERE status = 'FAILED' AND retry_count >= :maxRetries")
    int getMaxRetriesExceededCountSync(int maxRetries);

    @Query("DELETE FROM sync_queue WHERE status = 'SYNCED' AND updated_at < :olderThan")
    int deleteOldSynced(long olderThan);

    @Query("UPDATE sync_queue SET status = 'PENDING', retry_count = 0, next_retry_at = 0, last_error = '' WHERE status = 'FAILED'")
    int resetFailedItems();
}