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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.project.harpyja.utils.HashGenerator.generateHashKey;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final UserProjectService userProjectService;
    private final JwtUtil jwtUtil;
    private final SessionRepositoryImpl sessionRepository;


    @Autowired
    public ProjectController(
            ProjectService projectService,
            UserProjectService userProjectService,
            JwtUtil jwtUtil,
            SessionRepositoryImpl sessionRepository) {
        this.projectService = projectService;
        this.userProjectService = userProjectService;
        this.jwtUtil = jwtUtil;
        this.sessionRepository = sessionRepository;
    }

    @Operation(
            summary = "Cria um novo projeto",
            description = "Cria um novo projeto associado a uma organização. O usuário autenticado se torna o administrador do projeto.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Projeto criado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos ou organização não encontrada"),
                    @ApiResponse(responseCode = "401", description = "Não autorizado")
            }
    )
    @PostMapping("/create/{organizationId}")
    public ResponseEntity<?> createProject(
            @Parameter(description = "ID da organização à qual o projeto pertencerá", required = true)
            @PathVariable String organizationId,
            @RequestBody CreateProjectRequest createProjectRequest,
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String authHeader) {

        try {
            // 1. Validar organizationId
            if (organizationId == null || organizationId.isEmpty()) {
                return ResponseEntity.badRequest().body("organization_id is required");
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

            UserProjectId userProjectId = new UserProjectId();
            userProjectId.setUserId(userId);
            userProjectId.setProjectId(createdProject.getId());

            UserProject userProject = new UserProject();
            userProject.setId(userProjectId);
            userProject.setRole(ProjectRole.ADMIN);

            User user = new User();
            user.setId(userId);
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

    /*
    curl  -X GET http://localhost:8080/api/projects/my-projects \
          -H "Authorization: Bearer SEU_TOKEN_AQUI" \
          -H "Content-Type: application/json"
     */
    @Operation(
            summary = "Lista os projetos do usuário autenticado",
            description = "Retorna uma lista paginada dos projetos aos quais o usuário autenticado tem acesso, com opções de filtro.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Projetos listados com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Não autorizado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
            }
    )
    @GetMapping("/my-projects")
    public ResponseEntity<?> getUserProjects(
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String authHeader,
            @Parameter(description = "Número da página (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Quantidade de itens por página", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Filtrar por tipo de projeto")
            @RequestParam(required = false) String type,
            @Parameter(description = "Filtrar por nome do projeto (case-insensitive)")
            @RequestParam(required = false) String name) {

        try {
            // Validação do token (mantenha seu código existente)
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Authorization header is required and must be Bearer token");
            }
            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
            String userId = jwtUtil.extractUserId(token);

            // Adiciona paginação
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<Project> projectsPage = projectService.getUserProjects(userId, type, name, pageable);

            // Converte para Page de DTO
            Page<UserProjectResponse> response = projectsPage.map(project ->
                    new UserProjectResponse(
                            project.getId(),
                            project.getName(),
                            project.getKey(),
                            project.getType(),
                            project.getExpiration(),
                            null // role pode ser ajustado conforme sua lógica
                    )
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error fetching user projects: " + e.getMessage());
        }
    }
    /*
    curl -X GET http://localhost:8080/api/projects/{projectId}/details \
     -H "Authorization: Bearer seu_token_jwt_aqui" \
     -H "Content-Type: application/json"
     */
    @Operation(
            summary = "Obtém detalhes de um projeto específico",
            description = "Retorna informações detalhadas de um projeto, incluindo suas sessões, se o usuário autenticado tiver permissão de acesso.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Detalhes do projeto encontrados"),
                    @ApiResponse(responseCode = "401", description = "Não autorizado"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado ao projeto"),
                    @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
            }
    )
    @GetMapping("/{projectId}/details")
    public ResponseEntity<?> getProjectDetails(
            @Parameter(description = "ID do projeto a ser detalhado", required = true)
            @PathVariable String projectId,
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String authHeader) {
        try {
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

            Optional<Project> projectOpt = projectService.getProjectById(projectId);
            if (projectOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Project not found");
            }

            Project project = projectOpt.get();

            boolean hasAccess = userProjectService.existsByUserIdAndProjectId(userId, projectId);
            if (!hasAccess) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("User does not have access to this project");
            }

            ProjectRole userRole = userProjectService.findUserRoleInProject(userId, projectId)
                    .orElse(ProjectRole.VIEWER);


            List<Session> sessions = sessionRepository.findByKey(project.getKey());

            ProjectDetailsResponse response = new ProjectDetailsResponse(
                    project.getId(),
                    project.getName(),
                    project.getKey(),
                    project.getType(),
                    project.getExpiration(),
                    userRole,
                    sessions
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error fetching project details: " + e.getMessage());
        }
    }
}
