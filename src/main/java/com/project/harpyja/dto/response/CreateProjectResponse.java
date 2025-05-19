package com.project.harpyja.dto.response;

import lombok.Getter;

@Getter
public class CreateProjectResponse {
    private final String id;
    private final String name;
    private final String key;
    private final String type;

    public CreateProjectResponse(String id, String name, String key, String type) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.type = type;
    }

}