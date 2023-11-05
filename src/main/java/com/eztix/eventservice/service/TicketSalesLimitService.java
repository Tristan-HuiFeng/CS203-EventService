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
     * 
     * @param ticketSalesLimit
     * @return
     */
    // Add new TicketSalesLimit
    public TicketSalesLimit addNewTicketSalesLimit(TicketSalesLimit ticketSalesLimit) {
        return ticketSalesLimitRepository.save(ticketSalesLimit);
    }

    /**
     * 
     * @param ticketSalesLimit_id
     * @return
     */
    // Get TicketSalesLimit by id
    public TicketSalesLimit getTicketSalesLimitById(Long ticketSalesLimit_id) {

        return ticketSalesLimitRepository.findById(ticketSalesLimit_id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("ticket sales limit with id %d does not exist.", ticketSalesLimit_id))
        );

    }

    /**
     * 
     * @param salesRoundId
     * @return
     */
    public Iterable<TicketSalesLimit> getTicketSalesLimitBySalesRoundId(Long salesRoundId){
        return ticketSalesLimitRepository.findBySalesRoundId(salesRoundId);
    }

    /**
     * 
     * @return
     */
    // Get all TicketSalesLimit
    public Iterable<TicketSalesLimit> getAllTicketSalesLimits() {
        return ticketSalesLimitRepository.findAll();
    }

    /**
     * 
     * @param ticketSalesLimit
     * @return
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
     * 
     */
    // Delete all TicketSalesLimit
    public void deleteAllTicketSalesLimits() {
        ticketSalesLimitRepository.deleteAll();
    }

}
