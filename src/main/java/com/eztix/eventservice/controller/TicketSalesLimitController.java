package com.eztix.eventservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eztix.eventservice.model.TicketSalesLimit;
import com.eztix.eventservice.service.TicketSalesLimitService;

@RestController
public class TicketSalesLimitController {

    private final TicketSalesLimitService ticketSalesLimitService;

    public TicketSalesLimitController(TicketSalesLimitService ticketSalesLimitService) {
        this.ticketSalesLimitService = ticketSalesLimitService;
    }

    //Add a new TicketSalesLimit
    @PostMapping("/api/v1/ticket-sales-limit")
    public ResponseEntity<TicketSalesLimit> addTicketSalesLimit (@RequestBody TicketSalesLimit ticketSalesLimit) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ticketSalesLimitService.addNewTicketSalesLimit(ticketSalesLimit));
    }

    //Get TicketSalesLimit by id
    @GetMapping ("/api/v1/ticket-sales-limit/{id}")
    public ResponseEntity<TicketSalesLimit> getTicketSalesLimitBySalesRoundIdAndTicketTypeId (@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
               .body(ticketSalesLimitService.getTicketSalesLimitById(id));
    }

    //Get all TicketSalesLimits
    @GetMapping("/api/v1/ticket-sales-limit")
    public ResponseEntity<Iterable<TicketSalesLimit>> getAllTicketSalesLimit () {
        return ResponseEntity.status(HttpStatus.OK)
              .body(ticketSalesLimitService.getAllTicketSalesLimits());
    }

    //Update TicketSalesLimit
    @PutMapping("/api/v1/ticket-sales-limit/{id}")
    public ResponseEntity<TicketSalesLimit> updateTicketSalesLimit (@PathVariable Long id, @RequestBody TicketSalesLimit ticketSalesLimit) {

        ticketSalesLimit.setId(id);
        return ResponseEntity.status(HttpStatus.OK)
              .body(ticketSalesLimitService.updateTicketSalesLimit(ticketSalesLimit));
    }
}

