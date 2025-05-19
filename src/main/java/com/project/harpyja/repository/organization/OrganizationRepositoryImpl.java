package com.project.harpyja.repository.organization;

import com.project.harpyja.entity.Organization;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class OrganizationRepositoryImpl implements OrganizationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Organization createOrganizationRepository(Organization organization) {
        if (organization.getId() == null) {
            organization.setId(UUID.randomUUID().toString());
        }
        entityManager.persist(organization);
        return organization;
    }

    @Override
    public void updateOrganizationRepository(String id, Organization updates) {
        Organization existing = entityManager.find(Organization.class, id);
        if (existing == null) {
            throw new RuntimeException("Organization not found with ID: " + id);
        }

        if (updates.getName() != null) {
            existing.setName(updates.getName());
        }
        if (updates.getStatus() != null) {
            existing.setStatus(updates.getStatus());
        }
        if (updates.getBusinessSize() != null) {
            existing.setBusinessSize(updates.getBusinessSize());
        }
    }

    @Override
    public Optional<Organization> findById(String id) {
        Organization org = entityManager.find(Organization.class, id);
        return Optional.ofNullable(org);
    }
}
