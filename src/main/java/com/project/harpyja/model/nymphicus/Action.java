package com.project.harpyja.model.nymphicus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Action {
    // Getters e Setters
    @JsonProperty("action")
    private String action;

    @JsonProperty("targetTime")
    private String targetTime;

    @JsonProperty("coordinates")
    private String coordinates;

}
