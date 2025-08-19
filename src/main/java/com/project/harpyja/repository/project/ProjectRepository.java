package com.project.harpyja.repository.project;

import com.project.harpyja.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    @Query("SELECT p FROM Project p JOIN p.users u WHERE u.user.id = :userId")
    List<Project> findProjectsByUserId(@Param("userId") String userId);

    @Query("SELECT p FROM Project p JOIN p.users u WHERE u.user.id = :userId")
    Page<Project> findByUsers_UserId(@Param("userId") String userId, Pageable pageable);

    @Query("""
    SELECT p FROM Project p JOIN p.users u 
    WHERE u.user.id = :userId 
    AND (:type IS NULL OR p.type = :type)
    AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
    """)
    Page<Project> findByUserAndFilters(
            @Param("userId") String userId,
            @Param("type") String type,
            @Param("name") String name,
            Pageable pageable);
}