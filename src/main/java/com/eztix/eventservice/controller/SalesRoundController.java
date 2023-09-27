package com.eztix.eventservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eztix.eventservice.model.SalesRound;
import com.eztix.eventservice.service.SalesRoundService;

@RestController
public class SalesRoundController {

    private final SalesRoundService salesRoundService;

    public SalesRoundController(SalesRoundService salesRoundService) {
        this.salesRoundService = salesRoundService;
    }

    //Add a new SalesRound
    @PostMapping("/api/v1/event/{eventId}/sales-round")
    public ResponseEntity<SalesRound> addSalesRound (@PathVariable Long eventId, @RequestBody SalesRound salesRound) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(salesRoundService.addNewSalesRound(eventId, salesRound));
    }

    //Get SalesRound by id
    @GetMapping ("/api/v1/sales-round/{id}")
    public ResponseEntity<SalesRound> getSalesRoundById (@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
               .body(salesRoundService.getSalesRoundById(id));
    }

    @GetMapping ("/api/v1/event/{eventId}/sales-round")
    public ResponseEntity<Iterable<SalesRound>> getSalesRoundByEventId (@PathVariable Long eventId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(salesRoundService.getSalesRoundByEventId(eventId));
    }

    //Update SalesRound
    @PutMapping("/api/v1/sales-round/{id}")
    public ResponseEntity<SalesRound> updateSalesRound (@PathVariable Long id, @RequestBody SalesRound salesRound) {
        salesRound.setId(id);
        return ResponseEntity.status(HttpStatus.OK)
              .body(salesRoundService.updateSalesRound(salesRound));
    }

}