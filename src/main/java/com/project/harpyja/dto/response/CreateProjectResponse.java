package com.project.harpyja.dto.response;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateProjectResponse {
    // Getters
    private final UUID id;
    private final String name;
    private final String key;
    private final String type;

    // Construtor
    public CreateProjectResponse(UUID id, String name, String key, String type) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.type = type;
    }

}