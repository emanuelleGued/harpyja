package com.project.harpyja.controller;

import com.project.harpyja.dto.LoginRequest;
import com.project.harpyja.dto.UserWithTokenDto;
import com.project.harpyja.dto.VerifyEmailResponse;
import com.project.harpyja.exception.CustomException;
import com.project.harpyja.service.auth.AuthService;
import com.project.harpyja.service.auth.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * POST /api/auth/login
     */
    @Operation(summary = "Autentica um usuário",
            description = "Realiza o login do usuário com e-mail e senha, retornando dados do usuário e um token JWT em caso de sucesso.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login bem-sucedido",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserWithTokenDto.class))),
                    @ApiResponse(responseCode = "400", description = "E-mail ou senha não fornecidos"),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
            })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
            throw new CustomException("Email e senha são obrigatórios", 400);
        }

        UserWithTokenDto userWithToken = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(userWithToken);
    }

    /**
     * GET /api/auth/verify-email/{token}
     */
    @Operation(summary = "Verifica um token de e-mail",
            description = "Valida um token genérico (ex: de verificação de e-mail) e retorna as informações contidas nele.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token válido",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = VerifyEmailResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Token inválido ou expirado")
            })
    @GetMapping("/verify-email/{token}")
    public ResponseEntity<?> verifyEmail(@Parameter(description = "Token a ser verificado", required = true) @PathVariable("token") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Token is required");
        }

        try {
            // Faz parse do token
            Jws<Claims> claimsJws = jwtUtil.parseToken(token);
            Claims claims = claimsJws.getBody();

            // Pega as claims do token
            String organizationId = (String) claims.get("organizationId");
            String userId = (String) claims.get("userId");
            String email = (String) claims.get("email");

            // Exemplo “buscar user no banco e gerar outro token de sessão”
            // Você pode aproveitar o AuthService ou outro service
            // A lógica exata depende de como você guarda “organizationId”, “projectKey”, etc.

            // Exemplo fictício de gerar um token usando o userId
            String newAuthToken = jwtUtil.generateToken(userId);

            // Monte a resposta
            VerifyEmailResponse response = new VerifyEmailResponse(
                    organizationId,
                    userId,
                    null,         // se quiser retornar o User, busque no repo
                    newAuthToken,
                    ""            // se tiver projectKey, popule aqui
            );

            return ResponseEntity.ok(response);

        } catch (JwtException e) {
            // Token inválido ou expirado
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid or expired token: " + e.getMessage());
        }
    }
}
