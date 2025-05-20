package com.project.harpyja.service.project;

import com.project.harpyja.entity.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectService {

    Project createProject(Project project);

    List<Project> getUserProjects(String userId);

    Optional<Project> getProjectById(String projectId);
}