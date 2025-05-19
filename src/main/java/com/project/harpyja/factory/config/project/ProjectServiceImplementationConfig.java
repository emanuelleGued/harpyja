package com.project.harpyja.factory.config.project;

import com.project.harpyja.repository.project.ProjectRepository;
import com.project.harpyja.repository.project.ProjectRepositoryJDBC;
import com.project.harpyja.service.project.ProjectServiceImplJDBC;
import com.project.harpyja.service.project.ProjectServiceImplJPA;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class ProjectServiceImplementationConfig {

    @Bean
    public ProjectServiceImplJPA projectServiceImplJPA(ProjectRepository projectRepository) {
        return new ProjectServiceImplJPA(projectRepository);
    }

    @Bean
    public ProjectRepositoryJDBC projectDAO(JdbcTemplate jdbcTemplate) {
        return new ProjectRepositoryJDBC(jdbcTemplate);
    }

    @Bean
    public ProjectServiceImplJDBC projectServiceImplJDBC(ProjectRepositoryJDBC projectDAO) {
        return new ProjectServiceImplJDBC(projectDAO);
    }
}