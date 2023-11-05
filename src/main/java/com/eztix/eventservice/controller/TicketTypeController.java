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

    /**
     * Retrieve a ticket type.
     * 
     * @param id a long value representing the unique identifier of the TicketType to retrieve.
     * @return a ResponseEntity containing the retrieved TicketType and an OK status.
     */
    @GetMapping ("/api/v1/ticket-type/{id}")
    public ResponseEntity<TicketType> getTicketTypeById (@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
               .body(ticketTypeService.getTicketTypeById(id));
    }

    /**
     * Retrieve an iterable of ticket type based on activityId.
     * 
     * @param activityId a long value representing the unique identifier of the Activity associated with the TicketTypes to retrieve.
     * @return a ResponseEntity containing an iterable of retrieved TicketTypes and an OK status.
     */
    @GetMapping("/api/v1/activity/{activityId}/ticket-type")
    public ResponseEntity<Iterable<TicketType>> getTicketTypeByActivityId (@PathVariable Long activityId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ticketTypeService.getTicketTypeByActivityId(activityId));
    }

    /**
     * Update a ticket type.
     * 
     * @param id a long value representing the unique identifier of the TicketType to update.
     * @param ticketType a TicketType object containing the new TicketType info to be updated.
     * @return a ResponseEntity containing the updated TicketType and an OK status.
     */
    @PutMapping("/api/v1/ticket-type/{id}")
    public ResponseEntity<TicketType> updateTicketType (@PathVariable Long id, @RequestBody TicketType ticketType) {
        ticketType.setId(id);
        return ResponseEntity.status(HttpStatus.OK)
              .body(ticketTypeService.updateTicketType(ticketType));
    }

}