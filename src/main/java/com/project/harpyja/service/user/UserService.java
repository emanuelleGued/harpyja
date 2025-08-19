package com.project.harpyja.service.user;

import com.project.harpyja.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    User createUser(User user);

    User updateEmailVerified(String email);

    List<User> findAllUsers();

    User findUserById(String userId);

    void deleteUser(String userId);

    boolean emailExists(String email);

    User updateUserService(String userId, User updates);

    User findUserByEmailServiceOnboarding(String email);

    User findUserByEmail(String email) throws EntityNotFoundException;

    Page<User> findAllUsers(Pageable pageable, String name, String email);

}