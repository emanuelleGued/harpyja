package com.project.harpyja.model.nymphicus;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ActivityGesture {
    @JsonProperty("activityName")
    private String activityName;

    @JsonProperty("gestures")
    private List<Gesture> gestures;

    // Getters e Setters
    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public List<Gesture> getGestures() {
        return gestures;
    }

    public void setGestures(List<Gesture> gestures) {
        this.gestures = gestures;
    }
}