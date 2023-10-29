package com.eztix.eventservice.controller;

import com.eztix.eventservice.dto.PurchaseRequestCreation;
import com.eztix.eventservice.dto.PurchaseRequestDTO;
import com.eztix.eventservice.dto.PurchaseRequestItemDTO;
import com.eztix.eventservice.dto.confirmation.EventConfirmationDTO;
import com.eztix.eventservice.dto.prretrieval.PurchaseRequestRetrievalDTO;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
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

    //Update PurchaseRequest
    @PutMapping("/api/v1/purchase-request/{id}")
    public ResponseEntity<PurchaseRequest> updatePurchaseRequest (@PathVariable Long id, @RequestBody PurchaseRequest purchaseRequest) {
        purchaseRequest.setId(id);
        return ResponseEntity.status(HttpStatus.OK)
              .body(purchaseRequestService.updatePurchaseRequest(purchaseRequest));
    }

}