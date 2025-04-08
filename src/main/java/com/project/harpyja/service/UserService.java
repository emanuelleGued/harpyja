package com.project.harpyja.service;


import com.project.harpyja.model.User;
import com.project.harpyja.repository.UserWithProjectKey;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User updateEmailVerified(String email);
    List<User> findAllUsers();
    User findUserById(String userId);
    void deleteUser(String userId);
    UserWithProjectKey findUserWithProjectKeyByEmail(String email);
    // etc., conforme sua necessidade
}

