package com.eztix.eventservice.repository;

import com.eztix.eventservice.model.PurchaseRequest;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.repository.CrudRepository;

public interface PurchaseRequestRepository extends CrudRepository<PurchaseRequest, Long> {

    Optional<Stream<PurchaseRequest>> findBySalesRoundId (Long salesRoundId);

}
