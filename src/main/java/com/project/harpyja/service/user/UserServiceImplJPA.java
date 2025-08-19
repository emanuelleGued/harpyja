package com.project.harpyja.service.user;

import com.project.harpyja.entity.User;
import com.project.harpyja.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
public class UserServiceImplJPA implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) {
        user.setId(UUID.randomUUID().toString());
        user.setEmailVerified(false);
        user.setTermsAgreed(true);
        return userRepository.save(user);
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public User updateEmailVerified(String email) {
        // Encontra o usuário pelo email
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Atualiza o campo
        existingUser.setEmailVerified(true);

        User updatedUser = userRepository.save(existingUser);

        return updatedUser;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found by ID"));
    }

    @Override
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User updateUserService(String userId, User updates) {
        User existingUser = findUserById(userId);

        if (updates.getName() != null) {
            existingUser.setName(updates.getName());
        }
        if (updates.getPassword() != null && !updates.getPassword().isEmpty()) {
            existingUser.setPassword(updates.getPassword());
        }
        return existingUser;
    }

    @Override
    public User findUserByEmailServiceOnboarding(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        // Retorna null se não existe
        return null;
    }

    @Override
    public User findUserByEmail(String email) throws EntityNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    @Override
    public Page<User> findAllUsers(Pageable pageable, String name, String email) {
        if ((name == null || name.isEmpty()) && (email == null || email.isEmpty())) {
            return userRepository.findAll(pageable);
        }

        return userRepository.findByFilters(name, email, pageable);
    }
}