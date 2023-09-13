package com.eztix.eventservice.service;

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.Activity;
import com.eztix.eventservice.repository.ActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public Activity getActivityById(Long iD){
        return activityRepository.findById(iD).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Activity with id %d does not exist", iD)));
    }

    public Iterable<Activity> getAllActivity(){
        return activityRepository.findAll();
    }

    public Activity addNewActivity(Activity activity) {
        return activityRepository.save(activity);
    }
    @Transactional
    public Activity updateActivity(Activity activity){
        if (activity.getActivityId() == null){
            throw new RequestValidationException("Activity id cannot be null");
        }
        long activityId = activity.getActivityId();
        Optional<Activity> result = activityRepository.findById(activityId);
        if (result.isEmpty()){
            throw new ResourceNotFoundException(String.format("Activity with id %d not found!", activityId));
        }
        return activityRepository.save(activity);
   }


}

