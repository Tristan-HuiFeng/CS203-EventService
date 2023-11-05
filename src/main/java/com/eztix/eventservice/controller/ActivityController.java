package com.eztix.eventservice.controller;

import com.eztix.eventservice.model.Activity;
import com.eztix.eventservice.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    /**
     * Retrieve an activity.
     * 
     * @param a long value representing the unique identifier of the activity to retrieve
     * @return a ResponseEntity containing the retrieved Activity and an OK status
     */
    @GetMapping("/api/v1/activity/{activityId}")
    public ResponseEntity<Activity> getActivityById(@PathVariable Long activityId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(activityService.getActivityById(activityId));
    }

//    @PostMapping("/api/v1/event/{eventId}/activity")
//    public ResponseEntity<Activity> addActivity(@PathVariable Long eventId, @RequestBody Activity activity) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(activityService.addNewActivity(eventId, activity));
//    }

    /**
     * Update the info of an activity.
     * 
     * @param activityId a long value representing the unique identifier of the activity to retrieve
     * @param activity an Activity object containing the new Activity info to be updated
     * @return a ResponseEntity containing the updated Activity and an OK status
     */
    @PutMapping("/api/v1/activity/{activityId}")
    public ResponseEntity<Activity> updateActivity(@PathVariable Long activityId, @RequestBody Activity activity ){
        activity.setId((activityId));
        return ResponseEntity.status(HttpStatus.OK).body(activityService.updateActivity(activity));
    }

    /**
     * Delete an activity.
     * 
     * @param activityId a long value representing the unique identifier of the activity to retrieve
     * @return a ResponseEntity containing a string that states the activity deleted and its unique identifier and an OK status
     */
    @DeleteMapping("/api/v1/activity/{activityId}")
    public ResponseEntity<String> deleteActivity(@PathVariable Long activityId) {
        activityService.deleteActivity(activityId);

        return ResponseEntity.status(HttpStatus.OK).body(String.format("activity with id %d has been deleted", activityId));
    }

}