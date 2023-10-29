package com.eztix.eventservice.service;

import com.eztix.eventservice.dto.PurchaseRequestDTO;
import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.*;
import com.eztix.eventservice.repository.PurchaseRequestRepository;
import com.eztix.eventservice.repository.SalesRoundRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
public class PurchaseRequestService {

    private final PurchaseRequestRepository purchaseRequestRepository;
    private final TicketTypeService ticketTypeService;
    private final TicketSalesLimitService ticketSalesLimitService;
    private final PurchaseRequestItemService purchaseRequestItemService;
    private final SalesRoundRepository salesRoundRepository;

    // Add new PurchaseRequest
    public PurchaseRequest addNewPurchaseRequest(PurchaseRequestDTO purchaseRequestDTO) {

        if (purchaseRequestDTO.getSalesRoundId() == null) {
            throw new RequestValidationException("sales round id cannot be null.");
        }

        if (purchaseRequestDTO.getPurchaseRequestItems().isEmpty()) {
            throw new RequestValidationException("there cannot be 0 item in the purchase request.");
        }

        // Get Sales Round
        SalesRound salesRound = salesRoundRepository.findSalesRoundById(purchaseRequestDTO.getSalesRoundId())
                .orElseThrow();

        OffsetDateTime now = OffsetDateTime.now(ZoneId.of("Asia/Singapore"));

        if (salesRound.getRoundEnd().isAfter(now) || salesRound.getRoundEnd().isBefore(now)) {
            throw new RequestValidationException("request rejected due to sales round not ongoing.");
        }

        // New Purchase Request
        PurchaseRequest newPurchaseRequest =
                PurchaseRequest.builder().status("pending").customerId("Default TODO").salesRound(salesRound).build();

        List<PurchaseRequestItem> newPurchaseRequestItemList = createNewPrItemList(purchaseRequestDTO,
                newPurchaseRequest);
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

        OffsetDateTime now = OffsetDateTime.now(ZoneId.of("Asia/Singapore"));

        if (purchaseRequest.getSalesRound().getRoundEnd().isAfter(now) || purchaseRequest.getSalesRound().getRoundEnd().isBefore(now)) {
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

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void processPurchaseRequests(Long salesRoundId) {
        // Get the total count of items for the given sales round
        long totalItemCount = purchaseRequestRepository.countBySalesRoundId(salesRoundId);


        // Create a list of RNG to be used for randomization
        List<Long> rng = LongStream.range(0L, totalItemCount).boxed().collect(Collectors.toList());
        Collections.shuffle(rng, new SecureRandom());
        Iterator<Long> rngIterator = rng.iterator();

        // Run through all ticket sales limit to figure out current round max for each ticket
        Iterable<TicketSalesLimit> ticketSalesLimitIterable =
                ticketSalesLimitService.getTicketSalesLimitBySalesRoundId(salesRoundId);
        Map<Long, Integer> ticketTypeToVacancyMap = new HashMap<>();

        ticketSalesLimitIterable.forEach(ticketSalesLimit -> {
            ticketTypeToVacancyMap.put(ticketSalesLimit.getTicketType().getId(), ticketSalesLimit.getLimitVacancy());
        });

        // Stream through all the PRs under a sales round to assign queue numbers
        purchaseRequestRepository.findBySalesRoundId(salesRoundId)
                .peek(pr -> pr.setQueueNumber(rngIterator.next())) // Set Queue Number for each
                .sorted((a, b) -> Math.toIntExact(a.getQueueNumber() - b.getQueueNumber()))
                // Now for each of them, modify the quantity approved based on the window limit
                .forEach(pr -> {
                    pr.getPurchaseRequestItems().forEach(prItem -> {
                        TicketType ticketType = prItem.getTicketType();
                        int remaining = ticketTypeToVacancyMap.get(ticketType.getId());
                        int requested = prItem.getQuantityRequested();

                        if (remaining >= requested) {
                            ticketTypeToVacancyMap.put(ticketType.getId(), remaining - requested);
                            PurchaseRequestItem newPrItem = PurchaseRequestItem.builder()
                                    .id(prItem.getId())
                                    .quantityRequested(prItem.getQuantityRequested())
                                    .quantityRequested(prItem.getQuantityRequested())
                                    .ticketType(ticketType)
                                    .purchaseRequest(pr)
                                    .build();
                            purchaseRequestItemService.updatePurchaseRequestItem(newPrItem);
                        } else if (remaining > 0) {
                            ticketTypeToVacancyMap.put(ticketType.getId(), 0);
                            PurchaseRequestItem newPrItem = PurchaseRequestItem.builder()
                                    .id(prItem.getId())
                                    .quantityRequested(prItem.getQuantityRequested())
                                    .quantityRequested(remaining)
                                    .ticketType(ticketType)
                                    .purchaseRequest(pr)
                                    .build();
                            purchaseRequestItemService.updatePurchaseRequestItem(newPrItem);
                        }
                    });
                });

    }

    // Delete all PurchaseRequest
    public void deleteAllPurchaseRequests() {
        purchaseRequestRepository.deleteAll();
    }

    private List<PurchaseRequestItem> createNewPrItemList(PurchaseRequestDTO purchaseRequestDTO,
                                                          PurchaseRequest newPurchaseRequest) {
        AtomicInteger sum = new AtomicInteger();

        List<PurchaseRequestItem> newPurchaseRequestItemList =
                purchaseRequestDTO.getPurchaseRequestItems().stream().map(prItem -> {
                    if (prItem.getTicketTypeId() == null) {
                        throw new RequestValidationException("ticket type id cannot be null.");
                    }
                    TicketType ticketType = ticketTypeService.getTicketTypeById(prItem.getTicketTypeId());
                    sum.addAndGet(prItem.getQuantityRequested());

                    return PurchaseRequestItem.builder()
                            .quantityApproved(0)
                            .quantityRequested(prItem.getQuantityRequested())
                            .ticketType(ticketType)
                            .purchaseRequest(newPurchaseRequest)
                            .build();
                }).toList();

        checkTicketLimit(sum);

        return newPurchaseRequestItemList;
    }

    private List<PurchaseRequestItem> createNewPrItemList(PurchaseRequest purchaseRequest,
                                                          PurchaseRequest currentPurchaseRequest) {
        AtomicInteger sum = new AtomicInteger();
        List<PurchaseRequestItem> newPurchaseRequestItemList =
                purchaseRequest.getPurchaseRequestItems().stream().map(prItem -> {
                    if (prItem.getTicketType() == null) {
                        throw new RequestValidationException("ticket type cannot be null.");
                    }
                    sum.addAndGet(prItem.getQuantityRequested());

                    return PurchaseRequestItem.builder()
                            .quantityApproved(0)
                            .quantityRequested(prItem.getQuantityRequested())
                            .ticketType(prItem.getTicketType())
                            .purchaseRequest(currentPurchaseRequest).build();
                }).toList();

        checkTicketLimit(sum);

        return newPurchaseRequestItemList;
    }

    private void checkTicketLimit(AtomicInteger sum) {
        if (sum.get() > 4) {
            throw new RequestValidationException("purchase request exceed 4 ticket limit.");
        } else if (sum.get() < 0) {
            throw new RequestValidationException("purchase request must have at least 1 ticket.");
        }
    }
}
