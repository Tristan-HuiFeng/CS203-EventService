package com.eztix.eventservice.controller;

import com.eztix.eventservice.model.Event;

import com.eztix.eventservice.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("event/add")
    public ResponseEntity<Event> addEvent(@RequestBody Event event) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.addNewEvent(event));

    }

    @GetMapping("event/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getEventById(id));
    }

    @PutMapping("/event/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        event.setId(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.updateEvent(event));
    }

    @GetMapping("/event")
    public ResponseEntity<Iterable<Event>>  getAllEvent() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getAllEvents());
    }

}