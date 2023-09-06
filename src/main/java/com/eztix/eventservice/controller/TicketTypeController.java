package com.eztix.eventservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eztix.eventservice.model.TicketType;

@RestController
public class TicketTypeController {

    private final TicketTypeService ticketTypeService;

    public TicketTypeController(TicketTypeService ticketTypeService) {
        this.ticketTypeService = ticketTypeService;
    }

    @PostMapping("/createTicketType")
    public ResponseEntity<TicketType> create (@RequestBody TicketType request) {
        
    }

    @PutMapping("/updateTicketType/{ticketType_Id}")

    @GetMapping ("/findTicketType/{ticketType_Id}")

    @GetMapping("/findAllTicketType")

}