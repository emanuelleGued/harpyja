package com.project.harpyja.service.organization;

import com.project.harpyja.entity.Organization;
import com.project.harpyja.repository.organization.OrganizationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Transactional
    public Organization createOrganizationService(Organization organization) {
        organization.setId(UUID.randomUUID().toString());
        return organizationRepository.createOrganizationRepository(organization);
    }

    public void updateOrganizationService(String id, Organization updates) {
        organizationRepository.updateOrganizationRepository(id, updates);
    }

    @Override
    public Organization findById(String id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found with id: " + id));
    }
}
