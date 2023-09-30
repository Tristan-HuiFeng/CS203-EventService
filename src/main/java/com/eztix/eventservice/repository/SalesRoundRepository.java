package com.eztix.eventservice.repository;

import com.eztix.eventservice.model.SalesRound;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SalesRoundRepository extends CrudRepository<SalesRound, Long> {

    Optional<Iterable<SalesRound>> findByEventId(Long eventId);
}
