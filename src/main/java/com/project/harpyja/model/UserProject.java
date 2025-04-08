package com.project.harpyja.model;

import com.project.harpyja.model.enums.ProjectRole;
import jakarta.persistence.*;

@Entity
@Table(name = "user_project")
public class UserProject {

    @EmbeddedId
    private UserProjectId id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private ProjectRole role;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("projectId")
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public UserProject() {
    }

    public UserProject(UserProjectId id, ProjectRole role, User user, Project project) {
        this.id = id;
        this.role = role;
        this.user = user;
        this.project = project;
    }

    // Getters e Setters
    public UserProjectId getId() {
        return id;
    }
    public void setId(UserProjectId id) {
        this.id = id;
    }

    public ProjectRole getRole() {
        return role;
    }
    public void setRole(ProjectRole role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }
    public void setProject(Project project) {
        this.project = project;
    }
}
