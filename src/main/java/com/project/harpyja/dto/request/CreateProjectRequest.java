package com.project.harpyja.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateProjectRequest {
    private String name;
    private String type;

}