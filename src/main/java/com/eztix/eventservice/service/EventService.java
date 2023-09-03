package com.eztix.eventservice.service;

import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.repository.EventRepository;
import org.springframework.stereotype.Service;
import com.eztix.eventservice.repository.EventRepository;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event addNewEvent(Event event) {
        return eventRepository.save(event);
    }

    public Iterable<Event> getAllEvents() {
        return eventRepository.findAll();
    }

}
