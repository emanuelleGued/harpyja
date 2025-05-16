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
        // Se a entidade já tiver ID, ótimo; senão gere aqui
        if (organization.getId() == null) {
            organization.setId(UUID.fromString(UUID.randomUUID().toString()));
        }
        entityManager.persist(organization);
        return organization;
    }

    @Override
    public void updateOrganizationRepository(String id, Organization updates) {
        Organization existing = entityManager.find(Organization.class, UUID.fromString(id));
        if (existing == null) {
            throw new RuntimeException("Organization not found with ID: " + id);
        }

        // Atualiza os campos necessários:
        if (updates.getName() != null) {
            existing.setName(updates.getName());
        }
        if (updates.getStatus() != null) {
            existing.setStatus(updates.getStatus());
        }
        if (updates.getBusinessSize() != null) {
            existing.setBusinessSize(updates.getBusinessSize());
        }
        // etc.

        // O entityManager vai “flushar” quando a transação terminar
    }

    @Override
    public Organization getOrganizationByUserID(String userID) {
        // No Go code, você dava um SELECT em user_organization para achar a org.

        // Exemplo de JPQL (assumindo que existe a tabela user_organization
        // e a org ID está linkada):
        String jpql = """
                SELECT o 
                FROM Organization o
                JOIN UserOrganization uo ON o.id = uo.organizationId
                WHERE uo.userId = :userId
                """;

        return entityManager.createQuery(jpql, Organization.class)
                .setParameter("userId", UUID.fromString(userID))
                .getResultList() // ou getSingleResult()
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Optional<Organization> findById(UUID id) {
        Organization org = entityManager.find(Organization.class, id);
        return Optional.ofNullable(org);
    }
}
