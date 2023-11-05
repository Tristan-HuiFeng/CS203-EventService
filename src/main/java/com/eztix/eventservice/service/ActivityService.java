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
     * Retrieve an activity.
     * 
     * @param id a long value representing the unique identifier of the Activity to retrieve.
     * @return the updated Activity object.
     */
    public Activity getActivityById(Long id){
            return activityRepository.findById(id).orElseThrow(() ->
                    new ResourceNotFoundException(String.format("activity with id %d does not exist.", id))
            );
    }

    /**
     * Create an activity.
     * 
     * @param event an Event object associated with the Activity to be created.
     * @param activity an Activity object to associate with the Event.
     * @return the created Activity object.
     */
    public Activity addNewActivity(Event event, Activity activity) {
        activity.setEvent(event);

        return activityRepository.save(activity);
    }

    /**
     * Update an activity.
     * If "id" is null, throw a RequestValidationException.
     * If there is no activity with given "id", throw a ResourceNotFoundException.
     * 
     * @param activity an Activity object containing the new Activity info to be updated.
     * @return the updated Activity object.
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
    * Delete an activity.
    *  If "id" is null, throw a RequestValidationException.
    * 
    * @param activityId a long value representing the unique identifier of the Activity to retrieve.
    */
   public void deleteActivity(Long activityId) {
        if (activityId == null) {
            throw new RequestValidationException("activity id cannot be null.");
        }
        activityRepository.deleteById(activityId);
   }

}