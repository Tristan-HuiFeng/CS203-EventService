package com.eztix.eventservice.controller;

import com.eztix.eventservice.dto.request.NewSalesRound;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eztix.eventservice.model.SalesRound;
import com.eztix.eventservice.service.SalesRoundService;

@RestController
@RequiredArgsConstructor
public class SalesRoundController {
    private final SalesRoundService salesRoundService;

    /**
     * Create a sales round.
     * 
     * @param eventId id a long value representing the unique identifier of the event related to the SalesRounds to be created.
     * @param salesRounds a NewSalesRound array containing the info of the SalesRounds to be created.
     * @return a ResponseEntity containing an array of the created SalesRounds and an OK status.
     */
    @PostMapping("/api/v1/event/{eventId}/sales-round")
    public ResponseEntity<SalesRound[]> addSalesRound(@PathVariable Long eventId,
            @RequestBody NewSalesRound[] salesRounds) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(salesRoundService.addSalesRounds(eventId, salesRounds));
    }

    //Get SalesRound by id
    /**
     * Retrieve a sales round.
     * 
     * @param id a long value representing the unique identifier of the SalesRound to be retrieved.
     * @return a ResponseEntity containing the retrieved SalesRound and an OK status.
     */
    @GetMapping ("/api/v1/sales-round/{id}")
    public ResponseEntity<SalesRound> getSalesRoundById (@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(salesRoundService.getSalesRoundById(id));
    }

    /**
     * Retrieve an iterable of sales rounds based on eventId.
     * 
     * @param eventId a long value representing the unique identifier of the event related to the SalesRounds to be retrieved.
     * @return a ResponseEntity containing the iterable of retrieved SalesRounds and an OK status.
     */
    @GetMapping ("/api/v1/event/{eventId}/sales-round")
    public ResponseEntity<Iterable<SalesRound>> getSalesRoundByEventId (@PathVariable Long eventId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(salesRoundService.getSalesRoundByEventId(eventId));
    }

    //Update SalesRound
    /**
     * Update a sales round.
     * 
     * @param id a long value representing the unique identifier of the SalesRound to be updated.
     * @param salesRound a SalesRound object containing the new SalesRound info to be updated.
     * @return a ResponseEntity containing the updated SalesRound and an OK status.
     */
    @PutMapping("/api/v1/sales-round/{id}")
    public ResponseEntity<SalesRound> updateSalesRound(@PathVariable Long id, @RequestBody SalesRound salesRound) {
        salesRound.setId(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(salesRoundService.updateSalesRound(salesRound));
    }

    // @PostMapping("/api/v1/event/{eventId}/sales-round/{salesRoundId}/process-purchase")
    // public ResponseEntity<Void> processPurchaseRequests(@PathVariable Long eventId, @PathVariable Long salesRoundId) {
    //     salesRoundService.processPurchaseRequests(salesRoundId);
    //     return ResponseEntity.status(HttpStatus.OK).build();
    // }

}