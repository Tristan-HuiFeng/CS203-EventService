package com.eztix.eventservice.controller;

import com.eztix.eventservice.model.Activity;
import com.eztix.eventservice.service.ActivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/api/v1/activity/{activityId}")
    public ResponseEntity<Activity> getActivityById(@PathVariable Long activityId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(activityService.getActivityById(activityId));
    }

    @PostMapping("/api/v1/event/{eventId}/activity")
    public ResponseEntity<Activity> addActivity(@PathVariable Long eventId, @RequestBody Activity activity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(activityService.addNewActivity(eventId, activity));
    }

    @PutMapping("/api/v1/activity/{activityId}")
    public ResponseEntity<Activity> updateActivity(@PathVariable Long activityId, @RequestBody Activity activity ){
        activity.setId((activityId));
        return ResponseEntity.status(HttpStatus.OK).body(activityService.updateActivity(activity));
    }

    @DeleteMapping("/api/v1/activity/{activityId}")
    public ResponseEntity<String> deleteActivity(@PathVariable Long activityId) {
        activityService.deleteActivity(activityId);

        return ResponseEntity.status(HttpStatus.OK).body(String.format("activity with id %d has been deleted", activityId));
    }

}