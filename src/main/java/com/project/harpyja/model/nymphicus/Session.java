package com.project.harpyja.model.nymphicus;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Setter
@Getter
public class Session {
    private String id;
    private ActivityGestureLogs activities;
    private Device device;
    @Field("videourl")
    private String videoUrl;
    private String status;
    private Instant createdAt;
    private String key;
    private long duration;

    public Session() {
    }

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
}
