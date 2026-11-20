package com.lupefit.core.network.dto.user;

import com.squareup.moshi.Json;

/** Request para actualizar configuración */
public final class UserSettingsRequest {
    @Json(name = "theme")
    public final String theme;

    @Json(name = "language")
    public final String language;

    @Json(name = "units")
    public final String units;

    @Json(name = "notifications_enabled")
    public final Boolean notificationsEnabled;

    @Json(name = "water_reminder_enabled")
    public final Boolean waterReminderEnabled;

    @Json(name = "water_reminder_interval_min")
    public final Integer waterReminderIntervalMin;

    @Json(name = "meal_reminder_enabled")
    public final Boolean mealReminderEnabled;

    @Json(name = "workout_reminder_enabled")
    public final Boolean workoutReminderEnabled;

    @Json(name = "sync_on_wifi_only")
    public final Boolean syncOnWifiOnly;

    @Json(name = "auto_sync_enabled")
    public final Boolean autoSyncEnabled;

    @Json(name = "health_connect_enabled")
    public final Boolean healthConnectEnabled;

    public UserSettingsRequest(String theme, String language, String units,
                              Boolean notificationsEnabled, Boolean waterReminderEnabled,
                              Integer waterReminderIntervalMin, Boolean mealReminderEnabled,
                              Boolean workoutReminderEnabled, Boolean syncOnWifiOnly,
                              Boolean autoSyncEnabled, Boolean healthConnectEnabled) {
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