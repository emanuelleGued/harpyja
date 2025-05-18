package com.project.harpyja.service.project;

import com.project.harpyja.entity.Project;
import com.project.harpyja.repository.project.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Optional<Project> getProjectById(UUID projectId) {
        return projectRepository.findById(projectId);
    }

    public Optional<Project> getProjectByKey(String key) {
        return projectRepository.findByKey(key);
    }

    public List<Project> getProjectsByOrganization(UUID organizationId) {
        return projectRepository.findAllByOrganizationId(organizationId);
    }

    public List<Project> getExpiringProjects(LocalDateTime date) {
        return projectRepository.findExpiringBefore(date);
    }

    @Transactional
    public void deleteProject(UUID projectId) {
        projectRepository.deleteById(projectId);
    }

    public boolean projectNameExistsInOrganization(String name, UUID organizationId) {
        return projectRepository.existsByNameAndOrganizationId(name, organizationId);
    }

    public List<Project> getUserProjects(UUID userId) {
        return projectRepository.findProjectsByUserId(userId);
    }
}
