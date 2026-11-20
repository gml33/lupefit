package com.lupefit.core.sync.queue;

import androidx.lifecycle.LiveData;

import com.lupefit.core.sync.model.SyncQueueItem;

import java.util.List;

/**
 * Repositorio para la cola de sincronización
 */
public final class SyncQueueRepository {

    private final SyncQueueDao dao;

    public SyncQueueRepository(SyncQueueDao dao) {
        this.dao = dao;
    }

    public long enqueue(SyncQueueItem item) {
        return dao.insert(item);
    }

    public void enqueueAll(List<SyncQueueItem> items) {
        dao.insertAll(items);
    }

    public void update(SyncQueueItem item) {
        dao.update(item);
    }

    public void updateAll(List<SyncQueueItem> items) {
        dao.updateAll(items);
    }

    public void remove(long id) {
        dao.deleteById(id);
    }

    public void removeByEntity(String entityType, String entityId) {
        dao.deleteByEntity(entityType, entityId);
    }

    public LiveData<SyncQueueItem> getById(long id) {
        return dao.getById(id);
    }

    public LiveData<SyncQueueItem> getByEntity(String entityType, String entityId) {
        return dao.getByEntity(entityType, entityId);
    }

    // Método síncrono para uso interno del ConflictResolver
    public SyncQueueItem getByEntitySync(String entityType, String entityId) {
        return dao.getByEntitySync(entityType, entityId);
    }

    public LiveData<List<SyncQueueItem>> getPending() {
        return dao.getByStatus("PENDING");
    }

    public LiveData<List<SyncQueueItem>> getFailed() {
        return dao.getByStatus("FAILED");
    }

    public LiveData<List<SyncQueueItem>> getConflicts() {
        return dao.getConflicts();
    }

    public LiveData<List<SyncQueueItem>> getReadyForSync(long now, int limit) {
        return dao.getReadyForSync(now, limit);
    }

    public LiveData<List<SyncQueueItem>> getSyncing() {
        return dao.getSyncing();
    }

    public LiveData<Integer> getPendingCount() {
        return dao.getPendingCount();
    }

    public LiveData<Integer> getMaxRetriesExceededCount(int maxRetries) {
        return dao.getMaxRetriesExceededCount(maxRetries);
    }

    // Métodos síncronos para uso en Worker (background thread)
    public List<SyncQueueItem> getReadyForSyncSync(long now, int limit) {
        return dao.getReadyForSyncSync(now, limit);
    }

    public int getPendingCountSync() {
        return dao.getPendingCountSync();
    }

    public int getMaxRetriesExceededCountSync(int maxRetries) {
        return dao.getMaxRetriesExceededCountSync(maxRetries);
    }

    public int deleteOldSynced(long olderThan) {
        return dao.deleteOldSynced(olderThan);
    }

    public int resetFailedItems() {
        return dao.resetFailedItems();
    }
}