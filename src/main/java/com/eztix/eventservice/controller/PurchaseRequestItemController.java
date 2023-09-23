package com.eztix.eventservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eztix.eventservice.model.PurchaseRequestItem;
import com.eztix.eventservice.model.PurchaseRequestItemId;
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
    @GetMapping ("/purchaseRequestItem/{purchaseRequestItem_id}")
    public ResponseEntity<PurchaseRequestItem> getPurchaseRequestItemBySalesRoundIdAndTicketTypeId (@PathVariable Long purchaseRequestItem_id) {
        return ResponseEntity.status(HttpStatus.OK)
               .body(purchaseRequestItemService.getPurchaseRequestItemById(purchaseRequestItem_id));
    }

    //Get all PurchaseRequestItems
    @GetMapping("/purchaseRequestItem/getAll")
    public ResponseEntity<Iterable<PurchaseRequestItem>> getAllPurchaseRequestItem () {
        return ResponseEntity.status(HttpStatus.OK)
              .body(purchaseRequestItemService.getAllPurchaseRequestItems());
    }

    //Update PurchaseRequestItem
    @PutMapping("/updatePurchaseRequestItem/{purchaseRequestItem_id}")
    public ResponseEntity<PurchaseRequestItem> updatePurchaseRequestItem (@PathVariable Long purchaseRequestItem_id, @RequestBody PurchaseRequestItem purchaseRequestItem) {
        PurchaseRequestItemId id = purchaseRequestItem.getId();
        id.setId(purchaseRequestItem_id);
        purchaseRequestItem.setId(id);
        return ResponseEntity.status(HttpStatus.OK)
              .body(purchaseRequestItemService.updatePurchaseRequestItem(purchaseRequestItem));
    }
}

