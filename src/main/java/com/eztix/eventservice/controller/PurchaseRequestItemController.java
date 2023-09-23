package com.eztix.eventservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eztix.eventservice.model.PurchaseRequestItem;
import com.eztix.eventservice.service.PurchaseRequestItemService;

@RestController
public class PurchaseRequestItemController {

    private final PurchaseRequestItemService purchaseRequestItemService;

    public PurchaseRequestItemController(PurchaseRequestItemService purchaseRequestItemService) {
        this.purchaseRequestItemService = purchaseRequestItemService;
    }

    //Add a new PurchaseRequestItem
    @PostMapping("/api/v1/purchase-request-item")
    public ResponseEntity<PurchaseRequestItem> addPurchaseRequestItem (@RequestBody PurchaseRequestItem purchaseRequestItem) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(purchaseRequestItemService.addNewPurchaseRequestItem(purchaseRequestItem));
    }

    //Get PurchaseRequestItem by id
    @GetMapping ("/api/v1/purchase-request-item/{id}")
    public ResponseEntity<PurchaseRequestItem> getPurchaseRequestItemBySalesRoundIdAndTicketTypeId (@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
               .body(purchaseRequestItemService.getPurchaseRequestItemById(id));
    }

    //Get all PurchaseRequestItems
    @GetMapping("/api/v1/purchase-request-item")
    public ResponseEntity<Iterable<PurchaseRequestItem>> getAllPurchaseRequestItem () {
        return ResponseEntity.status(HttpStatus.OK)
              .body(purchaseRequestItemService.getAllPurchaseRequestItems());
    }

    //Update PurchaseRequestItem
    @PutMapping("/api/v1/purchase-request-item/{id}")
    public ResponseEntity<PurchaseRequestItem> updatePurchaseRequestItem (@PathVariable Long id, @RequestBody PurchaseRequestItem purchaseRequestItem) {
        purchaseRequestItem.setId(id);
        return ResponseEntity.status(HttpStatus.OK)
              .body(purchaseRequestItemService.updatePurchaseRequestItem(purchaseRequestItem));
    }
}

