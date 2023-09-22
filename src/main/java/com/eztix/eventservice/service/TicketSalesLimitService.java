package com.eztix.eventservice.service;

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.TicketSalesLimit;
import com.eztix.eventservice.repository.TicketSalesLimitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketSalesLimitService {

    private final TicketSalesLimitRepository ticketSalesLimitRepository;

    public TicketSalesLimitService(TicketSalesLimitRepository ticketSalesLimitRepository) {
        this.ticketSalesLimitRepository = ticketSalesLimitRepository;
    }

    // Add new TicketSalesLimit
    public TicketSalesLimit addNewTicketSalesLimit(TicketSalesLimit ticketSalesLimit) {
        return ticketSalesLimitRepository.save(ticketSalesLimit);
    }

    // Get TicketSalesLimit by id
    public TicketSalesLimit getTicketSalesLimitById(Long ticketSalesLimit_id) {

        return ticketSalesLimitRepository.findById(ticketSalesLimit_id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("ticket sales limit with id %d does not exist.", ticketSalesLimit_id))
        );

    }

    // Get all TicketSalesLimit
    public Iterable<TicketSalesLimit> getAllTicketSalesLimits() {
        return ticketSalesLimitRepository.findAll();
    }

    // Update TicketSalesLimit
    @Transactional
    public TicketSalesLimit updateTicketSalesLimit(TicketSalesLimit ticketSalesLimit) {
        if (ticketSalesLimit.getId().getId() == null) {
            throw new RequestValidationException("ticket sales limit id cannot be null.");
        }

        ticketSalesLimitRepository.findById(ticketSalesLimit.getId().getId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("ticket sales limit with id %d does not exist.", ticketSalesLimit.getId().getId())));

        return ticketSalesLimitRepository.save(ticketSalesLimit);
    }

    // Delete all TicketSalesLimit
    public void deleteAllTicketSalesLimits() {
        ticketSalesLimitRepository.deleteAll();
    }

}
