package com.eztix.eventservice.controller;

import com.eztix.eventservice.dto.PurchaseRequestDTO;
import com.eztix.eventservice.dto.PurchaseRequestItemDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eztix.eventservice.model.PurchaseRequest;
import com.eztix.eventservice.service.PurchaseRequestService;

import java.util.List;

@RestController
public class PurchaseRequestController {

    private final PurchaseRequestService purchaseRequestService;

    public PurchaseRequestController(PurchaseRequestService purchaseRequestService) {
        this.purchaseRequestService = purchaseRequestService;
    }

    //Add a new PurchaseRequest
    @PostMapping("/api/v1/purchase-request")
    public ResponseEntity<PurchaseRequest> addPurchaseRequest (@RequestBody PurchaseRequestDTO purchaseRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(purchaseRequestService.addNewPurchaseRequest(purchaseRequest));
    }

    //Get PurchaseRequest by id
    @GetMapping ("/api/v1/purchase-request/{id}")
    public ResponseEntity<PurchaseRequest> getPurchaseRequestById (@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
               .body(purchaseRequestService.getPurchaseRequestById(id));
    }

    //Update PurchaseRequest
    @PutMapping("/api/v1/purchase-request/{id}")
    public ResponseEntity<PurchaseRequest> updatePurchaseRequest (@PathVariable Long id, @RequestBody PurchaseRequest purchaseRequest) {
        purchaseRequest.setId(id);
        return ResponseEntity.status(HttpStatus.OK)
              .body(purchaseRequestService.updatePurchaseRequest(purchaseRequest));
    }

}