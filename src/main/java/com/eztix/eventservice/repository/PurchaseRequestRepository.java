package com.eztix.eventservice.repository;

import com.eztix.eventservice.model.PurchaseRequest;

import jakarta.persistence.OrderBy;
import org.springdoc.core.converters.models.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

import java.util.stream.Stream;

public interface PurchaseRequestRepository extends CrudRepository<PurchaseRequest, Long> {

    Stream<PurchaseRequest> findBySalesRoundId(Long salesRoundId);

    long countBySalesRoundId(Long salesRoundId);

    Stream<PurchaseRequest> findByCustomerIdOrderByStatusAsc(String customerId);
}
