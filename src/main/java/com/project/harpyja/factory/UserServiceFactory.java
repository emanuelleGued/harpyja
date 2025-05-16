package com.project.harpyja.factory;

import com.project.harpyja.config.user.UserServiceConfig;
import com.project.harpyja.service.user.UserService;
import com.project.harpyja.service.user.UserServiceImplJDBC;
import com.project.harpyja.service.user.UserServiceImplJPA;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class UserServiceFactory {

    @Bean
    @Primary
    public UserService userService(
            UserServiceConfig config,
            UserServiceImplJPA userServiceImplJPA,
            UserServiceImplJDBC userServiceImplJDBC) {

        return switch (config.getImplementation().toLowerCase()) {
            case "jdbc" -> userServiceImplJDBC;
            case "jpa" -> userServiceImplJPA;
            default -> throw new IllegalArgumentException(
                    "Valor inválido para app.user-service.implementation. Use 'jpa' ou 'jdbc'");
        };
    }
}