package com.project.harpyja.service.user;

import com.project.harpyja.entity.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
public class UserServiceImplJDBC implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImplJDBC.class);
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getString("id"));
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
            LocalDateTime now = LocalDateTime.now();
            user.setEmailVerified(false);
            user.setTermsAgreed(true);
            user.setCreatedAt(now);
            user.setUpdatedAt(now);

            String sql = "INSERT INTO users (id, name, email, password, email_verified, terms_agreed, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setObject(1, user.getId(), Types.OTHER);
                ps.setString(2, user.getName());
                ps.setString(3, user.getEmail());
                ps.setString(4, user.getPassword());
                ps.setBoolean(5, user.isEmailVerified());
                ps.setBoolean(6, user.isTermsAgreed());
                ps.setTimestamp(7, Timestamp.valueOf(user.getCreatedAt()));  // Converter LocalDateTime para Timestamp
                ps.setTimestamp(8, Timestamp.valueOf(user.getUpdatedAt()));  // Converter LocalDateTime para Timestamp
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

        LocalDateTime now = LocalDateTime.now();
        String updateSql = "UPDATE users SET email_verified = true, updated_at = ? WHERE email = ?";

        int updatedRows = jdbcTemplate.update(updateSql,
                Timestamp.valueOf(now),
                email
        );

        if (updatedRows == 0) {
            throw new RuntimeException("Failed to update email verification status");
        }

        User updatedUser = findUserByEmail(email);
        updatedUser.setUpdatedAt(now);

        return updatedUser;
    }

    @Override
    public List<User> findAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    @Override
    public User findUserById(String userId) {
        try {
            String sql = "SELECT * FROM users WHERE id = ?";
            return jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{userId},
                    userRowMapper
            );
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("User not found with ID: " + userId);
        }
    }

    @Override
    public void deleteUser(String userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, userId);
    }

    @Override
    public User updateUserService(String userId, User updates) {
        try {
            User existingUser = findUserById(userId);
            LocalDateTime now = LocalDateTime.now();
            boolean updated = false;

            if (updates.getName() != null && !updates.getName().equals(existingUser.getName())) {
                String updateNameSql = "UPDATE users SET name = ?, updated_at = ? WHERE id = ?";
                jdbcTemplate.update(updateNameSql, updates.getName(), Timestamp.valueOf(now), userId);
                existingUser.setName(updates.getName());
                updated = true;
            }

            if (updates.getPassword() != null && !updates.getPassword().isEmpty()) {
                String updatePasswordSql = "UPDATE users SET password = ?, updated_at = ? WHERE id = ?";
                jdbcTemplate.update(updatePasswordSql, updates.getPassword(), Timestamp.valueOf(now), userId);
                existingUser.setPassword(updates.getPassword());
                updated = true;
            }

            if (!updated) {
                throw new IllegalArgumentException("No valid fields provided for update");
            }

            existingUser.setUpdatedAt(now);
            return existingUser;

        } catch (DataAccessException e) {
            logger.error("ERRO DE BANCO DE DADOS ao atualizar usuário ID: {}", userId, e);

            if (e.getCause() instanceof SQLException sqlEx) {
                logger.error("SQL State: {}", sqlEx.getSQLState());
                logger.error("Error Code: {}", sqlEx.getErrorCode());
                logger.error("Message: {}", sqlEx.getMessage());
            }

            throw new RuntimeException("Database error while updating user: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("ERRO INESPERADO ao atualizar usuário ID: {}", userId, e);
            throw new RuntimeException("Unexpected error while updating user", e);
        }
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

    @Override
    public User findUserByEmail(String email) throws EntityNotFoundException {
        String sql = "SELECT * FROM users WHERE email = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{email}, (rs, rowNum) -> {
                User user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setEmailVerified(rs.getBoolean("email_verified"));
                user.setTermsAgreed(rs.getBoolean("terms_agreed"));
                return user;
            });
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("User not found with email: " + email);
        }
    }
}