package com.project.harpyja.model.nymphicus;

import java.util.List;

public class Gesture {
    private List<Action> actions;

    public Gesture() {}

    public Gesture(List<Action> actions) {
        this.actions = actions;
    }

    public List<Action> getActions() {
        return actions;
    }
    public void setActions(List<Action> actions) {
        this.actions = actions;
    }
}
