package com.eztix.eventservice.service;

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.PurchaseRequestItem;
import com.eztix.eventservice.repository.PurchaseRequestItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PurchaseRequestItemService {

    private final PurchaseRequestItemRepository purchaseRequestItemRepository;

    public PurchaseRequestItemService(PurchaseRequestItemRepository purchaseRequestItemRepository) {
        this.purchaseRequestItemRepository = purchaseRequestItemRepository;
    }

    // Add new PurchaseRequestItem
    public PurchaseRequestItem addNewPurchaseRequestItem(PurchaseRequestItem purchaseRequestItem) {
        return purchaseRequestItemRepository.save(purchaseRequestItem);
    }

    // Get PurchaseRequestItem by id
    public PurchaseRequestItem getPurchaseRequestItemById(Long purchaseRequestItem_id) {

        return purchaseRequestItemRepository.findById(purchaseRequestItem_id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("purchase request item with id %d does not exist.", purchaseRequestItem_id))
        );

    }

    // Get all PurchaseRequestItem
    public Iterable<PurchaseRequestItem> getAllPurchaseRequestItems() {
        return purchaseRequestItemRepository.findAll();
    }

    // Update PurchaseRequestItem
    @Transactional
    public PurchaseRequestItem updatePurchaseRequestItem(PurchaseRequestItem purchaseRequestItem) {
        if (purchaseRequestItem.getId() == null) {
            throw new RequestValidationException("purchase request item id cannot be null.");
        }

        purchaseRequestItemRepository.findById(purchaseRequestItem.getId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("purchase request item with id %d does not exist.", purchaseRequestItem.getId())));

        return purchaseRequestItemRepository.save(purchaseRequestItem);
    }

    // Delete all PurchaseRequestItem
    public void deleteAllPurchaseRequestItems() {
        purchaseRequestItemRepository.deleteAll();
    }

}
