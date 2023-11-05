package com.eztix.eventservice.service;

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.TicketSalesLimit;
import com.eztix.eventservice.repository.TicketSalesLimitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketSalesLimitService {

    private final TicketSalesLimitRepository ticketSalesLimitRepository;

    /**
     * Create a ticket sales limit.
     * 
     * @param ticketSalesLimit a TicketSalesLimit object to be created.
     * @return the created TicketSalesLimit object.
     */
    // Add new TicketSalesLimit
    public TicketSalesLimit addNewTicketSalesLimit(TicketSalesLimit ticketSalesLimit) {
        return ticketSalesLimitRepository.save(ticketSalesLimit);
    }

    /**
     * Retrieve a ticket sales limit.
     * If there is no ticket sales limit with given "id", throw a ResourceNotFoundException.
     * 
     * @param ticketSalesLimit_id a long value representing the unique identifier of the TicketSalesLimit to retrieve.
     * @return the retrieved TicketSalesLimit object.
     */
    // Get TicketSalesLimit by id
    public TicketSalesLimit getTicketSalesLimitById(Long ticketSalesLimit_id) {

        return ticketSalesLimitRepository.findById(ticketSalesLimit_id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("ticket sales limit with id %d does not exist.", ticketSalesLimit_id))
        );

    }

    /**
     * Retrieve all ticket sales limits associated with a given sales round.
     * 
     * @param salesRoundId a long value representing the unique identifier of the SalesRound associated with the TicketSalesLimits to be retrieved.
     * @return an iterable of the TicketSalesLimits associated with the sales round.
     */
    public Iterable<TicketSalesLimit> getTicketSalesLimitBySalesRoundId(Long salesRoundId){
        return ticketSalesLimitRepository.findBySalesRoundId(salesRoundId);
    }

    /**
     * Retrieve all ticket sales limits.
     * 
     * @return an iterable of all TicketSalesLimit objects.
     */
    // Get all TicketSalesLimit
    public Iterable<TicketSalesLimit> getAllTicketSalesLimits() {
        return ticketSalesLimitRepository.findAll();
    }

    /**
     * Update a ticket sales limit.
     * If "id" is null, throw a RequestValidationException.
     * If there is no ticket sales limit with given "id", throw a ResourceNotFoundException.
     * 
     * @param ticketSalesLimit a TicketSalesLimit object containing the new TicketSalesLimit info to be updated.
     * @return the updated TicketSalesLimit object.
     */
    // Update TicketSalesLimit
    @Transactional
    public TicketSalesLimit updateTicketSalesLimit(TicketSalesLimit ticketSalesLimit) {
        if (ticketSalesLimit.getId() == null) {
            throw new RequestValidationException("ticket sales limit id cannot be null.");
        }

        ticketSalesLimitRepository.findById(ticketSalesLimit.getId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("ticket sales limit with id %d does not exist.", ticketSalesLimit.getId())));

        return ticketSalesLimitRepository.save(ticketSalesLimit);
    }

    /**
     * Delete all ticket sales limits.
     */
    public void deleteAllTicketSalesLimits() {
        ticketSalesLimitRepository.deleteAll();
    }

}
