package com.eztix.eventservice.repository;

import com.eztix.eventservice.model.PurchaseRequest;
import org.springframework.data.repository.CrudRepository;
import java.util.stream.Stream;

public interface PurchaseRequestRepository extends CrudRepository<PurchaseRequest, Long> {

    Stream<PurchaseRequest> findBySalesRoundId(Long salesRoundId);

    long countBySalesRoundId(Long salesRoundId);

    Stream<PurchaseRequest> findByCustomerIdOrderBySubmitDateTimeDesc(String customerId);


}
