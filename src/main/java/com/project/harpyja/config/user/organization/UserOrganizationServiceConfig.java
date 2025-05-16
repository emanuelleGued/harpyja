package com.project.harpyja.config.user.organization;

import com.project.harpyja.service.user.organization.UserOrganizationService;
import com.project.harpyja.service.user.organization.UserOrganizationServiceImplJDBC;
import com.project.harpyja.service.user.organization.UserOrganizationServiceImplJPA;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class UserOrganizationServiceConfig {

    @Bean
    @ConfigurationProperties(prefix = "app.user-organization-service")
    public UserOrganizationServiceConfigProperties userOrganizationServiceConfigProperties() {
        return new UserOrganizationServiceConfigProperties();
    }

    @Bean
    public UserOrganizationServiceImplJPA userOrganizationServiceImplJPA() {
        return new UserOrganizationServiceImplJPA();
    }

    @Bean
    public UserOrganizationServiceImplJDBC userOrganizationServiceImplJDBC(JdbcTemplate jdbcTemplate) {
        return new UserOrganizationServiceImplJDBC(jdbcTemplate);
    }

    @Bean
    @Primary
    public UserOrganizationService userOrganizationService(
            UserOrganizationServiceConfigProperties config,
            UserOrganizationServiceImplJPA jpaImpl,
            UserOrganizationServiceImplJDBC jdbcImpl) {

        return switch (config.getImplementation().toLowerCase()) {
            case "jdbc" -> jdbcImpl;
            case "jpa" -> jpaImpl;
            default -> throw new IllegalArgumentException(
                    "Invalid value for app.user-organization-service.implementation. Use 'jpa' or 'jdbc'");
        };
    }

    public static class UserOrganizationServiceConfigProperties {
        private String implementation = "jpa";

        // getters and setters
        public String getImplementation() {
            return implementation;
        }

        public void setImplementation(String implementation) {
            this.implementation = implementation;
        }
    }
}