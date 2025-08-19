package com.project.harpyja.repository.user;

import com.project.harpyja.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    @Query("""
                SELECT u.id AS userId, p.key AS projectKey
                FROM User u
                JOIN UserProject up ON u.id = up.user.id
                JOIN Project p ON up.project.id = p.id
                WHERE u.email = :email
            """)
    UserWithProjectKey findUserWithProjectKeyByEmail(@Param("email") String email);

    @Query("""
        SELECT u FROM User u
        WHERE (:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')))
        AND (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))
        """)
    Page<User> findByFilters(
            @Param("name") String name,
            @Param("email") String email,
            Pageable pageable
    );
}