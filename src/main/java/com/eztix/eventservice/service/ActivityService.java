package com.eztix.eventservice.service;

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.Activity;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;

    /**
     * 
     * @param id
     * @return
     */
    public Activity getActivityById(Long id){
            return activityRepository.findById(id).orElseThrow(() ->
                    new ResourceNotFoundException(String.format("activity with id %d does not exist.", id))
            );
    }

    /**
     * 
     * @param event
     * @param activity
     * @return
     */
    public Activity addNewActivity(Event event, Activity activity) {
        activity.setEvent(event);

        return activityRepository.save(activity);
    }

    /**
     * 
     * @param activity
     * @return
     */
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

   /**
    * 
    * @param activityId
    */
   public void deleteActivity(Long activityId) {
        if (activityId == null) {
            throw new RequestValidationException("activity id cannot be null.");
        }
        activityRepository.deleteById(activityId);
   }

}