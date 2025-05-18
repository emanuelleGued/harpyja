package com.project.harpyja.entity;


import jakarta.persistence.*;

import com.project.harpyja.model.enums.OrganizationRole;
import com.project.harpyja.model.enums.ProjectRole;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "user_organization")
public class UserOrganization implements Serializable {

    // Getters e Setters
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

}

