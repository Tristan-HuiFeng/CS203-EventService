package com.eztix.eventservice.service;

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.TicketType;
import com.eztix.eventservice.repository.TicketTypeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketTypeService {

    private final TicketTypeRepository ticketTypeRepository;

    public TicketTypeService(TicketTypeRepository ticketTypeRepository) {
        this.ticketTypeRepository = ticketTypeRepository;
    }

    // Add new TicketType
    public TicketType addNewTicketType(TicketType ticketType) {
        return ticketTypeRepository.save(ticketType);
    }

    // Get TicketType by id
    public TicketType getTicketTypeById(Long id) {

        try {
            return ticketTypeRepository.findById(id)
                    .orElseThrow();
            // .orElseThrow(() -> new ResourceNotFoundException(String
            // .format("ticket type with id %d not found", id)));
        } catch (Exception e) {
            throw new ResourceNotFoundException(String
                    .format("ticket type with id %d not found", id));
        }
    }

    // Get all TicketTypes
    public Iterable<TicketType> getAllTicketTypes() {
        return ticketTypeRepository.findAll();
    }

    // Update TicketType
    @Transactional
    public TicketType updateTicketType(TicketType ticketType) {
        if (ticketType.getId() == null) {
            throw new RequestValidationException("ticket type id cannot be null");
        }

        // try {
        ticketTypeRepository.findById(ticketType.getId())
                .orElseThrow(() -> new ResourceNotFoundException(String
                        .format("ticket type with id %d not found", ticketType.getId())));
        return ticketTypeRepository.save(ticketType);
        // } catch (NullPointerException e) {
        // throw new ResourceNotFoundException(String
        // .format("ticket type with id %d not found", ticketType.getId()));
        // }
    }

    // Delete all TicketType
    public void deleteAllTicketTypes() {
        ticketTypeRepository.deleteAll();
    }

}
