package com.project.harpyja.entity;

import com.project.harpyja.model.enums.OrganizationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "organizations")
public class Organization {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(length = 255)
    private String name;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private OrganizationStatus status;

    @Column(name = "business_size")
    private String businessSize;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserOrganization> users;

    public Organization() {
    }

    public Organization(String id, String name, LocalDateTime registrationDate, OrganizationStatus status,
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