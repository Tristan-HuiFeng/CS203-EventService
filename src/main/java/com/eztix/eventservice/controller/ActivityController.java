package com.eztix.eventservice.controller;

import com.eztix.eventservice.model.Activity;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.service.ActivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@RestController
@RequestMapping("/activity")
public class ActivityController {
    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    // create own custom exception later on
    @GetMapping("/{activityId}")
    public ResponseEntity<Activity> getActivity(@PathVariable String activityId){
        try {
            long iD = Integer.parseInt(activityId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(activityService.getActivity(iD));

        } catch(NumberFormatException e){
            throw new RuntimeException("Invalid ID");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Activity>> getAllActivity(){
        return ResponseEntity.status(HttpStatus.OK).body(activityService.getAllActivity());
    }

    @PostMapping("/add")
    public ResponseEntity<Activity> addEvent(@RequestBody Activity activity) {

        Activity result = activityService.addNewActivity(activity);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(result);
    }

    @PutMapping("/{activityId}")
    public ResponseEntity<Activity> updateActivity(@PathVariable String activityId, @RequestBody Activity activity){

        try {
            long iD = Long.parseLong(activityId);
            activity.setActivityId(iD);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(activityService.updateActivity(activity));

        } catch(NumberFormatException e){
            throw new RuntimeException("Invalid ID");
        }
    }



}
