package com.project.harpyja.service.user;

import com.project.harpyja.entity.User;
import com.project.harpyja.repository.UserWithProjectKey;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface UserService {

    User createUser(User user);

    User updateEmailVerified(String email);

    List<User> findAllUsers();

    User findUserById(String userId);

    void deleteUser(String userId);

    UserWithProjectKey findUserWithProjectKeyByEmail(String email);

    boolean emailExists(String email);

    void validateUserEmailForOnboarding(String email);

    User createUserForOnboarding(String email, String rawPassword);

    User updateUserName(String userId, String newName);

    User updateUserService(String userId, User updates);

    User findUserByEmailServiceOnboarding(String email);

    User findUserByEmail(String email) throws EntityNotFoundException;
}
