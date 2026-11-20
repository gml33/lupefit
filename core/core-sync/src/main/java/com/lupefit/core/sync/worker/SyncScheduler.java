package com.lupefit.core.sync.worker;

import android.content.Context;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

/**
 * Helper para programar y gestionar el SyncWorker
 */
public final class SyncScheduler {

    private static final String SYNC_WORK_NAME = "periodic_sync";
    private static final long SYNC_INTERVAL_MINUTES = 15;
    private static final long SYNC_FLEX_MINUTES = 5;

    private SyncScheduler() {}

    /**
     * Programa la sincronización periódica
     */
    public static void schedulePeriodicSync(Context context) {
        Constraints constraints = new Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build();

        PeriodicWorkRequest syncWork = new PeriodicWorkRequest.Builder(
            SyncWorker.class,
            SYNC_INTERVAL_MINUTES,
            TimeUnit.MINUTES,
            SYNC_FLEX_MINUTES,
            TimeUnit.MINUTES
        )
        .setConstraints(constraints)
        .setBackoffCriteria(
            androidx.work.BackoffPolicy.EXPONENTIAL,
            30, // 30 min inicial
            TimeUnit.MINUTES
        )
        .build();

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                SYNC_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                syncWork
            );
    }

    /**
     * Cancela la sincronización periódica
     */
    public static void cancelPeriodicSync(Context context) {
        WorkManager.getInstance(context)
            .cancelUniqueWork(SYNC_WORK_NAME);
    }

    /**
     * Fuerza una sincronización inmediata (one-time)
     */
    public static void triggerImmediateSync(Context context) {
        Constraints constraints = new Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build();

        androidx.work.OneTimeWorkRequest syncWork = 
            new androidx.work.OneTimeWorkRequest.Builder(SyncWorker.class)
            .setConstraints(constraints)
            .setExpedited(androidx.work.OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build();

        WorkManager.getInstance(context).enqueue(syncWork);
    }

    /**
     * Verifica si hay trabajo de sync pendiente o en ejecución
     */
    public static boolean isSyncScheduled(Context context) {
        return WorkManager.getInstance(context)
            .getWorkInfosForUniqueWorkLiveData(SYNC_WORK_NAME)
            .getValue() != null;
    }
}