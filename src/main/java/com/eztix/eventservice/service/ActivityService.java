package com.eztix.eventservice.service;

import com.eztix.eventservice.controller.ActivityController;
import com.eztix.eventservice.model.Activity;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.repository.ActivityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;

    public Optional<Activity> getActivity(int iD){
        return activityRepository.findById(iD);
    }

    public Iterable<Activity> getAllActivity(){
        return activityRepository.findAll();
    }

    public Activity addNewActivity(Activity activity) {
        return activityRepository.save(activity);
    }
}
