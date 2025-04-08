package com.project.harpyja.controller;

import com.project.harpyja.model.User;
import com.project.harpyja.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Exemplo de controller REST
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
        // Faz as mesmas validações que você fazia no Go code:
        // se user.getName() == null, se user.getEmail() == null, se senha >= 8 chars etc.

        // Supondo que tudo certo:
        User created = userService.createUser(user);
        return ResponseEntity.status(201).body(created);
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

