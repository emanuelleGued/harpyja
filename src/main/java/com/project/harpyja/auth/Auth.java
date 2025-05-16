package com.project.harpyja.auth;

import com.project.harpyja.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class Auth {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateTokenToVerifyEmail(String email, String organizationId, String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("organizationId", organizationId);
        claims.put("userId", userId);
        claims.put("email", email);

        // Token expira em 24 horas
        long expirationTime = 24 * 60 * 60 * 1000; // 24 horas em milissegundos
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public String generateAuthToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        // Adicione outras claims necessárias

        long expirationTime = 24 * 60 * 60 * 1000; // 24 horas
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }
}