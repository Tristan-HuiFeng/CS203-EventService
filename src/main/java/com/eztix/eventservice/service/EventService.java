package com.eztix.eventservice.service;

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.repository.EventRepository;
import jakarta.validation.constraints.NotNull;
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
        if (event == null || event.getId() == null) {
            throw new RequestValidationException("event id cannot be null.");
        }

        eventRepository.findById(event.getId()).orElseThrow(() ->
                new ResourceNotFoundException(String.format("event with id %d does not exist.", event.getId()))
        );

       return eventRepository.save(event);
    }

    public Iterable<Event> getAllEvents(boolean featuredOnly) {
        if (featuredOnly) {
            return eventRepository.findAllByIsFeaturedTrueOrderByFeatureSequence();
        }
        else{
            return eventRepository.findAll();
        }

    }

    public void deleteAll() {
        eventRepository.deleteAll();
    }

}
