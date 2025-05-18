package com.project.harpyja.repository.project;

import com.project.harpyja.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    // Encontrar projeto por chave única
    Optional<Project> findByKey(String key);

    // Encontrar todos os projetos de uma organização
    @Query("SELECT p FROM Project p WHERE p.organization.id = :organizationId")
    List<Project> findAllByOrganizationId(@Param("organizationId") UUID organizationId);

    // Encontrar projetos por nome (com like)
    @Query("SELECT p FROM Project p WHERE p.name LIKE %:name%")
    List<Project> findByNameContaining(@Param("name") String name);

    // Encontrar projetos que expirarão antes de uma determinada data
    @Query("SELECT p FROM Project p WHERE p.expiration < :date")
    List<Project> findExpiringBefore(@Param("date") LocalDateTime date);

    // Verificar se um projeto com determinado nome já existe em uma organização
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM Project p WHERE p.name = :name AND p.organization.id = :organizationId")
    boolean existsByNameAndOrganizationId(
            @Param("name") String name,
            @Param("organizationId") UUID organizationId);

    @Query("SELECT p FROM Project p JOIN p.users u WHERE u.user.id = :userId")
    List<Project> findProjectsByUserId(@Param("userId") UUID userId);
}