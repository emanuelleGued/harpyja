package com.project.harpyja.model.nymphicus;

import java.util.List;

public class ActivityGesture {
    private String activityName;
    private List<Gesture> gestures;

    public ActivityGesture() {}

    public ActivityGesture(String activityName, List<Gesture> gestures) {
        this.activityName = activityName;
        this.gestures = gestures;
    }

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
