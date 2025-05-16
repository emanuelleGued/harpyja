package com.project.harpyja.service.user;

import com.project.harpyja.entity.User;
import com.project.harpyja.repository.UserRepository;
import com.project.harpyja.repository.UserWithProjectKey;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
        user.setId(UUID.randomUUID()); // Gera UUID
        // Exemplo de "hash" de senha (BCrypt, etc.)
        // user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setEmailVerified(false);
        user.setTermsAgreed(true); // conforme o Go code original
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

        // Salva (não é estritamente necessário com JPA e @Transactional, mas explícito)
        User updatedUser = userRepository.save(existingUser);

        return updatedUser;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserById(String userId) {
        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found by ID"));
    }

    @Override
    public void deleteUser(String userId) {
        userRepository.deleteById(UUID.fromString(userId));
    }

    @Override
    public UserWithProjectKey findUserWithProjectKeyByEmail(String email) {
        return null;
    }

    // =========================
    // MÉTODOS EXTRAS PARA ONBOARDING
    // =========================

    /**
     * Verifica se o e-mail já existe no sistema.
     * Caso exista, lança uma exceção para bloquear o processo de onboarding.
     */
    public void validateUserEmailForOnboarding(String email) {
        if (emailExists(email)) {
            throw new RuntimeException("Email already exists");
        }
    }

    /**
     * Cria um usuário simplificado para o onboarding,
     * podendo ter regras diferentes de createUser (nome opcional, etc.).
     */
    public User createUserForOnboarding(String email, String rawPassword) {
        // Supondo que a lógica é quase a mesma do createUser, mas sem nome
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(email);

        // Exemplo de hashing (se você tiver um passwordEncoder)
        // user.setPassword(passwordEncoder.encode(rawPassword));
        user.setPassword(rawPassword);

        user.setEmailVerified(false);
        user.setTermsAgreed(false); // talvez false, já que ainda não concordou?

        return userRepository.save(user);
    }

    /**
     * Atualiza somente o nome de um usuário pelo ID (durante onboarding).
     */
    public User updateUserName(String userId, String newName) {
        User user = findUserById(userId); // Reaproveitando método já existente
        user.setName(newName);
        // Ao final da transação, o user será salvo
        return user;
    }
    // ==============================
    // NOVOS MÉTODOS PARA EQUIVALÊNCIA AO GO CODE
    // ==============================

    /**
     * Atualiza parcialmente o usuário, seguindo a lógica do Go:
     * Se "updates.getName() != null", então atualiza; se "updates.getPassword() != null", então atualiza, etc.
     */
    @Override
    public User updateUserService(String userId, User updates) {
        User existingUser = findUserById(userId);

        // Exemplo de partial update
        if (updates.getName() != null) {
            existingUser.setName(updates.getName());
        }
        if (updates.getPassword() != null && !updates.getPassword().isEmpty()) {
            // Ex.: existingUser.setPassword(passwordEncoder.encode(updates.getPassword()));
            existingUser.setPassword(updates.getPassword());
        }
        // Se tiver outros campos, você pode atualizar aqui:
        // if (updates.isEmailVerified()) { existingUser.setEmailVerified(true); }

        // Com @Transactional, a entidade é "dirty-checked" e salva automaticamente
        return existingUser;
    }

    /**
     * Equivalente ao "FindUserByEmailServiceOnboarding" do Go.
     * Se o usuário existe ou err == nil, lança exceção de "EmailAlreadyExists".
     * Caso contrário, retorna null.
     */
    @Override
    public User findUserByEmailServiceOnboarding(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        // Se userOpt.isPresent() => e-mail já existe => erro
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
}