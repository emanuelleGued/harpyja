package com.project.harpyja.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // CSRF deve vir primeiro
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/onboarding/intention").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/onboarding/verify/**").permitAll()
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/projects/**").permitAll()
                        .requestMatchers("/api/v2/videos/**").permitAll()
                        .requestMatchers("/error").permitAll() // <- ESSENCIAL para evitar 403 ao redirecionar erro
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }

}
