package com.project.harpyja.service.project;

import com.project.harpyja.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProjectService {

    Project createProject(Project project);

    Page<Project> getUserProjects(String userId, String type, String name, Pageable pageable);

    List<Project> getUserProjects(String userId);

    Optional<Project> getProjectById(String projectId);
}
