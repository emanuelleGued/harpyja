package com.project.harpyja.controller.session;

import com.project.harpyja.model.nymphicus.Session;
import com.project.harpyja.service.auth.JwtUtil;
import com.project.harpyja.service.session.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    private final SessionService sessionService;
    private final JwtUtil jwtUtil;

    public SessionController(SessionService sessionService, JwtUtil jwtUtil) {
        this.sessionService = sessionService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/get/{project_key}")
    public ResponseEntity<?> getSession(
            @PathVariable("project_key") String projectKey,
            @RequestHeader("Authorization") String authHeader) {

        try {
            // 1. Validar project_key
            if (projectKey == null || projectKey.isEmpty()) {
                return ResponseEntity.badRequest().body("project_key is required");
            }

            // 2. Extrair e validar token JWT
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Authorization header is required and must be Bearer token");
            }

            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            String userId = jwtUtil.extractUserId(token);
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user claims");
            }

            List<Session> sessions = sessionService.getSessionByKey(projectKey);
            return ResponseEntity.ok(sessions);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to get session: " + e.getMessage());
        }
    }
}