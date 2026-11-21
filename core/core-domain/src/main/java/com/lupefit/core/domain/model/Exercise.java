package com.lupefit.core.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Exercise implements Parcelable {

    private long id;
    private String name;
    private String category;
    private String movementPattern;
    private String equipment;
    private boolean isBodyweight;
    private boolean isCustom;
    private long createdByUserId;
    private double defaultIncrementKg;
    private String description;
    private String videoUrl;
    private String imageUrl;
    private String serverId;
    private boolean isSynced;
    private String syncAction;
    private long lastModified;

    public Exercise() {}

    protected Exercise(Parcel in) {
        id = in.readLong();
        name = in.readString();
        category = in.readString();
        movementPattern = in.readString();
        equipment = in.readString();
        isBodyweight = in.readByte() != 0;
        isCustom = in.readByte() != 0;
        createdByUserId = in.readLong();
        defaultIncrementKg = in.readDouble();
        description = in.readString();
        videoUrl = in.readString();
        imageUrl = in.readString();
        serverId = in.readString();
        isSynced = in.readByte() != 0;
        syncAction = in.readString();
        lastModified = in.readLong();
    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getMovementPattern() { return movementPattern; }
    public void setMovementPattern(String movementPattern) { this.movementPattern = movementPattern; }

    public String getEquipment() { return equipment; }
    public void setEquipment(String equipment) { this.equipment = equipment; }

    public boolean isBodyweight() { return isBodyweight; }
    public void setBodyweight(boolean bodyweight) { isBodyweight = bodyweight; }

    public boolean isCustom() { return isCustom; }
    public void setCustom(boolean custom) { isCustom = custom; }

    public long getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(long createdByUserId) { this.createdByUserId = createdByUserId; }

    public double getDefaultIncrementKg() { return defaultIncrementKg; }
    public void setDefaultIncrementKg(double defaultIncrementKg) { this.defaultIncrementKg = defaultIncrementKg; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getServerId() { return serverId; }
    public void setServerId(String serverId) { this.serverId = serverId; }

    public boolean isSynced() { return isSynced; }
    public void setSynced(boolean synced) { isSynced = synced; }

    public String getSyncAction() { return syncAction; }
    public void setSyncAction(String syncAction) { this.syncAction = syncAction; }

    public long getLastModified() { return lastModified; }
    public void setLastModified(long lastModified) { this.lastModified = lastModified; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(category);
        dest.writeString(movementPattern);
        dest.writeString(equipment);
        dest.writeByte((byte) (isBodyweight ? 1 : 0));
        dest.writeByte((byte) (isCustom ? 1 : 0));
        dest.writeLong(createdByUserId);
        dest.writeDouble(defaultIncrementKg);
        dest.writeString(description);
        dest.writeString(videoUrl);
        dest.writeString(imageUrl);
        dest.writeString(serverId);
        dest.writeByte((byte) (isSynced ? 1 : 0));
        dest.writeString(syncAction);
        dest.writeLong(lastModified);
    }
}