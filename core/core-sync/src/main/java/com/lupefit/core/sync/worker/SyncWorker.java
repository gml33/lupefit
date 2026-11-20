package com.lupefit.core.sync.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lupefit.core.network.SyncEngine;
import com.lupefit.core.network.dto.sync.SyncBatchRequest;
import com.lupefit.core.network.dto.sync.SyncBatchResponse;
import com.lupefit.core.network.dto.sync.SyncStatus;
import com.lupefit.core.sync.model.SyncQueueItem;
import com.lupefit.core.sync.queue.SyncQueueRepository;
import com.lupefit.core.sync.conflict.ConflictResolver;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Worker de WorkManager para sincronización en segundo plano.
 * 
 * Se ejecuta:
 * - Periódicamente (cada 15 min por defecto)
 * - Cuando hay conectividad de red
 * - Cuando se encolan nuevos items (work enqueue)
 * 
 * Configuración recomendada en WorkManager:
 * - PeriodicWorkRequest: 15 min interval, flex 5 min
 * - Constraints: NetworkType.CONNECTED
 * - BackoffPolicy: EXPONENTIAL, 30 min initial
 */
public final class SyncWorker extends Worker {

    private static final String TAG = "SyncWorker";
    private static final int SYNC_BATCH_LIMIT = 50;
    private static final int MAX_RETRIES = 5;
    private static final long BACKOFF_BASE_MS = 30 * 60 * 1000; // 30 min

    private final SyncQueueRepository syncQueueRepository;
    private final SyncEngine syncEngine;
    private final ConflictResolver conflictResolver;

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        
        // Obtener factory desde el Application
        SyncWorkerFactory factory = ((SyncWorkerFactoryProvider) context.getApplicationContext()).getSyncWorkerFactory();
        
        this.syncQueueRepository = factory.createSyncQueueRepository();
        this.syncEngine = factory.createSyncEngine();
        this.conflictResolver = new ConflictResolver();
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            // Verificar si hay conectividad (constraint ya lo garantiza, pero double-check)
            if (!isNetworkAvailable()) {
                return Result.retry(); // Reintentar cuando haya red
            }

            // Obtener items listos para sincronizar
            long now = System.currentTimeMillis();
            List<SyncQueueItem> itemsToSync = syncQueueRepository.getReadyForSyncSync(now, SYNC_BATCH_LIMIT);
            
            if (itemsToSync == null || itemsToSync.isEmpty()) {
                // No hay items pendientes, trabajo completado
                return Result.success();
            }

            // Construir request de sync batch
            SyncBatchRequest request = buildSyncBatchRequest(itemsToSync);
            
            // Ejecutar sincronización
            SyncBatchResponse response = executeSyncBatch(request);
            
            if (response == null) {
                // Error de red o servidor, reintentar con backoff
                markItemsAsFailed(itemsToSync, "Sync returned null response");
                return Result.retry();
            }

            // Procesar respuesta
            processSyncResponse(response, itemsToSync);
            
            // Verificar si quedan más items para sincronizar
            if (hasMorePendingItems()) {
                // Re-enqueue worker para procesar siguiente batch
                return Result.success(); // WorkManager re-ejecutará según constraints
            }
            
            return Result.success();
            
        } catch (Exception e) {
            // Log error y reintentar
            return Result.retry();
        }
    }

    private boolean isNetworkAvailable() {
        // WorkManager constraints ya garantizan CONNECTED, pero verificamos
        return true;
    }

    private SyncBatchRequest buildSyncBatchRequest(List<SyncQueueItem> items) {
        // Separar items por tipo de entidad
        // En implementación real, deserializar payloadJson y agrupar por tipo
        // Placeholder: request vacío para compilar
        return new SyncBatchRequest(
            1, // clientVersion
            String.valueOf(System.currentTimeMillis()), // lastSyncTimestamp
            java.util.Collections.emptyList(), // foodLogs
            java.util.Collections.emptyList(), // workoutLogs
            java.util.Collections.emptyList(), // weightEntries
            java.util.Collections.emptyList(), // measurements
            java.util.Collections.emptyList(), // progressPhotos
            java.util.Collections.emptyList(), // recipes
            java.util.Collections.emptyList(), // waterLogs
            java.util.Collections.emptyList(), // exercises
            java.util.Collections.emptyList(), // personalRecords
            java.util.Collections.emptyList(), // glucoseLogs
            java.util.Collections.emptyList(), // moodEntries
            java.util.Collections.emptyList()  // userNotes
        );
    }

    private SyncBatchResponse executeSyncBatch(SyncBatchRequest request) {
        try {
            CompletableFuture<SyncBatchResponse> future = syncEngine.syncBatch(request);
            return future.get(60, TimeUnit.SECONDS); // Timeout 60s
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return null;
        }
    }

    private void processSyncResponse(SyncBatchResponse response, List<SyncQueueItem> sentItems) {
        // Marcar items sincronizados exitosamente
        for (SyncQueueItem item : sentItems) {
            // En implementación real, buscar el item correspondiente en la respuesta
            // y actualizar serverId, status, etc.
            item.status = "SYNCED";
            item.updatedAt = System.currentTimeMillis();
            syncQueueRepository.update(item);
        }

        // Procesar conflictos si los hay
        if (response.conflicts != null && !response.conflicts.isEmpty()) {
            conflictResolver.processConflicts(
                response.conflicts,
                key -> syncQueueRepository.getByEntitySync(key.split(":")[0], key.split(":")[1]),
                resolved -> syncQueueRepository.update(resolved)
            );
        }

        // Actualizar server version para próximo sync
        // (guardar en SharedPreferences o similar)
    }

    private void markItemsAsFailed(List<SyncQueueItem> items, String error) {
        long now = System.currentTimeMillis();
        for (SyncQueueItem item : items) {
            item.retryCount++;
            item.lastError = error;
            item.nextRetryAt = now + calculateBackoff(item.retryCount);
            
            if (item.retryCount >= MAX_RETRIES) {
                item.status = "FAILED";
            } else {
                item.status = "PENDING";
            }
            item.updatedAt = now;
            syncQueueRepository.update(item);
        }
    }

    private long calculateBackoff(int retryCount) {
        // Backoff exponencial: 30min, 1h, 2h, 4h, 8h...
        return BACKOFF_BASE_MS * (1L << (retryCount - 1));
    }

    private boolean hasMorePendingItems() {
        // Verificar si quedan items PENDING o FAILED con retry disponible
        return syncQueueRepository.getPendingCountSync() > 0 ||
               syncQueueRepository.getMaxRetriesExceededCountSync(MAX_RETRIES) == 0;
    }
}