package com.eztix.eventservice.service;

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.PurchaseRequestItem;
import com.eztix.eventservice.repository.PurchaseRequestItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseRequestItemService {

    private final PurchaseRequestItemRepository purchaseRequestItemRepository;

    /**
     * 
     * @param purchaseRequestItem
     * @return
     */
    // Add new PurchaseRequestItem
    public PurchaseRequestItem addNewPurchaseRequestItem(PurchaseRequestItem purchaseRequestItem) {
        return purchaseRequestItemRepository.save(purchaseRequestItem);
    }

    /**
     * 
     * @param purchaseRequestItem_id
     * @return
     */
    // Get PurchaseRequestItem by id
    public PurchaseRequestItem getPurchaseRequestItemById(Long purchaseRequestItem_id) {
        return purchaseRequestItemRepository.findById(purchaseRequestItem_id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("purchase request item with id %d does not exist.", purchaseRequestItem_id))
        );

    }

    /**
     * 
     * @return
     */
    // Get all PurchaseRequestItem
    public Iterable<PurchaseRequestItem> getAllPurchaseRequestItems() {
        return purchaseRequestItemRepository.findAll();
    }

    /**
     * 
     * @param purchaseRequestItem
     * @return
     */
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

    /**
     * 
     * @param purchaseRequestItemList
     */
    public void validatePurchaseRequestItemList(List<PurchaseRequestItem> purchaseRequestItemList) {

    }

    /**
     * 
     */
    // Delete all PurchaseRequestItem
    public void deleteAllPurchaseRequestItems() {
        purchaseRequestItemRepository.deleteAll();
    }

}
