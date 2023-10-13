package com.eztix.eventservice.controller;

import com.eztix.eventservice.dto.EventDTO;
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

    @PostMapping("/api/v1/event")
    public ResponseEntity<Event> addEvent(@RequestBody Event event) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventService.addNewEvent(event));

    }

    @CrossOrigin
    @GetMapping("/api/v1/event/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getEventById(id));
    }

    @PutMapping("/api/v1/event/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        event.setId(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.updateEvent(event));
    }

    @CrossOrigin
    @GetMapping("/api/v1/event")
    public ResponseEntity<Iterable<Event>> getAllEvent(@RequestParam(required = false, defaultValue = "false") boolean featuredOnly) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getAllEvents(featuredOnly));
    }

}