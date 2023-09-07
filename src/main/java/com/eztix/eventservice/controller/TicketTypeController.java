package com.eztix.eventservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eztix.eventservice.model.TicketType;
import com.eztix.eventservice.service.TicketTypeService;

@RestController
public class TicketTypeController {

    private final TicketTypeService ticketTypeService;

    public TicketTypeController(TicketTypeService ticketTypeService) {
        this.ticketTypeService = ticketTypeService;
    }

    //Add a new Ticket
    @PostMapping("/ticketType/add")
    public ResponseEntity<TicketType> addTicketType (@RequestBody TicketType ticketType) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ticketTypeService.createNewTicketType(ticketType));
    }

    //Get TicketType by id
    @GetMapping ("/ticketType/{ticketType_Id}")
    public ResponseEntity<TicketType> getTicketTypeById (@PathVariable Long ticketType_Id) {
        return ResponseEntity.status(HttpStatus.OK)
               .body(ticketTypeService.getTicketTypeById(ticketType_Id));
    }

    //Get all TicketTypes
    @GetMapping("/ticketType/getAll")
    public ResponseEntity<Iterable<TicketType>> getAllTicketType () {
        return ResponseEntity.status(HttpStatus.OK)
              .body(ticketTypeService.getAllTicketTypes());
    }

    //Update TicketType
    @PutMapping("/updateTicketType/{ticketType_Id}")
    public ResponseEntity<TicketType> updateTicketType (@PathVariable Long ticketType_Id, @RequestBody TicketType ticketType) {
        return ResponseEntity.status(HttpStatus.OK)
              .body(ticketTypeService.updateTicketType(ticketType));
    }

}