package com.project.harpyja.model.nymphicus;


import java.util.List;

public class ActivityGestureLogs {
    private List<ActivityGesture> activities;

    public ActivityGestureLogs() {}

    public ActivityGestureLogs(List<ActivityGesture> activities) {
        this.activities = activities;
    }

    public List<ActivityGesture> getActivities() {
        return activities;
    }
    public void setActivities(List<ActivityGesture> activities) {
        this.activities = activities;
    }
}
