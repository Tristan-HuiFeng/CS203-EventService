package com.eztix.eventservice.repository;

import com.eztix.eventservice.model.Activity;
import org.springframework.data.repository.CrudRepository;

public interface ActivityRepository extends CrudRepository<Activity, Long> {

}
