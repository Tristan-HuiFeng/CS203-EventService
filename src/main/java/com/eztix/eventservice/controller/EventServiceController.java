package com.eztix.eventservice.controller;

import com.eztix.eventservice.model.Event;

import com.eztix.eventservice.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EventServiceController {

    private final EventService eventService;

    public EventServiceController(EventService eventService) {
        this.eventService = eventService;
    }


   /* @PostMapping("event/add")
    public @ResponseBody String addEvent(@RequestParam String name, @RequestParam String category, @RequestParam String artist, @RequestParam String description,
                                         @RequestParam String bannerURL, @RequestParam String seatMapURL) {
        Event newEvent = new Event();
        newEvent.setName(name);
        newEvent.setCategory(category);
        newEvent.setArtist(artist);
        newEvent.setDescription(description);
        newEvent.setBannerURL(bannerURL);
        newEvent.setSeatMapURL(seatMapURL);
        eventRepository.save(newEvent);

        return "added";
    }*/

    @PostMapping("event/add")
    public ResponseEntity<Event> addEvent(@RequestParam Event event) {

        Event result = eventService.addNewEvent(event);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(result);

    }

    @GetMapping(path="/event/all")
    public ResponseEntity<Iterable<Event>>  getAllEvent() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getAllEvents());
    }

}