package com.project.harpyja.service.user.project;

import com.project.harpyja.entity.UserProject;
import com.project.harpyja.model.enums.ProjectRole;
import com.project.harpyja.repository.user.project.UserProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public boolean existsByUserIdAndProjectId(String userId, String projectId) {
        return userProjectRepository.existsByUserIdAndProjectId(userId, projectId);
    }

    public Optional<ProjectRole> findUserRoleInProject(String userId, String projectId) {
        return userProjectRepository.findUserRoleInProject(userId, projectId);
    }
}