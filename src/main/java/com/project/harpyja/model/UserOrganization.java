package com.project.harpyja.model;


import jakarta.persistence.*;

import com.project.harpyja.model.enums.OrganizationRole;
import com.project.harpyja.model.enums.ProjectRole;

import java.io.Serializable;

@Entity
@Table(name = "user_organization")
public class UserOrganization implements Serializable {

    @EmbeddedId
    private UserOrganizationId id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private OrganizationRole role;

    /**
     * Campo não persistido, caso você queira apenas usar em lógicas de negócio.
     * Se quiser salvar no banco, troque para @Column ou relacione de outra forma.
     */
    @Transient
    private ProjectRole defaultProjectRole;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("organizationId")
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    public UserOrganization() {
    }

    public UserOrganization(UserOrganizationId id, OrganizationRole role, ProjectRole defaultProjectRole,
                            User user, Organization organization) {
        this.id = id;
        this.role = role;
        this.defaultProjectRole = defaultProjectRole;
        this.user = user;
        this.organization = organization;
    }

    // Getters e Setters
    public UserOrganizationId getId() {
        return id;
    }
    public void setId(UserOrganizationId id) {
        this.id = id;
    }

    public OrganizationRole getRole() {
        return role;
    }
    public void setRole(OrganizationRole role) {
        this.role = role;
    }

    public ProjectRole getDefaultProjectRole() {
        return defaultProjectRole;
    }
    public void setDefaultProjectRole(ProjectRole defaultProjectRole) {
        this.defaultProjectRole = defaultProjectRole;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Organization getOrganization() {
        return organization;
    }
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}

