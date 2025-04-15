package com.project.harpyja.repository;

import com.project.harpyja.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
    @Query("""
    SELECT u.id AS userId, p.key AS projectKey
    FROM User u
    JOIN UserProject up ON u.id = up.user.id
    JOIN Project p ON up.project.id = p.id
    WHERE u.email = :email
""")
    UserWithProjectKey findUserWithProjectKeyByEmail(@Param("email") String email);
}
