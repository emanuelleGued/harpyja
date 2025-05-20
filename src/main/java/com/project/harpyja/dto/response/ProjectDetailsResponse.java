package com.project.harpyja.dto.response;

import com.project.harpyja.model.enums.ProjectRole;
import com.project.harpyja.model.nymphicus.Session;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ProjectDetailsResponse {
    private final String id;
    private final String name;
    private final String key;
    private final String type;
    private final LocalDateTime expiration;
    private final ProjectRole role;
    private final List<Session> sessions;

    public ProjectDetailsResponse(String id, String name, String key, String type,
                                  LocalDateTime expiration, ProjectRole role,
                                  List<Session> sessions) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.type = type;
        this.expiration = expiration;
        this.role = role;
        this.sessions = sessions;
    }
}