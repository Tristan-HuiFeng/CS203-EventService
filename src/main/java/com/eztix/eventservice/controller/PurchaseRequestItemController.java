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
    @PostMapping("/purchaseRequestItem/add")
    public ResponseEntity<PurchaseRequestItem> addPurchaseRequestItem (@RequestBody PurchaseRequestItem purchaseRequestItem) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(purchaseRequestItemService.addNewPurchaseRequestItem(purchaseRequestItem));
    }

    //Get PurchaseRequestItem by id
    @GetMapping ("/purchaseRequestItem/{PRItemId}")
    public ResponseEntity<PurchaseRequestItem> getPurchaseRequestItemBySalesRoundIdAndTicketTypeId (@PathVariable Long PRItemId) {
        return ResponseEntity.status(HttpStatus.OK)
               .body(purchaseRequestItemService.getPurchaseRequestItemById(PRItemId));
    }

    //Get all PurchaseRequestItems
    @GetMapping("/purchaseRequestItem/getAll")
    public ResponseEntity<Iterable<PurchaseRequestItem>> getAllPurchaseRequestItem () {
        return ResponseEntity.status(HttpStatus.OK)
              .body(purchaseRequestItemService.getAllPurchaseRequestItems());
    }

    //Update PurchaseRequestItem
    @PutMapping("/updatePurchaseRequestItem/{PRItemId}")
    public ResponseEntity<PurchaseRequestItem> updatePurchaseRequestItem (@PathVariable Long PRItemId, @RequestBody PurchaseRequestItem purchaseRequestItem) {
        purchaseRequestItem.setId(PRItemId);
        return ResponseEntity.status(HttpStatus.OK)
              .body(purchaseRequestItemService.updatePurchaseRequestItem(purchaseRequestItem));
    }
}

