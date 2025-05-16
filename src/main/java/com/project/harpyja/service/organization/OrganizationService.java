package com.project.harpyja.service.organization;

import com.project.harpyja.entity.Organization;

public interface OrganizationService {

    Organization createOrganizationService(Organization organization);

    void updateOrganizationService(String id, Organization updates);

    Organization findById(String id);
}
