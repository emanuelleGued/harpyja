package com.project.harpyja.service.user.organization;

import com.project.harpyja.entity.UserOrganization;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserOrganizationServiceImplJDBC implements UserOrganizationService {

    private static final Logger logger = LoggerFactory.getLogger(UserOrganizationServiceImplJDBC.class);
    private final JdbcTemplate jdbcTemplate;

    public UserOrganizationServiceImplJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void createUserOrganizationService(UserOrganization userOrg) {
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
            logger.error("Duplicate key violation for user {} and organization {}",
                    userOrg.getId().getUserId(), userOrg.getId().getOrganizationId(), e);
            throw new IllegalStateException("User-Organization relationship already exists", e);
        } catch (Exception e) {
            logger.error("Error creating user-organization relationship for user {} and organization {}",
                    userOrg.getId().getUserId(), userOrg.getId().getOrganizationId(), e);
            throw new IllegalStateException("Failed to create user-organization relationship", e);
        }
    }

    @Override
    public boolean existsByUserAndOrganization(String userId, String organizationId) {
        try {
            String sql = "SELECT COUNT(*) FROM user_organization WHERE user_id = ? AND organization_id = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, organizationId);
            return count != null && count > 0;
        } catch (Exception e) {
            logger.error("Error checking relationship for user {} and organization {}", userId, organizationId, e);
            throw new IllegalStateException("Error checking user-organization relationship", e);
        }
    }
}