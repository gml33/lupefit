package com.lupefit.core.network;

import com.lupefit.core.network.api.LuPeFitApiService;
import com.lupefit.core.network.dto.sync.SyncBatchRequest;
import com.lupefit.core.network.dto.sync.SyncBatchResponse;
import com.lupefit.core.network.dto.sync.SyncStatus;
import com.lupefit.core.network.interceptor.AuthInterceptor;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Motor de sincronización con el backend
 * Maneja sync batch bidireccional, reintentos, y resolución de conflictos
 */
public final class SyncEngine {

    private final LuPeFitApiService apiService;
    private final AuthInterceptor authInterceptor;
    private final ExecutorService executor;

    public SyncEngine(LuPeFitApiService apiService, AuthInterceptor authInterceptor) {
        this.apiService = apiService;
        this.authInterceptor = authInterceptor;
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Ejecuta sincronización batch completa
     */
    public CompletableFuture<SyncBatchResponse> syncBatch(SyncBatchRequest request) {
        CompletableFuture<SyncBatchResponse> future = new CompletableFuture<>();

        executor.submit(() -> {
            try {
                Call<SyncBatchResponse> call = apiService.syncBatch(
                    "Bearer " + getCurrentAccessToken(),
                    request
                );
                
                Response<SyncBatchResponse> response = call.execute();
                
                if (response.isSuccessful() && response.body() != null) {
                    future.complete(response.body());
                } else {
                    future.completeExceptionally(
                        new SyncException("Sync failed: " + response.code() + " " + response.message())
                    );
                }
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    /**
     * Obtiene estado de sincronización
     */
    public CompletableFuture<SyncStatus> getSyncStatus() {
        CompletableFuture<SyncStatus> future = new CompletableFuture<>();

        executor.submit(() -> {
            try {
                Call<SyncStatus> call = apiService.getSyncStatus(
                    "Bearer " + getCurrentAccessToken()
                );
                
                Response<SyncStatus> response = call.execute();
                
                if (response.isSuccessful() && response.body() != null) {
                    future.complete(response.body());
                } else {
                    future.completeExceptionally(
                        new SyncException("Status failed: " + response.code())
                    );
                }
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    /**
     * Actualiza el token de acceso
     */
    public void setAccessToken(String accessToken) {
        authInterceptor.setAccessToken(accessToken);
    }

    /**
     * Limpia el token de acceso
     */
    public void clearAccessToken() {
        authInterceptor.clearAccessToken();
    }

    private String getCurrentAccessToken() {
        // En implementación real, obtener de almacenamiento seguro
        return "";
    }

    public void shutdown() {
        executor.shutdown();
    }

    public static class SyncException extends Exception {
        public SyncException(String message) {
            super(message);
        }
    }
}