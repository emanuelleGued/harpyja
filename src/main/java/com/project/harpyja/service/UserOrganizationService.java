package com.project.harpyja.service;


import com.project.harpyja.model.UserOrganization;

public interface UserOrganizationService {

    /**
     * Cria uma relação entre usuário e organização no sistema.
     * @param userOrg objeto contendo userId, organizationId e demais campos necessários.
     */
    void createUserOrganizationService(UserOrganization userOrg);
}
