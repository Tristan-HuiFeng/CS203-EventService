package com.eztix.eventservice.repository;

import com.eztix.eventservice.model.TicketSalesLimit;

import org.springframework.data.repository.CrudRepository;

public interface TicketSalesLimitRepository extends CrudRepository<TicketSalesLimit, Long> {
    Iterable<TicketSalesLimit> findBySalesRoundId(Long salesRoundId);
}
