package com.project.harpyja.repository.user.project;

import com.project.harpyja.entity.UserProject;
import com.project.harpyja.entity.UserProjectId;
import com.project.harpyja.model.enums.ProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProjectRepository extends JpaRepository<UserProject, UserProjectId> {

    @Query("SELECT COUNT(up) > 0 FROM UserProject up WHERE up.id.userId = :userId AND up.id.projectId = :projectId")
    boolean existsByUserIdAndProjectId(@Param("userId") String userId, @Param("projectId") String projectId);

    @Query("SELECT up.role FROM UserProject up WHERE up.id.userId = :userId AND up.id.projectId = :projectId")
    Optional<ProjectRole> findUserRoleInProject(@Param("userId") String userId, @Param("projectId") String projectId);
}