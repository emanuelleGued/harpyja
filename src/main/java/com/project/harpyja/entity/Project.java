package com.project.harpyja.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    private String name;

    private String key;

    private String type;

    @Column(name = "expiration")
    private LocalDateTime expiration;

    // Indica a FK da Organization
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    // Relacionamento para tabela de associação UserProject
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserProject> users;

    public Project() {
    }

    public Project(UUID id, String name, String key, String type, LocalDateTime expiration,
                   Organization organization, List<UserProject> users) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.type = type;
        this.expiration = expiration;
        this.organization = organization;
        this.users = users;
    }

}
