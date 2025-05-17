package com.project.harpyja.repository.user.organization;


import com.project.harpyja.entity.UserOrganization;

public interface UserOrganizationRepository {

    /**
     * Cria um novo registro userOrganization no banco.
     */
    UserOrganization createUserOrganizationRepository(UserOrganization userOrganization);
}
