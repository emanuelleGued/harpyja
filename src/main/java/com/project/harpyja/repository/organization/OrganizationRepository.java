package com.project.harpyja.repository.organization;


import com.project.harpyja.entity.Organization;

import java.util.Optional;

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

    Optional<Organization> findById(String id);
}
