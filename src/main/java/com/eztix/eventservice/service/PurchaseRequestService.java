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

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
                        String.format("sales round with id %d not found.", purchaseRequestDTO.getSalesRoundId())));

        OffsetDateTime now = OffsetDateTime.now(ZoneId.of("Asia/Singapore"));

        if (salesRound.getRoundEnd().isAfter(now) || salesRound.getRoundEnd().isBefore(now)) {
            throw new RequestValidationException("request rejected due to sales round not ongoing.");
        }

        // New Purchase Request
        PurchaseRequest newPurchaseRequest = new PurchaseRequest();
        newPurchaseRequest.setStatus("pending");
        newPurchaseRequest.setCustomerId("Default TODO");

        newPurchaseRequest.setSalesRound(salesRound);

        // Create a List of Purchase Request Item
        List<PurchaseRequestItem> newPurchaseRequestItemList = new ArrayList<>();
        int sum = 0;

        for (PurchaseRequestItemDTO temp : purchaseRequestDTO.getPurchaseRequestItems()) {

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

        return purchaseRequestRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("purchase request with id %d does not exist.", id)));

    }

    // Update PurchaseRequest
    @Transactional
    public PurchaseRequest updatePurchaseRequest(PurchaseRequest purchaseRequest) {
        if (purchaseRequest.getId() == null) {
            throw new RequestValidationException("purchase request id cannot be null.");
        }

        PurchaseRequest currentPurchaseRequest = this.getPurchaseRequestById(purchaseRequest.getId());

        OffsetDateTime now = OffsetDateTime.now(ZoneId.of("Asia/Singapore"));

        if (purchaseRequest.getSalesRound().getRoundEnd().isAfter(now)
                || purchaseRequest.getSalesRound().getRoundEnd().isBefore(now)) {
            throw new RequestValidationException("request rejected due to sales round not ongoing.");
        }

        int sum = 0;
        List<PurchaseRequestItem> newPurchaseRequestItemList = new ArrayList<>();

        for (PurchaseRequestItem temp : purchaseRequest.getPurchaseRequestItems()) {

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

    // PR algorithm to process purchase requests by assign queue number
    @Transactional(rollbackFor = Exception.class)
    public void processPurchaseRequest(Long salesRoundId) throws ResourceNotFoundException{

        // Retrieve purchase requests
        Optional<java.util.stream.Stream<PurchaseRequest>> allPurchaseRequests = purchaseRequestRepository.findBySalesRoundId(salesRoundId);

        // Check if any purchase requests are retrieved
        if (!allPurchaseRequests.isPresent()) {

            // If no purchase requests retrieved, check if sales round exists, throw relevant exception
            if (!salesRoundRepository.findById(salesRoundId).isPresent()) {
                throw new ResourceNotFoundException(String.format("sales round with id %d does not exist.", salesRoundId));
            }
            throw new ResourceNotFoundException(String.format("purchase requests with sales round id %d do not exist.", salesRoundId));
        }

        // Algorithm
        Stream<PurchaseRequest> streamOfPR = allPurchaseRequests.get();
        long size = streamOfPR.count();
        List<Long> rng = new ArrayList<>();
        long i = 1;
        while (i <= size) {
            rng.add(i++);
        }
        Collections.shuffle(rng, new SecureRandom()));
        Iterator<Long> rngIterator = rng.iterator();
        List<PurchaseRequest> prToSave = new ArrayList<>();
        streamOfPR.forEach(pr -> {
            pr.setQueueNumber(rngIterator.next());
            prToSave.add(pr);

        // included a try catch block in case any logic in error handling is missed out
            // try {
        // save purchase requests in batches of 50 to reduce database operations
            if (prToSave.size() % 50 == 0) {
                purchaseRequestRepository.saveAll(prToSave);
            }
            // } catch (Exception e) {
            //     throw e;
            // }
        });

        if (!prToSave.isEmpty()) {
            purchaseRequestRepository.saveAll(prToSave);
        }

        return;
    }

    // Delete all PurchaseRequest
    public void deleteAllPurchaseRequests() {
        purchaseRequestRepository.deleteAll();
    }

}
