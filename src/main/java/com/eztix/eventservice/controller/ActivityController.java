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

    @GetMapping("activity/{activityId}")
    public ResponseEntity<Activity> getActivityById(@PathVariable Long activityId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(activityService.getActivityById(activityId));
    }

    @GetMapping("activity/all")
    public ResponseEntity<Iterable<Activity>> getAllActivity(){
        return ResponseEntity.status(HttpStatus.OK).body(activityService.getAllActivity());
    }

    @PostMapping("activity/add")
    public ResponseEntity<Activity> addActivity(@RequestBody Activity activity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(activityService.addNewActivity(activity));
    }

    @PutMapping("activity/{activityId}")
    public ResponseEntity<Activity> updateActivity(@PathVariable Long activityId, @RequestBody Activity activity ){
        activity.setId((activityId));
        return ResponseEntity.status(HttpStatus.OK).body(activityService.updateActivity(activity));
    }



}