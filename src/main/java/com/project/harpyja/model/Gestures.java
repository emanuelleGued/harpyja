package com.project.harpyja.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "gestures")
public class Gestures {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    // Você pode usar LocalDateTime em vez de String para targetTime e createdAt
    @Column(name = "target_time")
    private String targetTime;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "coordinates")
    private String coordinates;

    @ManyToOne
    @JoinColumn(name = "activity_id", referencedColumnName = "id")
    private Activity activity;

    public Gestures() {
    }

    public Gestures(UUID id, String targetTime, String createdAt, String coordinates, Activity activity) {
        this.id = id;
        this.targetTime = targetTime;
        this.createdAt = createdAt;
        this.coordinates = coordinates;
        this.activity = activity;
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String getTargetTime() {
        return targetTime;
    }
    public void setTargetTime(String targetTime) {
        this.targetTime = targetTime;
    }

    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public Activity getActivity() {
        return activity;
    }
    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
