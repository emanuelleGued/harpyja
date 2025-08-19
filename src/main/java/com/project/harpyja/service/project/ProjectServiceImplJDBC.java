package com.project.harpyja.service.project;


import com.project.harpyja.entity.Project;
import com.project.harpyja.repository.project.ProjectRepositoryJDBC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public Page<Project> getUserProjects(String userId, String type, String name, Pageable pageable) {
        return projectDAO.findByUserAndFilters(userId, type, name, pageable);
    }


    @Override
    public Optional<Project> getProjectById(String projectId) {
        return projectDAO.findById(projectId);
    }
}