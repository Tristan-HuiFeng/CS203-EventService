package com.eztix.eventservice.service;

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.Activity;
import com.eztix.eventservice.model.TicketType;
import com.eztix.eventservice.repository.TicketTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketTypeService {

    private final TicketTypeRepository ticketTypeRepository;

    // Add new TicketType
    public TicketType addNewTicketType(Activity activity, TicketType ticketType) {
        ticketType.setActivity(activity);
        return ticketTypeRepository.save(ticketType);

    }

    // Get TicketType by id
    public TicketType getTicketTypeById(Long id) {

        return ticketTypeRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("ticket type with id %d does not exist.", id))
        );

    }

    // Get TicketType iterable by activityId
    public Iterable<TicketType> getTicketTypeByActivityId(Long activityId) {

        return ticketTypeRepository.findByActivityId(activityId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("activity with id %d does not have any ticket type.", activityId))
        );

    }

    // Update TicketType
    @Transactional
    public TicketType updateTicketType(TicketType ticketType) {
        if (ticketType.getId() == null) {
            throw new RequestValidationException("ticket type id cannot be null.");
        }

        ticketTypeRepository.findById(ticketType.getId()).orElseThrow(() ->
                new ResourceNotFoundException(String.format("ticket type with id %d does not exist.", ticketType.getId()))
        );

        return ticketTypeRepository.save(ticketType);
    }

    // Delete all TicketType
    public void deleteAllTicketTypes() {
        ticketTypeRepository.deleteAll();
    }

}
