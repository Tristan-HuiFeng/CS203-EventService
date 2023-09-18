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

    public Activity getActivityById(Long id){
            return activityRepository.findById(id).orElseThrow(() ->
                    new ResourceNotFoundException(String.format("activity with id %d does not exist.", id))
            );

    }

    public Iterable<Activity> getAllActivity(){
        return activityRepository.findAll();
    }

    public Activity addNewActivity(Activity activity) {
        return activityRepository.save(activity);
    }
    @Transactional
    public Activity updateActivity(Activity activity){
        if (activity.getId() == null){
            throw new RequestValidationException("activity id cannot be null.");
        }

        activityRepository.findById(activity.getId()).orElseThrow(() ->
                new ResourceNotFoundException(String.format("activity with id %d does not exist.", activity.getId()))
        );

        return activityRepository.save(activity);
   }


}