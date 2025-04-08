package com.project.harpyja.service;

import com.project.harpyja.model.User;
import com.project.harpyja.repository.UserRepository;
import com.project.harpyja.repository.UserWithProjectKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    // Exemplo de criação de usuário, com hashing ou não
    @Override
    public User createUser(User user) {
        // Gerar ID se for string/UUID manualmente:
        user.setId(UUID.fromString(UUID.randomUUID().toString()));

        // Exemplo de "hash" de senha (BCrypt, etc.); aqui só para ilustração:
        // user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setEmailVerified(false);
        user.setTermsAgreed(true); // conforme seu Go code

        User saved = userRepository.save(user);
        return saved;
    }

    // "updateEmailVerified(email string)"
    @Override
    public User updateEmailVerified(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmailVerified(true);
        // userRepository.save(user); -> save é chamado automaticamente ao final da transação
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserById(String userId) {
        // Se userId for string/UUID:
        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found by ID"));
    }

    @Override
    public void deleteUser(String userId) {
        userRepository.deleteById(UUID.fromString(userId));
    }

    @Override
    public UserWithProjectKey findUserWithProjectKeyByEmail(String email) {
        // Se você tiver o método de projeção no repositório:
        UserWithProjectKey result = userRepository.findUserWithProjectKeyByEmail(email);
        if (result == null) {
            throw new RuntimeException("No record found with project key for email: " + email);
        }
        return result;
    }
}

