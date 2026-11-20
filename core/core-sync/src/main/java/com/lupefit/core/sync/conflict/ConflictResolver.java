package com.lupefit.core.sync.conflict;

import com.lupefit.core.network.dto.sync.SyncConflict;
import com.lupefit.core.sync.model.SyncQueueItem;

import java.util.List;
import java.util.function.Function;

/**
 * Estrategias de resolución de conflictos para sincronización.
 * 
 * Políticas disponibles:
 * - SERVER_WINS (0): Siempre gana el servidor, descarta cambios locales
 * - CLIENT_WINS (1): Siempre gana el cliente, sobrescribe servidor
 * - MERGE (2): Intenta merge inteligente campo por campo
 * - MANUAL (3): Requiere intervención del usuario
 */
public final class ConflictResolver {

    public static final int SERVER_WINS = 0;
    public static final int CLIENT_WINS = 1;
    public static final int MERGE = 2;
    public static final int MANUAL = 3;

    public ConflictResolver() {}

    /**
     * Determina la estrategia por defecto según el tipo de entidad
     */
    public static int getDefaultStrategy(String entityType) {
        switch (entityType) {
            case "USER":
            case "SETTINGS":
                return SERVER_WINS; // Configuración y usuario: servidor gana
            case "FOOD_LOG":
            case "WORKOUT_LOG":
            case "WATER_LOG":
            case "GLUCOSE_LOG":
            case "MOOD_ENTRY":
            case "USER_NOTE":
                return CLIENT_WINS; // Logs personales: cliente gana (datos subjetivos)
            case "WEIGHT_ENTRY":
            case "MEASUREMENT":
            case "PROGRESS_PHOTO":
                return SERVER_WINS; // Progreso: servidor gana (fuente de verdad)
            case "RECIPE":
            case "EXERCISE":
            case "PERSONAL_RECORD":
                return MERGE; // Contenido compartido: merge inteligente
            default:
                return SERVER_WINS;
        }
    }

    /**
     * Resuelve un conflicto aplicando la estrategia especificada
     */
    public static SyncQueueItem resolveConflict(SyncQueueItem localItem,
                                                SyncQueueItem serverItem,
                                                int strategy) {
        switch (strategy) {
            case SERVER_WINS:
                return applyServerWins(localItem, serverItem);
            case CLIENT_WINS:
                return applyClientWins(localItem, serverItem);
            case MERGE:
                return applyMerge(localItem, serverItem);
            case MANUAL:
            default:
                // Para MANUAL, marcar como conflicto y dejar que el usuario decida
                localItem.status = "CONFLICT";
                localItem.conflictResolution = MANUAL;
                localItem.mergedPayloadJson = serverItem.payloadJson;
                return localItem;
        }
    }

    private static SyncQueueItem applyServerWins(SyncQueueItem local, SyncQueueItem server) {
        // Servidor gana: actualizar local con datos del servidor
        local.serverId = server.serverId;
        local.payloadJson = server.payloadJson;
        local.status = "SYNCED";
        local.conflictResolution = SERVER_WINS;
        local.mergedPayloadJson = server.payloadJson;
        local.updatedAt = System.currentTimeMillis();
        return local;
    }

    private static SyncQueueItem applyClientWins(SyncQueueItem local, SyncQueueItem server) {
        // Cliente gana: mantener local, actualizar serverId
        local.serverId = server.serverId;
        local.status = "PENDING"; // Re-encolar para enviar al servidor
        local.conflictResolution = CLIENT_WINS;
        local.retryCount = 0;
        local.nextRetryAt = 0;
        local.lastError = "";
        local.updatedAt = System.currentTimeMillis();
        return local;
    }

    private static SyncQueueItem applyMerge(SyncQueueItem local, SyncQueueItem server) {
        // Merge inteligente: combinar campos no nulos de ambos
        // En implementación real, usar librería de diff/merge JSON
        // Por ahora, estrategia simple: cliente gana para campos editables, servidor para IDs
        local.serverId = server.serverId;
        local.status = "PENDING";
        local.conflictResolution = MERGE;
        local.mergedPayloadJson = mergePayloads(local.payloadJson, server.payloadJson);
        local.retryCount = 0;
        local.nextRetryAt = 0;
        local.lastError = "";
        local.updatedAt = System.currentTimeMillis();
        return local;
    }

    /**
     * Merge simple de payloads JSON
     * En producción, usar Jackson/Gson + librería de merge JSON
     */
    private static String mergePayloads(String clientJson, String serverJson) {
        // Placeholder: en implementación real, parsear JSON y mergear campo por campo
        // Por ahora, retornar cliente con serverId del servidor
        return clientJson; // Simplificado
    }

    /**
     * Procesa lista de conflictos del backend
     * 
     * @param conflicts Lista de conflictos del backend
     * @param entityResolver Función para resolver entidad local por tipo e ID
     * @param updateCallback Callback para actualizar entidad resuelta
     */
    public static void processConflicts(List<SyncConflict> conflicts,
                                       Function<String, SyncQueueItem> entityResolver,
                                       java.util.function.Consumer<SyncQueueItem> updateCallback) {
        for (SyncConflict conflict : conflicts) {
            SyncQueueItem localItem = entityResolver.apply(conflict.entityType + ":" + conflict.entityId);
            if (localItem != null) {
                // Crear item temporal del servidor
                SyncQueueItem serverItem = new SyncQueueItem();
                serverItem.entityType = conflict.entityType;
                serverItem.entityId = conflict.entityId;
                serverItem.serverId = conflict.mergedData != null ? conflict.entityId : null;
                serverItem.payloadJson = conflict.mergedData != null ? conflict.mergedData.toString() : "";
                serverItem.status = "SYNCED";

                int strategy = conflict.resolution != null ? 
                    parseResolution(conflict.resolution) : 
                    getDefaultStrategy(conflict.entityType);

                SyncQueueItem resolved = resolveConflict(localItem, serverItem, strategy);
                updateCallback.accept(resolved);
            }
        }
    }

    private static int parseResolution(String resolution) {
        switch (resolution.toUpperCase()) {
            case "SERVER_WINS": return SERVER_WINS;
            case "CLIENT_WINS": return CLIENT_WINS;
            case "MERGE": return MERGE;
            default: return SERVER_WINS;
        }
    }
}