package com.project.harpyja.model.nymphicus;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ActivityGestureLogs {
    @JsonProperty("activities")
    private List<ActivityGesture> activities;

}