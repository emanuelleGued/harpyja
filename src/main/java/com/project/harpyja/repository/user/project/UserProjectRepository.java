package com.project.harpyja.repository.user.project;

import com.project.harpyja.entity.Project;
import com.project.harpyja.entity.UserProject;
import com.project.harpyja.entity.UserProjectId;
import com.project.harpyja.model.enums.ProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProjectRepository extends JpaRepository<UserProject, UserProjectId> {

    // Encontrar todas as associações de um usuário
    @Query("SELECT up FROM UserProject up WHERE up.id.userId = :userId")
    List<UserProject> findAllByUserId(@Param("userId") UUID userId);

    // Encontrar todas as associações de um projeto
    @Query("SELECT up FROM UserProject up WHERE up.id.projectId = :projectId")
    List<UserProject> findAllByProjectId(@Param("projectId") UUID projectId);

    // Encontrar uma associação específica
    @Query("SELECT up FROM UserProject up WHERE up.id.userId = :userId AND up.id.projectId = :projectId")
    Optional<UserProject> findByUserIdAndProjectId(
            @Param("userId") UUID userId,
            @Param("projectId") UUID projectId);

    // Verificar se um usuário tem acesso a um projeto
    @Query("SELECT CASE WHEN COUNT(up) > 0 THEN true ELSE false END " +
            "FROM UserProject up WHERE up.id.userId = :userId AND up.id.projectId = :projectId")
    boolean existsByUserIdAndProjectId(
            @Param("userId") UUID userId,
            @Param("projectId") UUID projectId);

    // Encontrar todos os projetos de um usuário com uma determinada role
    @Query("SELECT up FROM UserProject up WHERE up.id.userId = :userId AND up.role = :role")
    List<UserProject> findByUserIdAndRole(
            @Param("userId") UUID userId,
            @Param("role") ProjectRole role);

    @Query("SELECT p.key FROM UserProject up JOIN up.project p WHERE up.id.userId = :userId")
    List<String> findAllProjectKeysByUserId(@Param("userId") UUID userId);

    @Query("SELECT up.project FROM UserProject up WHERE up.id.userId = :userId")
    List<Project> findAllProjectsByUserId(@Param("userId") UUID userId);

    @Query("SELECT p.key FROM UserProject up JOIN up.project p WHERE up.id.userId = :userId ORDER BY p.key DESC")
    List<String> findProjectKeysByUserIdOrdered(@Param("userId") UUID userId);
}