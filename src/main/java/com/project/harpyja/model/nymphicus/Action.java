package com.project.harpyja.model.nymphicus;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Action {
    @JsonProperty("action")
    private String action;

    @JsonProperty("targetTime")
    private String targetTime;

    @JsonProperty("coordinates")
    private String coordinates;

    // Getters e Setters
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTargetTime() {
        return targetTime;
    }

    public void setTargetTime(String targetTime) {
        this.targetTime = targetTime;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }
}
