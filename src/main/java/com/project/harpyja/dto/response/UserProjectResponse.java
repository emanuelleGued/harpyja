package com.project.harpyja.dto.response;

import com.project.harpyja.model.enums.ProjectRole;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserProjectResponse {
    private final String id;
    private final String name;
    private final String key;
    private final String type;
    private final LocalDateTime expiration;
    private final ProjectRole role;

    public UserProjectResponse(String id, String name, String key, String type,
                               LocalDateTime expiration, ProjectRole role) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.type = type;
        this.expiration = expiration;
        this.role = role;
    }
}