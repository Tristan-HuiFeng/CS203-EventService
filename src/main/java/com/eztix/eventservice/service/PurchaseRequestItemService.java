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
     * Create new purchase request item.
     * 
     * @param purchaseRequestItem the PurchaseRequestItem object to be added.
     * @return the created PurchaseRequestItem object.
     */
    public PurchaseRequestItem addNewPurchaseRequestItem(PurchaseRequestItem purchaseRequestItem) {
        return purchaseRequestItemRepository.save(purchaseRequestItem);
    }

    /**
     * Retrieve a purchase request item.
     * If there is no purchase request item with given "id", throw a ResourceNotFoundException.
     * 
     * @param purchaseRequestItem_id a long value representing the unique identifier of the PurchaseRequestItem to retrieve.
     * @return  the retrieved PurchaseRequestItem object.
     */
    // Get PurchaseRequestItem by id
    public PurchaseRequestItem getPurchaseRequestItemById(Long purchaseRequestItem_id) {
        return purchaseRequestItemRepository.findById(purchaseRequestItem_id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("purchase request item with id %d does not exist.", purchaseRequestItem_id))
        );

    }

    /**
     * Retrieves all purchase request items.
     * 
     * @return an iterable of retrieved PurchaseRequestItems.
     */
    public Iterable<PurchaseRequestItem> getAllPurchaseRequestItems() {
        return purchaseRequestItemRepository.findAll();
    }

    /**
     * Update a purchase request item.
     * If "id" is null, throw a RequestValidationException.
     * If there is no purchase request item with given "id", throw a ResourceNotFoundException.
     * 
     * @param purchaseRequestItem
     * @return
     */
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
     * Delete all purchase request items.
     */
    // Delete all PurchaseRequestItem
    public void deleteAllPurchaseRequestItems() {
        purchaseRequestItemRepository.deleteAll();
    }

}
