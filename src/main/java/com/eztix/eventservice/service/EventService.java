package com.eztix.eventservice.service;

import com.eztix.eventservice.dto.EventDTO;
import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event addNewEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("event with id %d does not exist.", id))
        );
    }

    @Transactional
    public Event updateEvent(Event event) {
        if (event.getId() == null) {
            throw new RequestValidationException("event id cannot be null.");
        }

        eventRepository.findById(event.getId()).orElseThrow(() ->
                new ResourceNotFoundException(String.format("event with id %d does not exist.", event.getId()))
        );

       return eventRepository.save(event);
    }

    public Iterable<EventDTO> getAllEvents(boolean featuredOnly) {
        if (featuredOnly) {
            return eventRepository.findAllByIsFeaturedTrue();
        }
        else{
            return eventRepository.findAllEvent();
        }

    }

    public void deleteAll() {
        eventRepository.deleteAll();
    }

}
