package com.eztix.eventservice.repository;

import org.springframework.data.repository.CrudRepository;
import com.eztix.eventservice.model.Activity;

public interface ActivityRepository extends CrudRepository<Activity, Integer>{

    //SQL -> SELECT * FROM ACTIVITY WHERE ID = {input_iD}

}
