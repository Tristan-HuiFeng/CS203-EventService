package com.eztix.eventservice.repository;

import com.eztix.eventservice.exception.ResourceNotFoundException;
import org.springframework.data.repository.CrudRepository;
import com.eztix.eventservice.model.Activity;

public interface ActivityRepository extends CrudRepository<Activity, Long>{
    //SQL -> SELECT * FROM ACTIVITY WHERE ID = {input_iD}
}
