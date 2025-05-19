package com.project.harpyja.service.user.organization;


import com.project.harpyja.entity.UserOrganization;

public interface UserOrganizationService {

    void createUserOrganizationService(UserOrganization userOrg);

    boolean existsByUserAndOrganization(String userId, String organizationId);
}
