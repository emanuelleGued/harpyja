package com.project.harpyja.repository;


import com.project.harpyja.model.UserOrganization;

public interface UserOrganizationRepository {

    /**
     * Cria um novo registro userOrganization no banco.
     */
    UserOrganization createUserOrganizationRepository(UserOrganization userOrganization);
}
