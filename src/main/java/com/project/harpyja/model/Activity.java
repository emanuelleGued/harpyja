package com.project.harpyja.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "activities")
public class Activity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "activity_name")
    private String activityName;

    // Caso você prefira armazenar diretamente o UUID, sem criar outra coluna para isso:
    // @Column(name = "session_id")
    // private UUID sessionId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "session_id", referencedColumnName = "id")
    private SessionEntity session;

    // Construtores
    public Activity() {
    }

    public Activity(UUID id, String activityName, LocalDateTime createdAt, LocalDateTime updatedAt, SessionEntity session) {
        this.id = id;
        this.activityName = activityName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.session = session;
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public SessionEntity getSession() {
        return session;
    }
    public void setSession(SessionEntity session) {
        this.session = session;
    }
}
