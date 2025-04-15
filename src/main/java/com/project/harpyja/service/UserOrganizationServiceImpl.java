package com.project.harpyja.service;

import com.project.harpyja.model.UserOrganization;
import com.project.harpyja.repository.UserOrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserOrganizationServiceImpl implements UserOrganizationService {

    @Autowired
    private UserOrganizationRepository userOrganizationRepository;

    @Override
    public void createUserOrganizationService(UserOrganization userOrg) {
        UserOrganization created = userOrganizationRepository.createUserOrganizationRepository(userOrg);
        System.out.println("UserOrganization criado com sucesso: " + created);
    }
}
