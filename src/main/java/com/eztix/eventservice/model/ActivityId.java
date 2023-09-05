package com.eztix.eventservice.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Objects;
@EqualsAndHashCode
@Embeddable
public class ActivityId implements Serializable {
    // generation strategies :?
    private int activityId;
    private int eventId;
    public ActivityId(int activityId, int eventId){
        this.activityId = activityId;
        this.eventId = eventId;
    }
}
