package com.project.harpyja.repository.user.organization;

import com.project.harpyja.entity.UserOrganization;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class UserOrganizationRepositoryImpl implements UserOrganizationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserOrganization createUserOrganizationRepository(UserOrganization userOrganization) {
        // Gera ID, se for o caso
        if (userOrganization.getId() == null) {
            throw new IllegalArgumentException("UserOrganizationId cannot be null. " +
                    "Please set userId and organizationId before persisting.");
        }

        entityManager.persist(userOrganization);
        return userOrganization;
    }
}
