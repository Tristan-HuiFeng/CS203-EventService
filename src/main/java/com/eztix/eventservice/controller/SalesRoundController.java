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
    @PostMapping("/salesRound/add")
    public ResponseEntity<SalesRound> addSalesRound (@RequestBody SalesRound salesRound) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(salesRoundService.addNewSalesRound(salesRound));
    }

    //Get SalesRound by id
    @GetMapping ("/salesRound/{salesRound_Id}")
    public ResponseEntity<SalesRound> getSalesRoundById (@PathVariable Long salesRound_Id) {
        return ResponseEntity.status(HttpStatus.OK)
               .body(salesRoundService.getSalesRoundById(salesRound_Id));
    }

    //Get all SalesRounds
    @GetMapping("/salesRound/getAll")
    public ResponseEntity<Iterable<SalesRound>> getAllSalesRound () {
        return ResponseEntity.status(HttpStatus.OK)
              .body(salesRoundService.getAllSalesRounds());
    }

    //Update SalesRound
    @PutMapping("/updateSalesRound/{salesRound_Id}")
    public ResponseEntity<SalesRound> updateSalesRound (@PathVariable Long salesRound_Id, @RequestBody SalesRound salesRound) {
        salesRound.setId(salesRound_Id);
        return ResponseEntity.status(HttpStatus.OK)
              .body(salesRoundService.updateSalesRound(salesRound));
    }

}