package com.project.harpyja.service.project;

import com.project.harpyja.entity.Project;
import com.project.harpyja.repository.project.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImplJPA implements ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImplJPA(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public List<Project> getUserProjects(String userId) {
        return projectRepository.findProjectsByUserId(userId);
    }

    @Override
    public Optional<Project> getProjectById(String projectId) {
        //TODO
        return Optional.empty();
    }

    @Override
    public Page<Project> getUserProjects(String userId, String type, String name, Pageable pageable) {
        return projectRepository.findByUserAndFilters(userId, type, name, pageable);
    }


}