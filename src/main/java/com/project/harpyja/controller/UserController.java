
package com.project.harpyja.controller;

import com.project.harpyja.dto.request.UserRequest;
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
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@Tag(name= "Users", description = "Operações relacionadas a usuários")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Listar usuários com paginação",
            description = "Retorna uma lista paginada de usuários com filtros opcionais",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuários encontrados"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
            })
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

    @Operation(summary = "Buscar usuário por ID",
            description = "Retorna os dados de um usuário específico a partir do seu ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            })
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@Parameter(description = "ID do usuário a ser buscado", required = true)
                                         @PathVariable("id") String id) {
        User user = userService.findUserById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Deletar um usuário",
            description = "Remove um usuário do sistema a partir do seu ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
        User user = userService.findUserById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }
        userService.deleteUser(id);
        return ResponseEntity.ok().body("Usuário deletado");
    }

    @Operation(summary = "Criar um novo usuário",
            description = "Cadastra um novo usuário no sistema.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos")
            })
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequest userRequest) {
        if (userService.emailExists(userRequest.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado");
        }
        User created = userService.createUser(userRequest.toUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Verificar o e-mail de um usuário",
            description = "Atualiza o status de um usuário para 'e-mail verificado'.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "E-mail do usuário verificado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            })
    @PutMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam("email") String email) {
        User updated = userService.updateEmailVerified(email);
        if (updated == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado com o e-mail fornecido");
        }
        return ResponseEntity.ok(updated);
    }
}
