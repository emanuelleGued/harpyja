package com.project.harpyja.service.user.organization;


import com.project.harpyja.model.UserOrganization;

import java.util.UUID;

public interface UserOrganizationService {
    
    void createUserOrganizationService(UserOrganization userOrg);

    boolean existsByUserAndOrganization(UUID userId, UUID organizationId);
}
