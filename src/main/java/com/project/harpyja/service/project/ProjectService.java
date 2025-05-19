package com.project.harpyja.service.project;

import com.project.harpyja.entity.Project;

import java.util.List;

public interface ProjectService {

    Project createProject(Project project);

    List<Project> getUserProjects(String userId);
}