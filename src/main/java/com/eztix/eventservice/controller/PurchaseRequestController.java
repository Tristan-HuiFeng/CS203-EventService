package com.eztix.eventservice.controller;

import com.eztix.eventservice.dto.PurchaseRequestCreation;
import com.eztix.eventservice.dto.PurchaseRequestDTO;
import com.eztix.eventservice.dto.confirmation.EventConfirmationDTO;
import com.eztix.eventservice.dto.prretrieval.PurchaseRequestItemRetrivalDTO;
import com.eztix.eventservice.dto.prretrieval.PurchaseRequestRetrievalDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.eztix.eventservice.model.PurchaseRequest;
import com.eztix.eventservice.service.PurchaseRequestService;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class PurchaseRequestController {

    private final PurchaseRequestService purchaseRequestService;

    //Add a new PurchaseRequest
    @CrossOrigin
    @PostMapping("/api/v1/purchase-request")
    public ResponseEntity<PurchaseRequestCreation> addPurchaseRequest (@RequestBody PurchaseRequestDTO purchaseRequest, Authentication authentication) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(purchaseRequestService.addNewPurchaseRequest(purchaseRequest, authentication.getName()));
    }

    @GetMapping ("/api/v1/purchase-request/{id}/confirmation")
    public ResponseEntity<EventConfirmationDTO> getPurchaseRequestConfirmation (@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(purchaseRequestService.getPurchaseRequestConfirmation(id));
    }

    @GetMapping("/api/v1/purchase-request")
    public ResponseEntity<List<PurchaseRequestRetrievalDTO>> getPurchaseRequestByUserId(Authentication authentication){
        return ResponseEntity.status(HttpStatus.OK)
                .body(purchaseRequestService.getPurchaseRequestByUserId(authentication.getName()));
    }

    @GetMapping ("/api/v1/purchase-request/{id}")
    public ResponseEntity<PurchaseRequest> getPurchaseRequestById (@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(purchaseRequestService.getPurchaseRequestById(id));
    }

    @GetMapping ("/api/v1/purchase-request/item/{id}")
    public ResponseEntity<PurchaseRequestItemRetrivalDTO> getPurchaseRequestItemById (@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(purchaseRequestService.getPurchaseRequestItemById(id));
    }

    //Update PurchaseRequest
    @PutMapping("/api/v1/purchase-request/{id}")
    public ResponseEntity<PurchaseRequest> updatePurchaseRequest (@PathVariable Long id, @RequestBody PurchaseRequest purchaseRequest) {
        purchaseRequest.setId(id);
        return ResponseEntity.status(HttpStatus.OK)
              .body(purchaseRequestService.updatePurchaseRequest(purchaseRequest));
    }

    @DeleteMapping("/api/v1/purchase-request/{id}")
    public ResponseEntity<String> deletePurchaseRequestById (@PathVariable Long id) {

        purchaseRequestService.deletePurchaseRequest(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body("deleted successfully");
    }

    @CrossOrigin
    @PostMapping("/api/v1/purchase-request/{id}/paid")
    public ResponseEntity<String> setPurchaseRequestPaid (Authentication authentication, Long id) {
        purchaseRequestService.setPurchaseRequestIsPaid(id);

        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

}