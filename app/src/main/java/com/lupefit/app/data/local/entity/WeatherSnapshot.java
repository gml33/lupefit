package com.lupefit.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Snapshot del clima para correlacionar con rendimiento/humor.
 */
@Entity(tableName = "weather_snapshots", indices = {
    @Index(value = {"dateKey"}),
    @Index(value = {"timestamp"})
})
public class WeatherSnapshot {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public long timestamp;

    @NonNull
    public int dateKey;

    public double temperatureCelsius;
    public double humidityPercent;
    public double pressureHpa;
    public double windSpeedKmh;
    public int conditionCode; // códigos OpenWeatherMap
    @NonNull
    public String conditionDescription; // "Clear sky", "Light rain", etc.

    public double uvIndex;
    public double precipitationMm;

    // Offline-first
    public boolean isSynced = false;
    @NonNull
    public String syncAction = "CREATE";
    public long lastModified = System.currentTimeMillis();
    public String serverId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeatherSnapshot)) return false;
        WeatherSnapshot that = (WeatherSnapshot) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}