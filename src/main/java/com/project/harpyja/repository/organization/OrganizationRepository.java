package com.project.harpyja.repository.organization;


import com.project.harpyja.entity.Organization;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository {

    /**
     * Cria uma nova organização no banco.
     */
    Organization createOrganizationRepository(Organization organization);

    /**
     * Atualiza uma organização com base no ID.
     */
    void updateOrganizationRepository(String id, Organization updates);

    /**
     * Busca a organização associada a determinado userID.
     */
    Organization getOrganizationByUserID(String userID);

    Optional<Organization> findById(UUID id);

}
