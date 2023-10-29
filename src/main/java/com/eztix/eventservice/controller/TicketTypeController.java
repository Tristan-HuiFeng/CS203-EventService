package com.eztix.eventservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eztix.eventservice.model.TicketType;
import com.eztix.eventservice.service.TicketTypeService;

@RestController
@RequiredArgsConstructor
public class TicketTypeController {
    private final TicketTypeService ticketTypeService;

    //Add a new Ticket
//    @PostMapping("/api/v1/activity/{activityId}/ticket-type")
//    public ResponseEntity<TicketType> addTicketType (@PathVariable Long activityId, @RequestBody TicketType ticketType) {
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(ticketTypeService.addNewTicketType(activityId, ticketType));
//    }

    //Get TicketType by id
    @GetMapping ("/api/v1/ticket-type/{id}")
    public ResponseEntity<TicketType> getTicketTypeById (@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
               .body(ticketTypeService.getTicketTypeById(id));
    }

    @GetMapping("/api/v1/activity/{activityId}/ticket-type")
    public ResponseEntity<Iterable<TicketType>> getTicketTypeByActivityId (@PathVariable Long activityId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ticketTypeService.getTicketTypeByActivityId(activityId));
    }

    //Update TicketType
    @PutMapping("/api/v1/ticket-type/{id}")
    public ResponseEntity<TicketType> updateTicketType (@PathVariable Long id, @RequestBody TicketType ticketType) {
        ticketType.setId(id);
        return ResponseEntity.status(HttpStatus.OK)
              .body(ticketTypeService.updateTicketType(ticketType));
    }

}