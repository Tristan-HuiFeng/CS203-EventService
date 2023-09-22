package com.eztix.eventservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eztix.eventservice.model.PurchaseRequest;
import com.eztix.eventservice.service.PurchaseRequestService;

@RestController
public class PurchaseRequestController {

    private final PurchaseRequestService purchaseRequestService;

    public PurchaseRequestController(PurchaseRequestService purchaseRequestService) {
        this.purchaseRequestService = purchaseRequestService;
    }

    //Add a new PurchaseRequest
    @PostMapping("/purchaseRequest/add")
    public ResponseEntity<PurchaseRequest> addPurchaseRequest (@RequestBody PurchaseRequest purchaseRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(purchaseRequestService.addNewPurchaseRequest(purchaseRequest));
    }

    //Get PurchaseRequest by id
    @GetMapping ("/purchaseRequest/{purchaseRequest_Id}")
    public ResponseEntity<PurchaseRequest> getPurchaseRequestById (@PathVariable Long purchaseRequest_Id) {
        return ResponseEntity.status(HttpStatus.OK)
               .body(purchaseRequestService.getPurchaseRequestById(purchaseRequest_Id));
    }

    //Get all PurchaseRequests
    @GetMapping("/purchaseRequest/getAll")
    public ResponseEntity<Iterable<PurchaseRequest>> getAllPurchaseRequest () {
        return ResponseEntity.status(HttpStatus.OK)
              .body(purchaseRequestService.getAllPurchaseRequests());
    }

    //Update PurchaseRequest
    @PutMapping("/updatePurchaseRequest/{purchaseRequest_Id}")
    public ResponseEntity<PurchaseRequest> updatePurchaseRequest (@PathVariable Long purchaseRequest_Id, @RequestBody PurchaseRequest purchaseRequest) {
        purchaseRequest.setId(purchaseRequest_Id);
        return ResponseEntity.status(HttpStatus.OK)
              .body(purchaseRequestService.updatePurchaseRequest(purchaseRequest));
    }

}