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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "Authenticate user",
            description = "Performs user login with email and password, returning user data and JWT token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserWithTokenDto.class))),
                    @ApiResponse(responseCode = "400", description = "Email or password not provided"),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials")
            })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty() ||
                loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
            throw new CustomException("Email and password are required", HttpStatus.BAD_REQUEST.value());
        }

        UserWithTokenDto userWithToken = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(userWithToken);
    }

    @Operation(summary = "Verify email token",
            description = "Validates a generic token (e.g., email verification) and returns its information",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Valid token",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = VerifyEmailResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid or expired token")
            })
    @GetMapping("/verify-email/{token}")
    public ResponseEntity<?> verifyEmail(@PathVariable("token") String token) {

        if (token == null || token.isEmpty()) {
            throw new CustomException("Token is required", HttpStatus.BAD_REQUEST.value());
        }

        try {
            Jws<Claims> claimsJws = jwtUtil.parseToken(token);
            Claims claims = claimsJws.getBody();

            String organizationId = (String) claims.get("organizationId");
            String userId = (String) claims.get("userId");

            String newAuthToken = jwtUtil.generateToken(userId);

            VerifyEmailResponse response = new VerifyEmailResponse(
                    organizationId,
                    userId,
                    null,
                    newAuthToken,
                    ""
            );

            return ResponseEntity.ok(response);

        } catch (JwtException e) {
            throw new CustomException("Invalid or expired token: " + e.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
    }
}
