package com.eztix.eventservice.service;

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.SalesRound;
import com.eztix.eventservice.repository.SalesRoundRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SalesRoundService {

    private final SalesRoundRepository salesRoundRepository;

    public SalesRoundService(SalesRoundRepository salesRoundRepository) {
        this.salesRoundRepository = salesRoundRepository;
    }

    // Add new SalesRound
    public SalesRound addNewSalesRound(SalesRound salesRound) {
        return salesRoundRepository.save(salesRound);
    }

    // Get SalesRound by id
    public SalesRound getSalesRoundById(Long id) {

        return salesRoundRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("sales round with id %d does not exist.", id))
        );

    }

    public Iterable<SalesRound> getSalesRoundByEventId(Long activityId) {

        return salesRoundRepository.findByActivityId(activityId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("activity with id %d does not have any sales round.", activityId))
        );

    }

    // Get all SalesRound
    public Iterable<SalesRound> getAllSalesRounds() {
        return salesRoundRepository.findAll();
    }

    // Update SalesRound
    @Transactional
    public SalesRound updateSalesRound(SalesRound salesRound) {
        if (salesRound.getId() == null) {
            throw new RequestValidationException("sales round id cannot be null");
        }

        salesRoundRepository.findById(salesRound.getId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("sales round with id %d not found", salesRound.getId())));

        return salesRoundRepository.save(salesRound);
    }

    // Delete all SalesRound
    public void deleteAllSalesRounds() {
        salesRoundRepository.deleteAll();
    }

}
