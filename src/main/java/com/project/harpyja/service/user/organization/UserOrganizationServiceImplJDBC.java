package com.project.harpyja.service.user.organization;

import com.project.harpyja.model.UserOrganization;
import jakarta.transaction.Transactional;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

public class UserOrganizationServiceImplJDBC implements UserOrganizationService {

    private final JdbcTemplate jdbcTemplate;

    public UserOrganizationServiceImplJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void createUserOrganizationService(UserOrganization userOrg) {
        // Verifica se a relação já existe
        if (existsByUserAndOrganization(userOrg.getId().getUserId(), userOrg.getId().getOrganizationId())) {
            throw new IllegalStateException("User-Organization relationship already exists");
        }

        String sql = "INSERT INTO user_organization (user_id, organization_id, role) VALUES (?, ?, ?)";

        try {
            jdbcTemplate.update(sql,
                    userOrg.getId().getUserId(),
                    userOrg.getId().getOrganizationId(),
                    userOrg.getRole().toString());
        } catch (DuplicateKeyException e) {
            throw new IllegalStateException("User-Organization relationship already exists", e);
        }
    }

    @Override
    public boolean existsByUserAndOrganization(UUID userId, UUID organizationId) {
        String sql = "SELECT COUNT(*) FROM user_organization WHERE user_id = ? AND organization_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, organizationId);
        return count != null && count > 0;
    }
}