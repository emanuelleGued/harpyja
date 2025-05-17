package com.project.harpyja.service.user.organization;

import com.project.harpyja.entity.UserOrganization;
import com.project.harpyja.repository.user.organization.UserOrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Primary
public class UserOrganizationServiceImplJPA implements UserOrganizationService {

    @Autowired
    private UserOrganizationRepository userOrganizationRepository;

    @Override
    @Transactional
    public void createUserOrganizationService(UserOrganization userOrg) {
        UserOrganization created = userOrganizationRepository.createUserOrganizationRepository(userOrg);
        System.out.println("UserOrganization criado com sucesso: " + created);
    }

    @Override
    public boolean existsByUserAndOrganization(UUID userId, UUID organizationId) {
        return false;
    }
}
