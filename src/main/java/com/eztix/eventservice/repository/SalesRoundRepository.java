package com.eztix.eventservice.repository;

import com.eztix.eventservice.model.SalesRound;

import org.springframework.data.repository.CrudRepository;
import java.time.OffsetDateTime;
import java.util.Optional;

public interface SalesRoundRepository extends CrudRepository<SalesRound, Long> {

    Optional<Iterable<SalesRound>> findByEventId(Long activityId);

    Optional<SalesRound> findSalesRoundById(Long salesRoundId);

    Optional<SalesRound> findTop1ByEventIdAndRoundStartLessThanAndRoundEndGreaterThan(Long eventId, OffsetDateTime roundStart, OffsetDateTime roundEnd);

}
