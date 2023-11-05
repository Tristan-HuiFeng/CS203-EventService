package com.eztix.eventservice.controller;

import com.eztix.eventservice.dto.request.NewEvent;
import com.eztix.eventservice.model.Event;

import com.eztix.eventservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    /**
     * Create an event.
     * 
     * @param newEvent a NewEvent datatype object containing the Event info to be created.
     * @return a ResponseEntity containing the created Event and an OK status.
     */
    @PostMapping("/api/v1/event")
    public ResponseEntity<Event> addEvent(@RequestBody NewEvent newEvent) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventService.addNewEvent(newEvent));

    }

    /**
     * Retrieve an event.
     * 
     * @param id a long value representing the unique identifier of the event to retrieve.
     * @return a ResponseEntity containing the retrieved Event and an OK status.
     */
    @CrossOrigin
    @GetMapping("/api/v1/event/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getEventById(id));
    }

    /**
     * Update an event.
     * 
     * @param id a long value representing the unique identifier of the event to update.
     * @param event an Event object containing the new Event info to be updated.
     * @return a ResponseEntity containing the updated Event and an OK status.
     */
    @PutMapping("/api/v1/event/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        event.setId(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.updateEvent(event));
    }

    /**
     * Retrieve a list of events based on filter criterion.
     * 
     * @param featuredOnly a boolean value representing if the events are featured.
     * @param category a String value representing the category of the events.
     * @param search a String value representing the search keyword(s).
     * @return a ResponseEntity containing an iterable of retrieved Event that matches the filter criterion above and an OK status.
     */
    @CrossOrigin
    @GetMapping("/api/v1/event")
    public ResponseEntity<Iterable<Event>> getAllEvent(@RequestParam(required = false, defaultValue = "false") boolean featuredOnly,
                                                       @RequestParam(required = false, defaultValue = "none") String category,
                                                       @RequestParam(required = false, defaultValue = "none") String search) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getAllEvents(featuredOnly, category, search));
    }


}