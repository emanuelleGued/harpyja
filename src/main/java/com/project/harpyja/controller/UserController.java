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

@Tag(name= "Users", description = "Operations related to users")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "List users with pagination",
            description = "Returns a paginated list of users with optional filters",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Users found"),
                    @ApiResponse(responseCode = "400", description = "Invalid parameters")
            })
    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Filter by name (case-insensitive)", example = "Robert")
            @RequestParam(required = false) String name,
            @Parameter(description = "Filter by email (case-insensitive)", example = "test")
            @RequestParam(required = false) String email,
            @Parameter(description = "Sort field and direction", example = "name,asc")
            @RequestParam(defaultValue = "name,asc") String[] sort) {

        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<User> users = userService.findAllUsers(pageable, name, email);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get user by ID",
            description = "Returns user details by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "404", description = "User not found")
            })
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@Parameter(description = "ID of the user to find", required = true)
                                         @PathVariable("id") String id) {
        User user = userService.findUserById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found by ID");
        }
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Delete a user",
            description = "Deletes a user by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
        User user = userService.findUserById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found by ID");
        }
        userService.deleteUser(id);
        return ResponseEntity.ok().body("User deleted successfully");
    }

    @Operation(summary = "Create a new user",
            description = "Registers a new user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid data")
            })
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequest userRequest) {
        if (userService.emailExists(userRequest.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }
        User created = userService.createUser(userRequest.toUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Verify user email",
            description = "Updates user's status to 'email verified'",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email verified successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "404", description = "User not found with provided email")
            })
    @PutMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam("email") String email) {
        User updated = userService.updateEmailVerified(email);
        if (updated == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with provided email");
        }
        return ResponseEntity.ok(updated);
    }
}
