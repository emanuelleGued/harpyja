package com.project.harpyja.factory.config.project;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app.project-service")
public class ProjectServiceConfig {

    private String implementation = "jpa";
}