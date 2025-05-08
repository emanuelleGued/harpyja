package com.project.harpyja.model.nymphicus;

public class Action {
    private String action;
    private String targetTime;
    private String coordinates;

    public Action() {}

    public Action(String action, String targetTime, String coordinates) {
        this.action = action;
        this.targetTime = targetTime;
        this.coordinates = coordinates;
    }

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
