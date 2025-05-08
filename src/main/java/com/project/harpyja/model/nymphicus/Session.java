package com.project.harpyja.model.nymphicus;

import java.time.Instant;

public class Session {
    private String id;
    private ActivityGestureLogs activities;
    private Device device;
    private String videoUrl;
    private String status;
    private Instant createdAt;
    private String key;
    private long duration;

    public Session() {}

    public Session(String id, ActivityGestureLogs activities, Device device,
                   String videoUrl, String status, Instant createdAt,
                   String key, long duration) {
        this.id = id;
        this.activities = activities;
        this.device = device;
        this.videoUrl = videoUrl;
        this.status = status;
        this.createdAt = createdAt;
        this.key = key;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public ActivityGestureLogs getActivities() {
        return activities;
    }
    public void setActivities(ActivityGestureLogs activities) {
        this.activities = activities;
    }

    public Device getDevice() {
        return device;
    }
    public void setDevice(Device device) {
        this.device = device;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public long getDuration() {
        return duration;
    }
    public void setDuration(long duration) {
        this.duration = duration;
    }
}
