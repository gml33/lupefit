package com.lupefit.core.network.dto.user;

import com.squareup.moshi.Json;

/** Configuración de usuario */
public final class UserSettingsResponse {
    @Json(name = "theme")
    public final String theme; // "LIGHT" | "DARK" | "SYSTEM"

    @Json(name = "language")
    public final String language; // "es-AR"

    @Json(name = "units")
    public final String units; // "METRIC" | "IMPERIAL"

    @Json(name = "notifications_enabled")
    public final boolean notificationsEnabled;

    @Json(name = "water_reminder_enabled")
    public final boolean waterReminderEnabled;

    @Json(name = "water_reminder_interval_min")
    public final int waterReminderIntervalMin;

    @Json(name = "meal_reminder_enabled")
    public final boolean mealReminderEnabled;

    @Json(name = "workout_reminder_enabled")
    public final boolean workoutReminderEnabled;

    @Json(name = "sync_on_wifi_only")
    public final boolean syncOnWifiOnly;

    @Json(name = "auto_sync_enabled")
    public final boolean autoSyncEnabled;

    @Json(name = "health_connect_enabled")
    public final boolean healthConnectEnabled;

    public UserSettingsResponse(String theme, String language, String units,
                               boolean notificationsEnabled, boolean waterReminderEnabled,
                               int waterReminderIntervalMin, boolean mealReminderEnabled,
                               boolean workoutReminderEnabled, boolean syncOnWifiOnly,
                               boolean autoSyncEnabled, boolean healthConnectEnabled) {
        this.theme = theme;
        this.language = language;
        this.units = units;
        this.notificationsEnabled = notificationsEnabled;
        this.waterReminderEnabled = waterReminderEnabled;
        this.waterReminderIntervalMin = waterReminderIntervalMin;
        this.mealReminderEnabled = mealReminderEnabled;
        this.workoutReminderEnabled = workoutReminderEnabled;
        this.syncOnWifiOnly = syncOnWifiOnly;
        this.autoSyncEnabled = autoSyncEnabled;
        this.healthConnectEnabled = healthConnectEnabled;
    }
}