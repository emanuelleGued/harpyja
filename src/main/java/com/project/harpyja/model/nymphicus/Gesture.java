package com.project.harpyja.model.nymphicus;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Gesture {
    @JsonProperty("actions")
    private List<Action> actions;

    // Getters e Setters
    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }
}
