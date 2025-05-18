package com.project.harpyja.model.nymphicus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ActivityGesture {
    @JsonProperty("activityName")
    private String activityName;

    @JsonProperty("gestures")
    private List<Gesture> gestures;

}