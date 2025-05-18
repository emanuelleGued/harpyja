package com.project.harpyja.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "roles")
public class Role {

    // Getters e Setters
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    private String name;

    private String description;

    private String type;

    @Column(name = "default_project_role_id")
    private String defaultProjectRoleId;

    public Role() {
    }

    public Role(UUID id, String name, String description, String type, String defaultProjectRoleId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.defaultProjectRoleId = defaultProjectRoleId;
    }

}
