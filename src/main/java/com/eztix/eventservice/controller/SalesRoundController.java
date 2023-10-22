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

    //Add a new SalesRound
    @PostMapping("/api/v1/event/{eventId}/sales-round")
    public ResponseEntity<SalesRound[]> addSalesRound (@PathVariable Long eventId, @RequestBody NewSalesRound[] salesRounds) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(salesRoundService.addSalesRounds(eventId, salesRounds));
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