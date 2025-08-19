package com.project.harpyja.repository.project;

import com.project.harpyja.entity.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class ProjectRepositoryJDBC {

    private static final Logger logger = LoggerFactory.getLogger(ProjectRepositoryJDBC.class);


    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Project> projectRowMapper = (rs, rowNum) -> {
        Project project = new Project();
        project.setId(rs.getString("id"));
        project.setName(rs.getString("name"));
        project.setKey(rs.getString("key"));
        project.setExpiration(rs.getObject("expiration", LocalDateTime.class));
        project.setType(rs.getString("type"));
        return project;
    };

    public ProjectRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Project save(Project project) {
        try {
            LocalDateTime now = LocalDateTime.now();
            project.setId(UUID.randomUUID().toString());
            project.setCreatedAt(now);
            project.setUpdatedAt(now);

            String sql = "INSERT INTO projects (id, name, key, type, expiration, organization_id, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            jdbcTemplate.update(sql,
                    project.getId(),
                    project.getName(),
                    project.getKey(),
                    project.getType(),
                    project.getExpiration(),
                    project.getOrganization().getId(),
                    Timestamp.valueOf(project.getCreatedAt()),
                    Timestamp.valueOf(project.getUpdatedAt()));

            return project;
        } catch (Exception e) {
            String errorMsg = "Erro ao salvar projeto: " + e.getMessage();
            logger.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    public List<Project> findProjectsByUserId(String userId) {
        String sql = "SELECT p.* FROM projects p " +
                "JOIN user_project up ON p.id = up.project_id " +
                "WHERE up.user_id = ?";

        try {
            return jdbcTemplate.query(sql, projectRowMapper, userId);
        } catch (DataAccessException e) {
            logger.error("Erro ao buscar projetos do usuário: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public Optional<Project> findById(String projectId) {
        String sql = "SELECT * FROM projects WHERE id = ?";
        try {
            Project project = jdbcTemplate.queryForObject(sql, projectRowMapper, projectId);
            return Optional.ofNullable(project);
        } catch (DataAccessException e) {
            logger.error("Erro ao buscar projeto por ID: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }


    public Page<Project> findByUserAndFilters(String userId, String type, String name, Pageable pageable) {
        StringBuilder sql = new StringBuilder("""
        SELECT p.* FROM projects p
        JOIN user_project up ON p.id = up.project_id
        WHERE up.user_id = ?
        """);

        List<Object> params = new ArrayList<>();
        params.add(userId);

        if (type != null && !type.isEmpty()) {
            sql.append(" AND p.type = ?");
            params.add(type);
        }
        if (name != null && !name.isEmpty()) {
            sql.append(" AND LOWER(p.name) LIKE LOWER(?)");
            params.add("%" + name + "%");
        }

        if (pageable.getSort().isSorted()) {
            sql.append(" ORDER BY ");
            pageable.getSort().forEach(order -> {
                sql.append(order.getProperty())
                        .append(" ")
                        .append(order.getDirection().name());
            });
        } else {
            sql.append(" ORDER BY p.created_at DESC");
        }

        sql.append(" LIMIT ? OFFSET ?");
        params.add(pageable.getPageSize());
        params.add(pageable.getOffset());

        List<Project> projects = jdbcTemplate.query(
                sql.toString(),
                params.toArray(),
                projectRowMapper
        );

        long total = countByUserAndFilters(userId, type, name);

        return new PageImpl<>(projects, pageable, total);
    }

    public long countByUserAndFilters(String userId, String type, String name) {
        StringBuilder sql = new StringBuilder("""
        SELECT COUNT(p.id) FROM projects p
        JOIN user_project up ON p.id = up.project_id
        WHERE up.user_id = ?
        """);

        List<Object> params = new ArrayList<>();
        params.add(userId);

        if (type != null && !type.isEmpty()) {
            sql.append(" AND p.type = ?");
            params.add(type);
        }
        if (name != null && !name.isEmpty()) {
            sql.append(" AND LOWER(p.name) LIKE LOWER(?)");
            params.add("%" + name + "%");
        }

        try {
            return jdbcTemplate.queryForObject(
                    sql.toString(),
                    params.toArray(),
                    Long.class
            );
        } catch (DataAccessException e) {
            logger.error("Erro ao contar projetos: {}", e.getMessage(), e);
            return 0;
        }
    }

}