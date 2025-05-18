package com.project.harpyja.entity;

import com.project.harpyja.model.enums.ProjectRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}
