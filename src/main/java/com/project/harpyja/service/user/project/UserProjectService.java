package com.project.harpyja.service.user.project;

import com.project.harpyja.entity.UserProject;
import com.project.harpyja.repository.user.project.UserProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
}