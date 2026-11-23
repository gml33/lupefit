package com.lupefit.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "progress_measurements")
public class MeasurementEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;
    @NonNull public String kind = "";
    public double centimeters;
    public long timestamp;
}
