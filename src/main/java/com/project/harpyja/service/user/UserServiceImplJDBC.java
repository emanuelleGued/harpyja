package com.project.harpyja.service.user;

import com.project.harpyja.entity.User;
import com.project.harpyja.repository.UserWithProjectKey;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.UUID;

@Transactional
public class UserServiceImplJDBC implements UserService {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(UUID.fromString(rs.getString("id")));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setEmailVerified(rs.getBoolean("email_verified"));
        user.setTermsAgreed(rs.getBoolean("terms_agreed"));
        return user;
    };

    public UserServiceImplJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        try {
            user.setEmailVerified(false);
            user.setTermsAgreed(true);

            String sql = "INSERT INTO users (id, name, email, password, email_verified, terms_agreed) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            // Usando PreparedStatementCreator para controle preciso dos tipos
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setObject(1, user.getId(), Types.OTHER);  // UUID como tipo OTHER
                ps.setString(2, user.getName());             // VARCHAR
                ps.setString(3, user.getEmail());            // VARCHAR
                ps.setString(4, user.getPassword());        // VARCHAR
                ps.setBoolean(5, user.isEmailVerified());    // BOOLEAN
                ps.setBoolean(6, user.isTermsAgreed());      // BOOLEAN
                return ps;
            });

            return user;
        } catch (DataAccessException e) {
            SQLException sqlEx = (SQLException) e.getCause();
            System.err.println("SQL State: " + sqlEx.getSQLState());
            System.err.println("Error Code: " + sqlEx.getErrorCode());
            System.err.println("Message: " + sqlEx.getMessage());
            throw new RuntimeException("Database error while creating user", e);
        }
    }

    @Override
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count > 0;
    }

    @Override
    public User updateEmailVerified(String email) {
        if (!emailExists(email)) {
            throw new RuntimeException("User not found with email: " + email);
        }

        String updateSql = "UPDATE users SET email_verified = true WHERE email = ?";
        int updatedRows = jdbcTemplate.update(updateSql, email);

        if (updatedRows == 0) {
            throw new RuntimeException("Failed to update email verification status");
        }

        return findUserByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    @Override
    public User findUserById(String userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, userRowMapper, userId);
    }

    @Override
    public void deleteUser(String userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, userId);
    }

    @Override
    public UserWithProjectKey findUserWithProjectKeyByEmail(String email) {
        return null;
    }

    // =========================
    // MÉTODOS EXTRAS PARA ONBOARDING
    // =========================

    @Override
    public void validateUserEmailForOnboarding(String email) {
        if (emailExists(email)) {
            throw new RuntimeException("Email already exists");
        }
    }

    @Override
    public User createUserForOnboarding(String email, String rawPassword) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(email);
        user.setPassword(rawPassword); // Deveria ser hash na prática
        user.setEmailVerified(false);
        user.setTermsAgreed(false);

        String sql = "INSERT INTO users (id, email, password, email_verified, terms_agreed) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                user.getId().toString(),
                user.getEmail(),
                user.getPassword(),
                user.isEmailVerified(),
                user.isTermsAgreed());

        return user;
    }

    @Override
    public User updateUserName(String userId, String newName) {
        String updateSql = "UPDATE users SET name = ? WHERE id = ?";
        jdbcTemplate.update(updateSql, newName, userId);

        return findUserById(userId);
    }

    // ==============================
    // NOVOS MÉTODOS PARA EQUIVALÊNCIA AO GO CODE
    // ==============================

    @Override
    public User updateUserService(String userId, User updates) {
        User existingUser = findUserById(userId);

        if (updates.getName() != null) {
            existingUser.setName(updates.getName());
            jdbcTemplate.update("UPDATE users SET name = ? WHERE id = ?",
                    updates.getName(), userId);
        }

        if (updates.getPassword() != null && !updates.getPassword().isEmpty()) {
            existingUser.setPassword(updates.getPassword());
            jdbcTemplate.update("UPDATE users SET password = ? WHERE id = ?",
                    updates.getPassword(), userId);
        }

        // Adicione outros campos conforme necessário

        return existingUser;
    }

    @Override
    public User findUserByEmailServiceOnboarding(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, email);

        if (!users.isEmpty()) {
            throw new RuntimeException("Email already exists");
        }

        return null;
    }

    public User findByEmail(String email) {
        return null;
    }

    @Override
    public User findUserByEmail(String email) throws EntityNotFoundException {
        String sql = "SELECT * FROM users WHERE email = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{email}, (rs, rowNum) -> {
                User user = new User();
                user.setId(UUID.fromString(rs.getString("id")));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setEmailVerified(rs.getBoolean("email_verified"));
                user.setTermsAgreed(rs.getBoolean("terms_agreed"));
                // Mapeie outros campos conforme necessário
                return user;
            });
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("User not found with email: " + email);
        }
    }
}