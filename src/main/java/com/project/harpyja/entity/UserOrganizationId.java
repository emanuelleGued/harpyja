package com.project.harpyja.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Embeddable
public class UserOrganizationId implements Serializable {

    @Column(name = "user_id")
    private String userId;

    @Column(name = "organization_id")
    private String organizationId;

    public UserOrganizationId() {
    }

    public UserOrganizationId(String userId, String organizationId) {
        this.userId = userId;
        this.organizationId = organizationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserOrganizationId that)) return false;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(organizationId, that.organizationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, organizationId);
    }
}