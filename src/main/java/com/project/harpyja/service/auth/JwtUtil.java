package com.project.harpyja.service.auth;

import com.project.harpyja.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // Métodos existentes (mantidos para compatibilidade)

    public String generateToken(String userId) {
        Instant now = Instant.now();
        Instant exp = now.plus(7, ChronoUnit.DAYS);

        return Jwts.builder()
                .claim("id", userId)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Métodos adicionais necessários para o controller

    /**
     * Valida um token JWT
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Extrai todas as claims do token
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extrai uma claim específica do token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrai o ID do usuário do token (como subject)
     */
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrai o ID do usuário de claims customizadas (para compatibilidade com o Go)
     */
    public String getUserIdFromToken(String token) {
        return extractClaim(token, claims -> claims.get("id", String.class));
    }

    /**
     * Extrai o email do usuário do token
     */
    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    /**
     * Extrai a organização do token
     */
    public String extractOrganizationId(String token) {
        return extractClaim(token, claims -> claims.get("organizationId", String.class));
    }

    /**
     * Verifica se o token está expirado
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrai a data de expiração do token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Método para parse do token (mantido para compatibilidade)
     */
    public Jws<Claims> parseToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
    }

    public String generateAuthToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 24h
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Padronizado
                .compact();
    }

    public String generateTokenToVerifyEmail(String email, String organizationId, String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("organizationId", organizationId);
        claims.put("userId", userId);
        claims.put("email", email);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Padronizado
                .compact();
    }

    /**
     * Verifica e extrai os dados de um token de verificação de email
     *
     * @param token Token JWT gerado por generateTokenToVerifyEmail
     * @return Objeto com os dados verificados
     * @throws JwtException Se o token for inválido
     */
    public VerifiedEmailToken verifyEmailToken(String token) throws JwtException {
        // Parse e validação do token
        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);

        Claims claims = jws.getBody();

        // Extrair as claims específicas
        String email = claims.get("email", String.class);
        String organizationId = claims.get("organizationId", String.class);
        String userId = claims.get("userId", String.class);

        // Verificar se todas as claims necessárias estão presentes
        if (email == null || organizationId == null || userId == null) {
            throw new MalformedJwtException("Missing required claims in token");
        }

        return new VerifiedEmailToken(email, organizationId, userId);
    }

    // Classe DTO para retornar os dados verificados
    public static class VerifiedEmailToken {
        private final String email;
        private final String organizationId;
        private final String userId;

        public VerifiedEmailToken(String email, String organizationId, String userId) {
            this.email = email;
            this.organizationId = organizationId;
            this.userId = userId;
        }

        // Getters
        public String getEmail() {
            return email;
        }

        public String getOrganizationId() {
            return organizationId;
        }

        public String getUserId() {
            return userId;
        }
    }
}