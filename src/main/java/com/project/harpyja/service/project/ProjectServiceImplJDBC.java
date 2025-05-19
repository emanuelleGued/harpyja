package com.project.harpyja.service.project;


import com.project.harpyja.entity.Project;
import com.project.harpyja.repository.project.ProjectRepositoryJDBC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectServiceImplJDBC implements ProjectService {

    private final ProjectRepositoryJDBC projectDAO;

    @Autowired
    public ProjectServiceImplJDBC(ProjectRepositoryJDBC projectDAO) {
        this.projectDAO = projectDAO;
    }

    @Override
    @Transactional
    public Project createProject(Project project) {
        return projectDAO.save(project);
    }

    @Override
    public List<Project> getUserProjects(String userId) {
        List<Project> projects = projectDAO.findProjectsByUserId(userId);
        return projects;
    }
}