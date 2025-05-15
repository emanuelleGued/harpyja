package com.project.harpyja.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()  // Novo método para autorizar requisições
                .requestMatchers("/api/users/**").permitAll()  // Permite acesso sem autenticação à rota /api/users
                .requestMatchers("/api/auth/**").permitAll()  // Permite acesso sem autenticação à rota /api/users
                .requestMatchers("/api/v2/videos/**").permitAll()  // Permite acesso sem autenticação à rota /api/users
                .anyRequest().authenticated()  // Outras rotas exigem autenticação
                .and()
                .csrf().disable()  // Desabilita a proteção CSRF para APIs REST (não é necessária)
                .httpBasic().disable();  // Desabilita autenticação básica HTTP (já que você usará JWT)

        return http.build();  // Retorna a configuração do HttpSecurity
    }
}
