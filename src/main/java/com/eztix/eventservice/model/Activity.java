package com.eztix.eventservice.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Activity {
    // see lect slides on how to solve this composite key
    private int eventId;
    private int activityId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String location;

}
