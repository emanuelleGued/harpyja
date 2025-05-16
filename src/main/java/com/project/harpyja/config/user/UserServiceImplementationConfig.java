package com.project.harpyja.config.user;

import com.project.harpyja.service.user.UserServiceImplJDBC;
import com.project.harpyja.service.user.UserServiceImplJPA;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class UserServiceImplementationConfig {

    @Bean
    public UserServiceImplJPA userServiceImplJPA() {
        return new UserServiceImplJPA();
    }

    @Bean
    public UserServiceImplJDBC userServiceImplJDBC(JdbcTemplate jdbcTemplate) {
        return new UserServiceImplJDBC(jdbcTemplate);
    }
}