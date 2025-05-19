package com.project.harpyja.factory;

import com.project.harpyja.factory.config.project.ProjectServiceConfig;
import com.project.harpyja.service.project.ProjectService;
import com.project.harpyja.service.project.ProjectServiceImplJDBC;
import com.project.harpyja.service.project.ProjectServiceImplJPA;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ProjectServiceFactory {

    @Bean
    @Primary
    public ProjectService projectService(
            ProjectServiceConfig config,
            ProjectServiceImplJPA projectServiceImplJPA,
            ProjectServiceImplJDBC projectServiceImplJDBC) {

        return switch (config.getImplementation().toLowerCase()) {
            case "jdbc" -> projectServiceImplJDBC;
            case "jpa" -> projectServiceImplJPA;
            default -> throw new IllegalArgumentException(
                    "Valor inválido para app.project-service.implementation. Use 'jpa' ou 'jdbc'");
        };
    }
}