package com.project.harpyja.controller.project;

import com.project.harpyja.dto.request.CreateProjectRequest;
import com.project.harpyja.dto.response.CreateProjectResponse;
import com.project.harpyja.dto.response.UserProjectResponse;
import com.project.harpyja.entity.*;
import com.project.harpyja.model.enums.ProjectRole;
import com.project.harpyja.service.auth.JwtUtil;
import com.project.harpyja.service.project.ProjectService;
import com.project.harpyja.service.user.project.UserProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final UserProjectService userProjectService;
    private final JwtUtil jwtUtil;

    @Autowired
    public ProjectController(
            ProjectService projectService,
            UserProjectService userProjectService,
            JwtUtil jwtUtil) {
        this.projectService = projectService;
        this.userProjectService = userProjectService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/create/{organizationId}")
    public ResponseEntity<?> createProject(
            @PathVariable String organizationId,
            @RequestBody CreateProjectRequest createProjectRequest,
            @RequestHeader("Authorization") String authHeader) {

        try {
            // 1. Validar organizationId
            if (organizationId == null || organizationId.isEmpty()) {
                return ResponseEntity.badRequest().body("organization_id is required");
            }

            UUID orgId;
            try {
                orgId = UUID.fromString(organizationId);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid organization_id format");
            }

            // 2. Validar corpo da requisição
            if (createProjectRequest.getName() == null || createProjectRequest.getName().isEmpty()) {
                return ResponseEntity.badRequest().body("Project name is required");
            }

            // 3. Extrair e validar token JWT
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header is required and must be Bearer token");
            }

            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            String userId = jwtUtil.extractUserId(token);
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user claims");
            }

            UUID userUuid;
            try {
                userUuid = UUID.fromString(userId);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user ID format in token");
            }

            Project project = new Project();
            project.setId(UUID.randomUUID());
            project.setName(createProjectRequest.getName());
            project.setType(createProjectRequest.getType());
            project.setExpiration(LocalDateTime.now().plusDays(30)); // 30 dias de expiração
            project.setKey(generateProjectKey());

            Organization org = new Organization();
            org.setId(orgId);
            project.setOrganization(org);

            Project createdProject = projectService.createProject(project);

            UserProjectId userProjectId = new UserProjectId();
            userProjectId.setUserId(userUuid);
            userProjectId.setProjectId(createdProject.getId());

            UserProject userProject = new UserProject();
            userProject.setId(userProjectId);
            userProject.setRole(ProjectRole.ADMIN);

            User user = new User();
            user.setId(userUuid);
            userProject.setUser(user);
            userProject.setProject(createdProject);

            userProjectService.createUserProject(userProject);

            CreateProjectResponse response = new CreateProjectResponse(
                    createdProject.getId(),
                    createdProject.getName(),
                    createdProject.getKey(),
                    createdProject.getType()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String generateProjectKey() {
        // Implemente sua lógica de geração de chave aqui
        return "prj_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    @GetMapping("/my-projects")
    public ResponseEntity<?> getUserProjects(@RequestHeader("Authorization") String authHeader) {
        try {
            // 1. Validar token JWT
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

            UUID userUuid;
            try {
                userUuid = UUID.fromString(userId);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user ID format in token");
            }

            List<Project> projects = projectService.getUserProjects(userUuid);

            List<UserProjectResponse> response = projects.stream()
                    .map(project -> {
                        ProjectRole role = project.getUsers().stream()
                                .filter(up -> up.getUser().getId().equals(userUuid))
                                .findFirst()
                                .map(UserProject::getRole)
                                .orElse(ProjectRole.VIEWER); // Default caso não encontre

                        return new UserProjectResponse(
                                project.getId(),
                                project.getName(),
                                project.getKey(),
                                project.getType(),
                                project.getExpiration(),
                                role
                        );
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error fetching user projects: " + e.getMessage());
        }
    }
}