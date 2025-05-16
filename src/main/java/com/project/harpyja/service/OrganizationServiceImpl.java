package com.project.harpyja.service;

import com.project.harpyja.model.Organization;
import com.project.harpyja.repository.OrganizationRepository;
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
        // Gera um UUID para a organização (caso não esteja definido)
        organization.setId(UUID.fromString(UUID.randomUUID().toString()));

        // Chama o método do repositório que persiste a entidade
        Organization created = organizationRepository.createOrganizationRepository(organization);
        return created;
    }

    public void updateOrganizationService(String id, Organization updates) {
        // Chama o repositório para aplicar as atualizações
        organizationRepository.updateOrganizationRepository(id, updates);
    }

    public Organization getOrganizationByUserIDService(String userId) {
        // Retorna a organização vinculada a um usuário específico
        return organizationRepository.getOrganizationByUserID(userId);
    }

    /**
     * Busca a entidade Organization por ID (string).
     * Lança exceção se não encontrado.
     */
    @Override
    public Organization findById(String id) {
        // Podemos criar um método "findById" no repositório ou mesclar com JPA nativo
        // Aqui, assumindo que iremos criar no repositorio algo como:
        return organizationRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Organization not found with id: " + id));
    }
}
