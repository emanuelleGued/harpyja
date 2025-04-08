package com.project.harpyja.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sessions")
public class SessionEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "device_time")
    private LocalDateTime deviceTime;

    @Column(name = "upload_time")
    private LocalDateTime uploadTime;

    @Column(name = "recording_time")
    private LocalDateTime recordingTime;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    // Se quiser relacionar com Activity (1 Session -> N Activities):
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Activity> activities;

    public SessionEntity() {
    }

    public SessionEntity(UUID id, LocalDateTime deviceTime, LocalDateTime uploadTime, LocalDateTime recordingTime, Project project) {
        this.id = id;
        this.deviceTime = deviceTime;
        this.uploadTime = uploadTime;
        this.recordingTime = recordingTime;
        this.project = project;
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getDeviceTime() {
        return deviceTime;
    }
    public void setDeviceTime(LocalDateTime deviceTime) {
        this.deviceTime = deviceTime;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }
    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public LocalDateTime getRecordingTime() {
        return recordingTime;
    }
    public void setRecordingTime(LocalDateTime recordingTime) {
        this.recordingTime = recordingTime;
    }

    public Project getProject() {
        return project;
    }
    public void setProject(Project project) {
        this.project = project;
    }

    public java.util.List<Activity> getActivities() {
        return activities;
    }
    public void setActivities(java.util.List<Activity> activities) {
        this.activities = activities;
    }
}
