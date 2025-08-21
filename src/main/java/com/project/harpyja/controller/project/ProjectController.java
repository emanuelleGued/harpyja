package com.project.harpyja.controller.project;

import com.project.harpyja.dto.request.CreateProjectRequest;
import com.project.harpyja.dto.response.CreateProjectResponse;
import com.project.harpyja.dto.response.ProjectDetailsResponse;
import com.project.harpyja.dto.response.UserProjectResponse;
import com.project.harpyja.entity.*;
import com.project.harpyja.model.enums.ProjectRole;
import com.project.harpyja.model.nymphicus.Session;
import com.project.harpyja.repository.nymphicus.session.SessionRepositoryImpl;
import com.project.harpyja.service.auth.JwtUtil;
import com.project.harpyja.service.project.ProjectService;
import com.project.harpyja.service.user.project.UserProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.project.harpyja.utils.HashGenerator.generateHashKey;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Operations related to projects")
public class ProjectController {

    private final ProjectService projectService;
    private final UserProjectService userProjectService;
    private final JwtUtil jwtUtil;
    private final SessionRepositoryImpl sessionRepository;

    @Autowired
    public ProjectController(ProjectService projectService,
                             UserProjectService userProjectService,
                             JwtUtil jwtUtil,
                             SessionRepositoryImpl sessionRepository) {
        this.projectService = projectService;
        this.userProjectService = userProjectService;
        this.jwtUtil = jwtUtil;
        this.sessionRepository = sessionRepository;
    }

    @Operation(summary = "Create a new project", description = "Creates a project and assigns the authenticated user as ADMIN")
    @PostMapping("/create/{organizationId}")
    public ResponseEntity<?> createProject(
            @PathVariable String organizationId,
            @Valid @RequestBody CreateProjectRequest createProjectRequest,
            @RequestHeader("Authorization") String authHeader) {

        if (organizationId == null || organizationId.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Organization ID is required");

        if (createProjectRequest.getName() == null || createProjectRequest.getName().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project name is required");

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization header is required");

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");

        String userId = jwtUtil.extractUserId(token);
        if (userId == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid user claims");

        try {
            Project project = new Project();
            project.setId(UUID.randomUUID().toString());
            project.setName(createProjectRequest.getName());
            project.setType(createProjectRequest.getType());
            project.setExpiration(LocalDateTime.now().plusDays(30));
            project.setKey(generateHashKey());

            Organization org = new Organization();
            org.setId(organizationId);
            project.setOrganization(org);

            Project createdProject = projectService.createProject(project);

            UserProject userProject = new UserProject();
            User user = new User();
            user.setId(userId);
            userProject.setUser(user);
            userProject.setId(new UserProjectId(userId, createdProject.getId()));
            userProject.setRole(ProjectRole.ADMIN);
            userProject.setProject(createdProject);

            userProjectService.createUserProject(userProject);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new CreateProjectResponse(
                            createdProject.getId(),
                            createdProject.getName(),
                            createdProject.getKey(),
                            createdProject.getType()
                    ));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "List authenticated user's projects")
    @GetMapping("/my-projects")
    public ResponseEntity<?> getUserProjects(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String name) {

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization header is required");

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");

        String userId = jwtUtil.extractUserId(token);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Project> projectsPage = projectService.getUserProjects(userId, type, name, pageable);

        Page<UserProjectResponse> response = projectsPage.map(project -> new UserProjectResponse(
                project.getId(),
                project.getName(),
                project.getKey(),
                project.getType(),
                project.getExpiration(),
                userProjectService.findUserRoleInProject(userId, project.getId()).orElse(ProjectRole.VIEWER)
        ));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get details of a specific project")
    @GetMapping("/{projectId}/details")
    public ResponseEntity<?> getProjectDetails(
            @PathVariable String projectId,
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization header is required");

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");

        String userId = jwtUtil.extractUserId(token);

        Project project = projectService.getProjectById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        if (!userProjectService.existsByUserIdAndProjectId(userId, projectId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have access to this project");

        ProjectRole userRole = userProjectService.findUserRoleInProject(userId, projectId)
                .orElse(ProjectRole.VIEWER);

        List<Session> sessions = sessionRepository.findByKey(project.getKey());

        return ResponseEntity.ok(new ProjectDetailsResponse(
                project.getId(),
                project.getName(),
                project.getKey(),
                project.getType(),
                project.getExpiration(),
                userRole,
                sessions
        ));
    }
}