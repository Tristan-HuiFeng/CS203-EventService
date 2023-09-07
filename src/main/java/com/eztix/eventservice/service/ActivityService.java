package com.eztix.eventservice.service;

import com.eztix.eventservice.controller.ActivityController;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.Activity;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.repository.ActivityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;

    public Activity getActivity(Long iD){
        return activityRepository.findById(iD).orElseThrow(() -> new ResourceNotFoundException(String.format("Activity with id %d does not exist", iD)));
    }

    public Iterable<Activity> getAllActivity(){
        return activityRepository.findAll();
    }

    public Activity addNewActivity(Activity activity) {
        return activityRepository.save(activity);
    }
    @Transactional
    public Activity updateActivity(Activity activity){
        long activityId = activity.getActivityId();
        Optional<Activity> result = activityRepository.findById(activityId);
        if (result.isEmpty()){
            throw new ResourceNotFoundException(String.format("Activity with id %d not found!", activityId));
        }
        return activityRepository.save(activity);
   }


}

