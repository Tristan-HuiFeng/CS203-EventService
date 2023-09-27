package com.eztix.eventservice.service;

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.model.SalesRound;
import com.eztix.eventservice.repository.SalesRoundRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SalesRoundService {

    private final SalesRoundRepository salesRoundRepository;
    private final EventService eventService;

    public SalesRoundService(SalesRoundRepository salesRoundRepository, EventService eventService) {
        this.salesRoundRepository = salesRoundRepository;
        this.eventService = eventService;
    }

    // Add new SalesRound
    public SalesRound addNewSalesRound(Long eventId, SalesRound salesRound) {

        Event event = eventService.getEventById(eventId);

        if (salesRound.getId() != null) {
            throw new RequestValidationException("not allowed to specify id for new sales round");
        }
        if (salesRound.getTicketSalesLimits().isEmpty()) {
            throw new RequestValidationException("sales round must have at least one ticket sales limit");
        }

        // TODO Validation

        return salesRoundRepository.save(salesRound);
    }

    // Get SalesRound by id
    public SalesRound getSalesRoundById(Long id) {

        return salesRoundRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("sales round with id %d does not exist.", id))
        );

    }

    public Iterable<SalesRound> getSalesRoundByEventId(Long eventId) {

        return salesRoundRepository.findByEventId(eventId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("event with id %d does not have any sales round.", eventId))
        );

    }

    // Update SalesRound
    @Transactional
    public SalesRound updateSalesRound(SalesRound salesRound) {
        if (salesRound.getId() == null) {
            throw new RequestValidationException("sales round id cannot be null");
        }

        // TODO Validation

        salesRoundRepository.findById(salesRound.getId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("sales round with id %d not found", salesRound.getId())));

        return salesRoundRepository.save(salesRound);
    }

    // Delete all SalesRound
    public void deleteAllSalesRounds() {
        salesRoundRepository.deleteAll();
    }

}
