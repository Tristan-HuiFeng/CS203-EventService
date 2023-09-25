package com.eztix.eventservice.service;

import com.eztix.eventservice.dto.PurchaseRequestDTO;
import com.eztix.eventservice.dto.PurchaseRequestItemDTO;
import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.PurchaseRequest;
import com.eztix.eventservice.model.PurchaseRequestItem;
import com.eztix.eventservice.model.SalesRound;
import com.eztix.eventservice.model.TicketType;
import com.eztix.eventservice.repository.PurchaseRequestItemRepository;
import com.eztix.eventservice.repository.PurchaseRequestRepository;
import com.eztix.eventservice.repository.SalesRoundRepository;
import com.eztix.eventservice.repository.TicketTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseRequestService {

    private final PurchaseRequestRepository purchaseRequestRepository;
    private final SalesRoundRepository salesRoundRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final PurchaseRequestItemRepository purchaseRequestItemRepository;


    public PurchaseRequestService(PurchaseRequestRepository purchaseRequestRepository,
                                  SalesRoundRepository salesRoundRepository,
                                  TicketTypeRepository ticketTypeRepository,
                                  PurchaseRequestItemRepository purchaseRequestItemRepository) {
        this.purchaseRequestRepository = purchaseRequestRepository;
        this.salesRoundRepository = salesRoundRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.purchaseRequestItemRepository = purchaseRequestItemRepository;
    }

    // Add new PurchaseRequest
    public PurchaseRequest addNewPurchaseRequest(PurchaseRequestDTO purchaseRequestDTO) {

        if (purchaseRequestDTO.getSalesRoundId() == null) {
            throw new RequestValidationException("sales round id cannot be null.");
        }
        // Get Sales Round
        SalesRound salesRound = salesRoundRepository.findById(purchaseRequestDTO.getSalesRoundId())
                .orElseThrow(() -> new ResourceNotFoundException("sales round id not found."));

        // New Purchase Request
        PurchaseRequest newPurchaseRequest = new PurchaseRequest();
        newPurchaseRequest.setStatus("pending");
        newPurchaseRequest.setCustomerId("Default TODO");


        newPurchaseRequest.setSalesRound(salesRound);

        // Create a List of Purchase Request Item
       List<PurchaseRequestItem> newPurchaseRequestItemList = new ArrayList<>();

        for (PurchaseRequestItemDTO temp: purchaseRequestDTO.getPurchaseRequestItems()) {

            if (temp.getTicketTypeId() == null) {
                throw new RequestValidationException("ticket type id cannot be null.");
            }
            TicketType ticketType = ticketTypeRepository.findById(temp.getTicketTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("ticket type id not found."));

            PurchaseRequestItem purchaseRequestItem = new PurchaseRequestItem();
            purchaseRequestItem.setQuantityApproved(0);
            purchaseRequestItem.setQuantityRequested(temp.getQuantityRequested());
            purchaseRequestItem.setTicketType(ticketType);
            purchaseRequestItem.setPurchaseRequest(newPurchaseRequest);

            newPurchaseRequestItemList.add(purchaseRequestItem);

        }

        // Add to New Purchase Request
        newPurchaseRequest.setPurchaseRequestItems(newPurchaseRequestItemList);

        return purchaseRequestRepository.save(newPurchaseRequest);
    }

    // Get PurchaseRequest by id
    public PurchaseRequest getPurchaseRequestById(Long id) {

        return purchaseRequestRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("purchase request with id %d does not exist.", id))
        );

    }

    // Get all PurchaseRequest
    public Iterable<PurchaseRequest> getAllPurchaseRequests() {
        return purchaseRequestRepository.findAll();
    }

    // Update PurchaseRequest
    @Transactional
    public PurchaseRequest updatePurchaseRequest(PurchaseRequest purchaseRequest) {
        if (purchaseRequest.getId() == null) {
            throw new RequestValidationException("purchase request id cannot be null.");
        }

        purchaseRequestRepository.findById(purchaseRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("purchase request with id %d does not exist.", purchaseRequest.getId())));

        return purchaseRequestRepository.save(purchaseRequest);
    }

    // Delete all PurchaseRequest
    public void deleteAllPurchaseRequests() {
        purchaseRequestRepository.deleteAll();
    }

}
