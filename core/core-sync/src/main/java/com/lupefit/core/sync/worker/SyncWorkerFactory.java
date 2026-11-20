package com.lupefit.core.sync.worker;

import com.lupefit.core.network.SyncEngine;
import com.lupefit.core.sync.queue.SyncQueueRepository;

/**
 * Factory para proporcionar dependencias al SyncWorker.
 * La implementación la proporciona el módulo app para evitar dependencias circulares.
 */
public interface SyncWorkerFactory {

    SyncQueueRepository createSyncQueueRepository();

    SyncEngine createSyncEngine();
}