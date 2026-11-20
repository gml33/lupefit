package com.lupefit.core.sync.worker;

/**
 * Interfaz que debe implementar la Application para proporcionar el factory del SyncWorker.
 * Esto evita dependencias circulares entre core-sync y app.
 */
public interface SyncWorkerFactoryProvider {

    SyncWorkerFactory getSyncWorkerFactory();
}