package com.eztix.eventservice.repository;

import com.eztix.eventservice.model.SalesRound;
import com.eztix.eventservice.model.TicketType;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TicketTypeRepository extends CrudRepository<TicketType, Long> {
    Optional<Iterable<TicketType>> findByActivityId(Long activityId);
}
