package com.project.harpyja.model;

import com.project.harpyja.model.enums.OrganizationStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
    @Column(name = "status")
    private OrganizationStatus status;

    @Column(name = "business_size")
    private String businessSize;

    /**
     * Caso queira manter a relação 1-para-1 com Address (como está descrito em Address.java):
     * Neste caso, a entity Address controlaria a FK (com @JoinColumn(unique=true)).
     * Você pode remover este campo e acessar `Address` via `address.getOrganization()`.
     *
     * MAS, se quiser que a Organization seja "dona" do relacionamento, você pode
     * deixar a FK aqui. Então precisaria ajustar em Address para não usar @OneToOne
     * ou usar `mappedBy`.
     */

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

    // Getters e Setters
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }
    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public OrganizationStatus getStatus() {
        return status;
    }
    public void setStatus(OrganizationStatus status) {
        this.status = status;
    }

    public String getBusinessSize() {
        return businessSize;
    }
    public void setBusinessSize(String businessSize) {
        this.businessSize = businessSize;
    }

    public List<Project> getProjects() {
        return projects;
    }
    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<UserOrganization> getUsers() {
        return users;
    }
    public void setUsers(List<UserOrganization> users) {
        this.users = users;
    }
}
