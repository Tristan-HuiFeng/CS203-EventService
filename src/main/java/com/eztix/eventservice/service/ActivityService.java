package com.eztix.eventservice.service;

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.Activity;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.repository.ActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final EventService eventService;

    public ActivityService(ActivityRepository activityRepository, EventService eventService) {
        this.activityRepository = activityRepository;
        this.eventService = eventService;
    }

    public Activity getActivityById(Long id){
            return activityRepository.findById(id).orElseThrow(() ->
                    new ResourceNotFoundException(String.format("activity with id %d does not exist.", id))
            );
    }

    public Activity addNewActivity(Long eventId, Activity activity) {
        Event event = eventService.getEventById(eventId);
        activity.setEvent(event);

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

        long activityId = activity.getId();
        Optional<Activity> result = activityRepository.findById(activityId);
        if (result.isEmpty()){
            throw new ResourceNotFoundException(String.format("activity with id %d not found.", activityId));
        }
        return activityRepository.save(activity);
   }

   public void deleteActivity(Long activityId) {
        if (activityId == null) {
            throw new RequestValidationException("activity id cannot be null.");
        }
        activityRepository.deleteById(activityId);
   }

}