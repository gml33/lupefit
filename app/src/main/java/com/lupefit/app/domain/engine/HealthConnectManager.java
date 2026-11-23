package com.lupefit.app.domain.engine;

import android.content.Context;
import androidx.health.connect.client.HealthConnectClient;
import androidx.health.connect.client.PermissionController;
import androidx.health.connect.client.permission.HealthPermission;
import androidx.health.connect.client.records.ExerciseSessionRecord;
import androidx.health.connect.client.records.Record;
import java.util.Collections;
import java.util.Set;
import kotlin.jvm.JvmClassMappingKt;

/** Gestor de Health Connect - stub para compilación base. */
public class HealthConnectManager {
    public static final String PROVIDER_PACKAGE = "com.google.android.apps.healthdata";
    private final Context context;
    private final HealthConnectClient client;

    public HealthConnectManager(Context context) {
        this.context = context.getApplicationContext();
        this.client = isAvailable(this.context) ? HealthConnectClient.getOrCreate(this.context) : null;
    }

    public boolean isAvailable() { return client != null; }

    public HealthConnectClient getClient() { return client; }

    public Set<String> requiredPermissions() {
        if (client == null) return Collections.emptySet();
        return Collections.singleton(HealthPermission.getReadPermission(JvmClassMappingKt.getKotlinClass(ExerciseSessionRecord.class)));
    }

    public androidx.activity.result.contract.ActivityResultContract<java.util.Set<String>, java.util.Set<String>> permissionContract() {
        return PermissionController.createRequestPermissionResultContract();
    }

    public static boolean isAvailable(Context context) {
        int status = HealthConnectClient.getSdkStatus(context, PROVIDER_PACKAGE);
        return status == HealthConnectClient.SDK_AVAILABLE;
    }
}
