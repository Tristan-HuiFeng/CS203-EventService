package com.eztix.eventservice.repository;

import com.eztix.eventservice.model.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, Long> {

}
