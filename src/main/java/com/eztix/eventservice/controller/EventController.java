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
     * 
     * @param newEvent
     * @return
     */
    @PostMapping("/api/v1/event")
    public ResponseEntity<Event> addEvent(@RequestBody NewEvent newEvent) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventService.addNewEvent(newEvent));

    }

    /**
     * 
     * @param id
     * @return
     */
    @CrossOrigin
    @GetMapping("/api/v1/event/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getEventById(id));
    }

    /**
     * 
     * @param id
     * @param event
     * @return
     */
    @PutMapping("/api/v1/event/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        event.setId(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.updateEvent(event));
    }

    /**
     * 
     * @param featuredOnly
     * @param category
     * @param search
     * @return
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