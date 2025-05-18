package com.project.harpyja.entity;

import com.project.harpyja.model.enums.OrganizationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "organizations")
public class Organization {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(length = 255)
    private String name;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private OrganizationStatus status;

    @Column(name = "business_size")
    private String businessSize;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserOrganization> users;

    public Organization() {
    }

    public Organization(UUID id, String name, LocalDateTime registrationDate, OrganizationStatus status,
                        String businessSize, List<Project> projects, List<UserOrganization> users) {
        this.id = id;
        this.name = name;
        this.registrationDate = registrationDate;
        this.status = status;
        this.businessSize = businessSize;
        this.projects = projects;
        this.users = users;
    }

}
