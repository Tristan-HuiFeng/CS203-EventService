package com.eztix.eventservice.repository;

import com.eztix.eventservice.model.PurchaseRequestItem;

import org.springframework.data.repository.CrudRepository;

public interface PurchaseRequestItemRepository extends CrudRepository<PurchaseRequestItem, Long> {
}
