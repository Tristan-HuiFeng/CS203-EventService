package com.eztix.eventservice.service;

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.Activity;
import com.eztix.eventservice.model.SalesRound;
import com.eztix.eventservice.model.TicketType;
import com.eztix.eventservice.repository.ActivityRepository;
import com.eztix.eventservice.repository.TicketTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@RequiredArgsConstructor
public class TicketTypeService {

    private final TicketTypeRepository ticketTypeRepository;
    private final ActivityService activityService;

    /**
     * Create a ticket type associated with a given activity.
     * 
     * @param activity an Activity object to associate with the ticket type to be created.
     * @param ticketType a TicketType object to be associated with an activity.
     * @return the created TicketType object.
     */
    public TicketType addNewTicketType(Activity activity, TicketType ticketType) {
        ticketType.setActivity(activity);
        return ticketTypeRepository.save(ticketType);

    }

    /**
     * Retrieve a ticket type.
     * If there is no ticket type with given "id", throw a ResourceNotFoundException.
     * 
     * @param id a long value representing the unique identifier of the TicketType to retrieve.
     * @return the retrieved TicketType object.
     */
    public TicketType getTicketTypeById(Long id) {

        return ticketTypeRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("ticket type with id %d does not exist.", id))
        );

    }

    /**
     * Retrieve all ticket types associated with an activity.
     * If there is no ticket sales limit associated with given activity, throw a ResourceNotFoundException.
     * 
     * @param activityId a long value representing the unique identifier of the Activity associated with the TicketTypes to be retrieved.
     * @return an iterable of all TicketType objects associated with an Activity.
     */
    public Iterable<TicketType> getTicketTypeByActivityId(Long activityId) {

        return ticketTypeRepository.findByActivityId(activityId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("activity with id %d does not have any ticket type.", activityId))
        );

    }

    /**
     * Update a ticket type.
     * If "id" is null, throw a RequestValidationException.
     * If there is no ticket type with given "id", throw a ResourceNotFoundException.
     * 
     * @param ticketType a TicketType object containing the new TicketType info to be updated.
     * @return the updated TicketType objects.
     */
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

    /**
     * Delete all ticket types.
     */
    public void deleteAllTicketTypes() {
        ticketTypeRepository.deleteAll();
    }

}
