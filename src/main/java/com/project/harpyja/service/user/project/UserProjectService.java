package com.project.harpyja.service.user.project;

import com.project.harpyja.entity.Project;
import com.project.harpyja.entity.UserProject;
import com.project.harpyja.repository.user.project.UserProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserProjectService {

    private final UserProjectRepository userProjectRepository;

    @Autowired
    public UserProjectService(UserProjectRepository userProjectRepository) {
        this.userProjectRepository = userProjectRepository;
    }

    @Transactional
    public UserProject createUserProject(UserProject userProject) {
        return userProjectRepository.save(userProject);
    }

    public List<UserProject> getUserProjectsByUserId(UUID userId) {
        return userProjectRepository.findAllByUserId(userId);
    }

    public boolean userHasAccessToProject(UUID userId, UUID projectId) {
        return userProjectRepository.existsByUserIdAndProjectId(userId, projectId);
    }

    public List<UserProject> getProjectMembers(UUID projectId) {
        return userProjectRepository.findAllByProjectId(projectId);
    }

    public Optional<String> getLastUserProjectKey(UUID userId) {
        List<String> keys = userProjectRepository.findProjectKeysByUserIdOrdered(userId);
        return keys.isEmpty() ? Optional.empty() : Optional.of(keys.get(0));
    }

    public List<Project> getUserProjects(UUID userId) {
        return userProjectRepository.findAllProjectsByUserId(userId);
    }
}