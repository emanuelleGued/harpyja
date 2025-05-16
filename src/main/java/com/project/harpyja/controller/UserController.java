package com.project.harpyja.controller;

import com.project.harpyja.model.User;
import com.project.harpyja.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // GET /api/users
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    // GET /api/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") String id) {
        // Se seu ID for "Long", troque para (long id)
        User user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    // POST /api/users
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
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().body("User deleted");
    }

    // Exemplo de endpoint para "updateEmailVerified"
    @PutMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam("email") String email) {
        User updated = userService.updateEmailVerified(email);
        return ResponseEntity.ok(updated);
    }
}
