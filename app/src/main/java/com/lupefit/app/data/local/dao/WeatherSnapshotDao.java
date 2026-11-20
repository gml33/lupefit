package com.lupefit.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lupefit.app.data.local.entity.WeatherSnapshot;

import java.util.List;

@Dao
public interface WeatherSnapshotDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(WeatherSnapshot snapshot);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<WeatherSnapshot> snapshots);

    @Update
    int update(WeatherSnapshot snapshot);

    @Query("DELETE FROM weather_snapshots WHERE id = :id")
    int deleteById(long id);

    @Query("SELECT * FROM weather_snapshots WHERE dateKey = :dateKey ORDER BY timestamp DESC LIMIT 1")
    LiveData<WeatherSnapshot> getLatestByDate(int dateKey);

    @Query("SELECT * FROM weather_snapshots WHERE dateKey BETWEEN :startDateKey AND :endDateKey ORDER BY timestamp")
    LiveData<List<WeatherSnapshot>> getByDateRange(int startDateKey, int endDateKey);

    @Query("SELECT AVG(temperatureCelsius) FROM weather_snapshots WHERE dateKey BETWEEN :startDateKey AND :endDateKey")
    LiveData<Double> getAvgTemperatureInRange(int startDateKey, int endDateKey);
}