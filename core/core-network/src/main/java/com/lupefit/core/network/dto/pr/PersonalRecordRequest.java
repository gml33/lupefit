package com.lupefit.core.network.dto.pr;

import com.squareup.moshi.Json;

/** Request para Personal Record */
public final class PersonalRecordRequest {
    @Json(name = "exercise_id")
    public final String exerciseId;

    @Json(name = "type")
    public final String type;

    @Json(name = "value")
    public final double value;

    @Json(name = "unit")
    public final String unit;

    @Json(name = "date")
    public final String date;

    @Json(name = "workout_log_id")
    public final String workoutLogId;

    @Json(name = "notes")
    public final String notes;

    public PersonalRecordRequest(String exerciseId, String type, double value,
                                String unit, String date, String workoutLogId,
                                String notes) {
        this.exerciseId = exerciseId;
        this.type = type;
        this.value = value;
        this.unit = unit;
        this.date = date;
        this.workoutLogId = workoutLogId;
        this.notes = notes;
    }
}