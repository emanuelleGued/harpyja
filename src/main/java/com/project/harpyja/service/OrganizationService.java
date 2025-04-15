package com.project.harpyja.service;

import com.project.harpyja.model.Organization;

public interface OrganizationService {

    Organization createOrganizationService(Organization organization);
    void updateOrganizationService(String id, Organization updates);
    Organization getOrganizationByUserIDService(String userId);

    /**
     * Novo método para buscar a Organization pelo ID (UUID em string).
     */
    Organization findById(String id);
}
