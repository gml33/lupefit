package com.lupefit.core.network.dto.progress;

import com.squareup.moshi.Json;

/** Respuesta de medida corporal */
public final class MeasurementResponse {
    @Json(name = "id")
    public final String id;

    @Json(name = "user_id")
    public final String userId;

    @Json(name = "chest_cm")
    public final Double chestCm;

    @Json(name = "waist_cm")
    public final Double waistCm;

    @Json(name = "hip_cm")
    public final Double hipCm;

    @Json(name = "bicep_cm")
    public final Double bicepCm;

    @Json(name = "thigh_cm")
    public final Double thighCm;

    @Json(name = "calf_cm")
    public final Double calfCm;

    @Json(name = "neck_cm")
    public final Double neckCm;

    @Json(name = "forearm_cm")
    public final Double forearmCm;

    @Json(name = "shoulder_cm")
    public final Double shoulderCm;

    @Json(name = "date")
    public final String date;

    @Json(name = "notes")
    public final String notes;

    @Json(name = "is_synced")
    public final boolean isSynced;

    @Json(name = "created_at")
    public final String createdAt;

    @Json(name = "updated_at")
    public final String updatedAt;

    public MeasurementResponse(String id, String userId,
                              Double chestCm, Double waistCm, Double hipCm,
                              Double bicepCm, Double thighCm, Double calfCm,
                              Double neckCm, Double forearmCm, Double shoulderCm,
                              String date, String notes, boolean isSynced,
                              String createdAt, String updatedAt) {
        this.id = id;
        this.userId = userId;
        this.chestCm = chestCm;
        this.waistCm = waistCm;
        this.hipCm = hipCm;
        this.bicepCm = bicepCm;
        this.thighCm = thighCm;
        this.calfCm = calfCm;
        this.neckCm = neckCm;
        this.forearmCm = forearmCm;
        this.shoulderCm = shoulderCm;
        this.date = date;
        this.notes = notes;
        this.isSynced = isSynced;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}