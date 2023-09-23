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
    @PostMapping("/ticketSalesLimit/add")
    public ResponseEntity<TicketSalesLimit> addTicketSalesLimit (@RequestBody TicketSalesLimit ticketSalesLimit) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ticketSalesLimitService.addNewTicketSalesLimit(ticketSalesLimit));
    }

    //Get TicketSalesLimit by id
    @GetMapping ("/ticketSalesLimit/{ticketSalesLimit_id}")
    public ResponseEntity<TicketSalesLimit> getTicketSalesLimitBySalesRoundIdAndTicketTypeId (@PathVariable Long ticketSalesLimit_id) {
        return ResponseEntity.status(HttpStatus.OK)
               .body(ticketSalesLimitService.getTicketSalesLimitById(ticketSalesLimit_id));
    }

    //Get all TicketSalesLimits
    @GetMapping("/ticketSalesLimit/getAll")
    public ResponseEntity<Iterable<TicketSalesLimit>> getAllTicketSalesLimit () {
        return ResponseEntity.status(HttpStatus.OK)
              .body(ticketSalesLimitService.getAllTicketSalesLimits());
    }

    //Update TicketSalesLimit
    @PutMapping("/updateTicketSalesLimit/{ticket_sales_limit_id}")
    public ResponseEntity<TicketSalesLimit> updateTicketSalesLimit (@PathVariable Long ticket_sales_limit_id, @RequestBody TicketSalesLimit ticketSalesLimit) {

        ticketSalesLimit.setId(ticket_sales_limit_id);
        return ResponseEntity.status(HttpStatus.OK)
              .body(ticketSalesLimitService.updateTicketSalesLimit(ticketSalesLimit));
    }
}

