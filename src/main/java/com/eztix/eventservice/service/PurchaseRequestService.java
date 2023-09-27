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

        if (purchaseRequestDTO.getPurchaseRequestItems().isEmpty()) {
            throw new RequestValidationException("there cannot be 0 item in the purchase request.");
        }

        // Get Sales Round
        SalesRound salesRound = salesRoundRepository.findById(purchaseRequestDTO.getSalesRoundId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("sales round with id %d not found.", purchaseRequestDTO.getSalesRoundId())
                ));

        // New Purchase Request
        PurchaseRequest newPurchaseRequest = new PurchaseRequest();
        newPurchaseRequest.setStatus("pending");
        newPurchaseRequest.setCustomerId("Default TODO");


        newPurchaseRequest.setSalesRound(salesRound);

        // Create a List of Purchase Request Item
        List<PurchaseRequestItem> newPurchaseRequestItemList = new ArrayList<>();
        int sum = 0;

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
            sum += temp.getQuantityRequested();

            newPurchaseRequestItemList.add(purchaseRequestItem);

        }
        if (sum > 4) {
            throw new RequestValidationException("purchase request exceed 4 ticket limit.");
        } else if (sum < 0) {
            throw new RequestValidationException("purchase request must have at least 1 ticket.");
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

    // Update PurchaseRequest
    @Transactional
    public PurchaseRequest updatePurchaseRequest(PurchaseRequest purchaseRequest) {
        if (purchaseRequest.getId() == null) {
            throw new RequestValidationException("purchase request id cannot be null.");
        }

        PurchaseRequest currentPurchaseRequest = this.getPurchaseRequestById(purchaseRequest.getId());

        int sum = 0;
        List<PurchaseRequestItem> newPurchaseRequestItemList = new ArrayList<>();

        for (PurchaseRequestItem temp: purchaseRequest.getPurchaseRequestItems()) {

            if (temp.getTicketType() == null) {
                throw new RequestValidationException("ticket type cannot be null.");
            }

            PurchaseRequestItem purchaseRequestItem = new PurchaseRequestItem();
            purchaseRequestItem.setQuantityApproved(0);
            purchaseRequestItem.setQuantityRequested(temp.getQuantityRequested());
            purchaseRequestItem.setTicketType(temp.getTicketType());
            purchaseRequestItem.setPurchaseRequest(currentPurchaseRequest);
            sum += temp.getQuantityRequested();

            newPurchaseRequestItemList.add(purchaseRequestItem);

        }

        if (sum > 4) {
            throw new RequestValidationException("purchase request exceed 4 ticket limit.");
        } else if (sum < 0) {
            throw new RequestValidationException("purchase request must have at least 1 ticket.");
        }

        currentPurchaseRequest.setPurchaseRequestItems(newPurchaseRequestItemList);

        return purchaseRequestRepository.save(currentPurchaseRequest);
    }

    // Delete all PurchaseRequest
    public void deleteAllPurchaseRequests() {
        purchaseRequestRepository.deleteAll();
    }

}
