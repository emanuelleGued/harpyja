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
import java.sql.Types;
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
            user.setEmailVerified(false);
            user.setTermsAgreed(true);

            String sql = "INSERT INTO users (id, name, email, password, email_verified, terms_agreed) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setObject(1, user.getId(), Types.OTHER);
                ps.setString(2, user.getName());
                ps.setString(3, user.getEmail());
                ps.setString(4, user.getPassword());
                ps.setBoolean(5, user.isEmailVerified());
                ps.setBoolean(6, user.isTermsAgreed());
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
            logger.info("Iniciando atualização do usuário ID: {}", userId);
            User existingUser = findUserById(userId);
            logger.debug("Usuário encontrado para atualização: {}", existingUser);

            boolean updated = false;

            if (updates.getName() != null && !updates.getName().equals(existingUser.getName())) {
                logger.debug("Atualizando nome para: {}", updates.getName());
                String updateNameSql = "UPDATE users SET name = ? WHERE id = ?";
                int rowsUpdated = jdbcTemplate.update(updateNameSql, updates.getName(), userId);
                logger.debug("Linhas afetadas na atualização do nome: {}", rowsUpdated);
                existingUser.setName(updates.getName());
                updated = true;
            }

            if (updates.getPassword() != null && !updates.getPassword().isEmpty()) {
                logger.debug("Atualizando senha");
                String updatePasswordSql = "UPDATE users SET password = ? WHERE id = ?";
                int rowsUpdated = jdbcTemplate.update(updatePasswordSql, updates.getPassword(), userId);
                logger.debug("Linhas afetadas na atualização da senha: {}", rowsUpdated);
                existingUser.setPassword(updates.getPassword());
                updated = true;
            }

            if (!updated) {
                logger.warn("Nenhum campo válido fornecido para atualização");
                throw new IllegalArgumentException("No valid fields provided for update");
            }

            logger.info("Usuário atualizado com sucesso. ID: {}", userId);
            return existingUser;

        } catch (DataAccessException e) {
            logger.error("ERRO DE BANCO DE DADOS ao atualizar usuário ID: {}", userId, e);

            // Log detalhado para SQLException
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