package com.project.harpyja.model.nymphicus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Gesture {
    @JsonProperty("actions")
    private List<Action> actions;
}
