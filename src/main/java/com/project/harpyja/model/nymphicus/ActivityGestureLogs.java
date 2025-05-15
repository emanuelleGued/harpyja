package com.project.harpyja.model.nymphicus;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ActivityGestureLogs {
    @JsonProperty("activities")
    private List<ActivityGesture> activities;

    // Getters e Setters
    public List<ActivityGesture> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityGesture> activities) {
        this.activities = activities;
    }
}