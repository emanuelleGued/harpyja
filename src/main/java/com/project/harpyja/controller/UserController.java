package com.project.harpyja.controller;

import com.project.harpyja.entity.User;
import com.project.harpyja.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name= "Users", description = "Operações relacionadas a usuários")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // GET /api/users
    @Operation(
            summary = "Listar usuários com paginação",
            description = "Retorna uma lista paginada de usuários com filtros opcionais",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuários encontrados"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
            }
    )
    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @Parameter(description = "Número da página (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Quantidade de itens por página", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Filtro por nome (case-insensitive)", example = "Robert")
            @RequestParam(required = false) String name,

            @Parameter(description = "Filtro por email (case-insensitive)", example = "test")
            @RequestParam(required = false) String email,

            @Parameter(description = "Campo para ordenação", example = "name,asc")
            @RequestParam(defaultValue = "name,asc") String[] sort) {

        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<User> users = userService.findAllUsers(pageable, name, email);
        return ResponseEntity.ok(users);
    }

    // GET /api/users/{id}
    @Operation(
            summary = "Buscar usuário por ID",
            description = "Retorna os dados de um usuário específico a partir do seu ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@Parameter(description = "ID do usuário a ser buscado", required = true)
                                             @PathVariable("id") String id) {
        User user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    // POST /api/users
    @Operation(
            summary = "Criar um novo usuário",
            description = "Cadastra um novo usuário no sistema.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos (ex: e-mail já existe, campos em branco)")
            }
    )
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {

        // Validação: Verificar se o nome é nulo ou vazio
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O nome do usuário não pode ser vazio.");
        }

        // Validação: Verificar se o email é nulo, vazio ou inválido
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O email do usuário não pode ser vazio.");
        }
        if (!isValidEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O email fornecido não tem um formato válido.");
        }

        // Verificar se já existe um usuário com o mesmo email
        if (userService.emailExists(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Já existe um usuário registrado com este email.");
        }

        // Validação: Verificar se a senha tem pelo menos 8 caracteres
        if (user.getPassword() == null || user.getPassword().length() < 8) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A senha deve ter pelo menos 8 caracteres.");
        }

        User created = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    // DELETE /api/users/{id}
    @Operation(
            summary = "Deletar um usuário",
            description = "Remove um usuário do sistema a partir do seu ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@Parameter(description = "ID do usuário a ser deletado", required = true)
                                            @PathVariable("id") String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().body("User deleted");
    }

    @Operation(
            summary = "Verificar o e-mail de um usuário",
            description = "Atualiza o status de um usuário para 'e-mail verificado'.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "E-mail do usuário verificado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado com o e-mail fornecido")
            }
    )
    @PutMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@Parameter(description = "E-mail do usuário a ser verificado", required = true)
                                             @RequestParam("email") String email) {
        User updated = userService.updateEmailVerified(email);
        return ResponseEntity.ok(updated);
    }
}
