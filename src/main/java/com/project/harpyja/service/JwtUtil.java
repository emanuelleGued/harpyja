package com.project.harpyja.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtUtil {

    private final String secretKey = "chave-super-secreta-exemplo"; // Exemplo fixo
    // OU injete via @Value("${jwt.secret}") ou via JwtConfig

    /**
     * Gera um token com expiração de 7 dias (por exemplo).
     */
    public String generateToken(String userId) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        Instant now = Instant.now();
        Instant exp = now.plus(7, ChronoUnit.DAYS); // expira em 7 dias

        return Jwts.builder()
                .claim("id", userId)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Gera um token de verificação de email válido por 24h.
     */
    public String generateTokenToVerifyEmail(String email, String organizationId, String userId) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        Instant now = Instant.now();
        Instant exp = now.plus(1, ChronoUnit.DAYS); // 24h

        return Jwts.builder()
                .claim("email", email)
                .claim("organizationId", organizationId)
                .claim("userId", userId)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrai as claims do token JWT (se válido).
     */
    public Jws<Claims> parseToken(String token) throws JwtException {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    /**
     * Retorna o ID do usuário se presente no token.
     */
    public String getUserIdFromToken(String token) {
        Jws<Claims> jws = parseToken(token);
        return jws.getBody().get("id", String.class);
    }
}
