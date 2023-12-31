package com.eztix.eventservice.service;

import com.eztix.eventservice.dto.PurchaseRequestCreation;
import com.eztix.eventservice.dto.PurchaseRequestDTO;
import com.eztix.eventservice.dto.confirmation.EventConfirmationDTO;
import com.eztix.eventservice.dto.confirmation.PurchaseRequestConfirmationDTO;
import com.eztix.eventservice.dto.confirmation.PurchaseRequestItemConfirmationDTO;
import com.eztix.eventservice.dto.confirmation.SalesRoundConfirmationDTO;
import com.eztix.eventservice.dto.prretrieval.PurchaseRequestItemRetrivalDTO;
import com.eztix.eventservice.dto.prretrieval.PurchaseRequestRetrievalDTO;
import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.*;
import com.eztix.eventservice.repository.EventRepository;
import com.eztix.eventservice.repository.PurchaseRequestRepository;
import com.eztix.eventservice.repository.SalesRoundRepository;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.Sort;
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

    /**
     * Get the status of a purchase request.
     * 
     * @param pr a PurchaseRequest object that we want to obtain the status of.
     * @return a String that indicates the status of the PurchaseRequest.
     */
    public String getStatus(PurchaseRequest pr) {
        OffsetDateTime now = OffsetDateTime.now(ZoneId.of("Singapore"));

        if(now.isAfter(pr.getSalesRound().getRoundEnd()) && now.isBefore(pr.getSalesRound().getPurchaseEnd())) {
            return "processing";
        }

        return "submitted";

    }

    /**
     * Create a purchase request.
     * If "event id" is null, throw a RequestValidationException.
     * If "user id" is null, throw a RequestValidationException.
     * If 0 purchase request items, throw a RequestValidationException.
     * If current datetime is after end of sales round, throw a RequestValidationException.
     * If current datetime is before start of sales round, throw a RequestValidationException.
     * 
     * @param purchaseRequestDTO a PurchaseRequestDTO object containing the info of the purchase request to be created.
     * @param userId a String representing the user id.
     * @return a PurchaseRequestCreation object containing the details of the new purchase request.
     */
    public PurchaseRequestCreation addNewPurchaseRequest(PurchaseRequestDTO purchaseRequestDTO, String userId) {

        if (purchaseRequestDTO.getEventId() == null) {
            throw new RequestValidationException("event id cannot be null.");
        }

        if (userId == null) {
            throw new RequestValidationException("must contain valid user id");
        }

        if (purchaseRequestDTO.getPurchaseRequestItems().isEmpty()) {
            throw new RequestValidationException("there cannot be 0 item in the purchase request.");
        }

        // Get Sales Round
        OffsetDateTime now = OffsetDateTime.now(ZoneId.of("Asia/Singapore"));

        SalesRound salesRound = salesRoundRepository.findTop1ByEventIdAndRoundStartLessThanAndRoundEndGreaterThan(purchaseRequestDTO.getEventId(), now, now)
                .orElseThrow();

        if (now.isAfter(salesRound.getRoundEnd()) || now.isBefore(salesRound.getRoundStart())) {
            throw new RequestValidationException("request rejected due to sales round not ongoing.");
        }

        // New Purchase Request
        PurchaseRequest newPurchaseRequest =
                PurchaseRequest.builder().customerId(userId).isPaid(false).salesRound(salesRound).submitDateTime(now).build();

        List<PurchaseRequestItem> newPurchaseRequestItemList = createNewPrItemList(purchaseRequestDTO,
                newPurchaseRequest);
        newPurchaseRequest.setPurchaseRequestItems(newPurchaseRequestItemList);

        newPurchaseRequest = purchaseRequestRepository.save(newPurchaseRequest);

        return PurchaseRequestCreation.builder().purchaseRequestId(newPurchaseRequest.getId()).build();
    }

    /**
     * Retrieve an event confirmation dto.
     * 
     * @param purchaseRequestId a long value representing the unique identifier of the purchase request related to the event confirmation.
     * @return the EventConfirmationDTO containing details about the purchase request.
     */
    public EventConfirmationDTO getPurchaseRequestConfirmation(Long purchaseRequestId) {

        PurchaseRequest purchaseRequest = purchaseRequestRepository.findById(purchaseRequestId)
                .orElseThrow();

        SalesRound salesRound = purchaseRequest.getSalesRound();

        Event event = purchaseRequest.getSalesRound().getEvent();

        return EventConfirmationDTO.builder()
                .id(event.getId())
                .endDateTime(event.getEnd_datetime())
                .startDateTime(event.getStart_datetime())
                .name(event.getName())
                .location(event.getLocation())
                .description(event.getDescription())
                .bannerURL(event.getBannerURL())
                .salesRound(SalesRoundConfirmationDTO.builder()
                        .roundStart(salesRound.getRoundStart())
                        .roundEnd(salesRound.getRoundEnd())
                        .salesType(salesRound.getSalesType())
                        .build())
                .purchaseRequest(PurchaseRequestConfirmationDTO.builder()
                        .id(purchaseRequestId)
                        .status(getStatus(purchaseRequest))
                        .purchaseRequestItems(purchaseRequest.getPurchaseRequestItems().stream().map(prItem ->
                                PurchaseRequestItemConfirmationDTO.builder()
                                        .id(prItem.getId())
                                        .price(prItem.getTicketType().getPrice() * prItem.getQuantityRequested())
                                        .ticketType(prItem.getTicketType().getType())
                                        .quantityRequested(prItem.getQuantityRequested())
                                        .eventStartDateTime(prItem.getTicketType().getActivity().getStartDateTime())
                                        .eventEndDateTime(prItem.getTicketType().getActivity().getEndDateTime())
                                        .build())
                                .toList())
                        .build())
                .build();

    }

    /**
     * Retrieve a list of purchase requests filtered by userId.
     * If "customer id" is null, throw a RequestValidationException.
     * 
     * @param customerId a String representing the customer id.
     * @return a list of PurchaseRequestRetrievalDTO related to the customer id.
     */
    @Transactional
    public List<PurchaseRequestRetrievalDTO> getPurchaseRequestByUserId(String customerId){
        if (customerId == null) {
            throw new RequestValidationException("must contain valid customer id");
        }

        return purchaseRequestRepository.findByCustomerIdAndIsPaidFalseOrderBySubmitDateTimeDesc(customerId)
                .filter(pr -> pr.getSalesRound().getPurchaseEnd().isAfter(OffsetDateTime.now(ZoneId.of("Singapore"))))
                .map(pr -> PurchaseRequestRetrievalDTO.builder()
                        .id(pr.getId())
                        .eventName(pr.getSalesRound().getEvent().getName())
                        .status(getStatus(pr))
                        .bannerURL(pr.getSalesRound().getEvent().getBannerURL())
                        .queueNumber(pr.getQueueNumber())
                        .build())
                .toList();
    }

    /**
     * Retrieve a purchase request.
     * 
     * @param id a long value representing the unique identifier of the purchase request to be retrieved.
     * @return the retrieved PurchaseRequest object.
     */
    public PurchaseRequest getPurchaseRequestById(Long id) {

        return purchaseRequestRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("purchase request with id %d does not exist.", id))
        );

    }

    /**
     * Update a purchase request.
     * 
     * @param purchaseRequest a PurchaseRequest object containing the new PurchaseRequest info to be updated.
     * @return the updated PurchaseRequest object.
     */
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

    /**
     * Process purchase requests made within the sales round.
     * 
     * @param salesRoundId a long value representing the unique identifier of the SalesRound to process.
     */
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

    /**
     * Delete all purchase requests.
     */
    public void deleteAllPurchaseRequests() {
        purchaseRequestRepository.deleteAll();
    }

    /**
     * Delete a purchase request.
     * 
     * @param id a long value representing the unique identifier of the purchase request to be deleted.
     */
    public void deletePurchaseRequest(long id) {

        purchaseRequestRepository.deleteById(id);
    }

    /**
     * Retrieve a purchase request item.
     * 
     * @param id a long value representing the unique identifier of the purchase request.
     * @return a PurchaseRequestItemRetrivalDTO containing info of the retrieved purchase request item.
     */
    public PurchaseRequestItemRetrivalDTO getPurchaseRequestItemById(Long id) {

        PurchaseRequestItem prItem = purchaseRequestItemService.getPurchaseRequestItemById(id);


        return PurchaseRequestItemRetrivalDTO.builder()
                .id(prItem.getId())
                .ticketType(prItem.getTicketType().getType())
                .price(prItem.getTicketType().getPrice())
                .quantityRequested(prItem.getQuantityRequested())
                .eventStartDateTime(prItem.getTicketType().getActivity().getStartDateTime())
                .eventEndDateTime(prItem.getTicketType().getActivity().getEndDateTime())
                .build();

    }

    /**
     * Create a list of purchase request items based on the PurchaseRequestDTO object and the new PurchaseRequest object.
     * If "ticket type id" of a purchase request item is null, throw a RequestValidationException.
     * 
     * @param purchaseRequestDTO a PurchaseRequestDTO object containing PurchaseRequestItems.
     * @param newPurchaseRequest a new PurchaseRequest object to which the PurchaseRequestItems belong.
     * @return the created list of PurchaseRequestItems.
     */
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

    /**
     * Create a list of purchase request items based on the PurchaseRequestDTO object and the new PurchaseRequest object.
     * If "ticket type id" of a purchase request item is null, throw a RequestValidationException.
     * 
     * @param purchaseRequestDTO a PurchaseRequestDTO object containing PurchaseRequestItems.
     * @param newPurchaseRequest a new PurchaseRequest object to which the PurchaseRequestItems belong.
     * @return the created list of PurchaseRequestItems.
     */
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

    /**
     * Check if number of tickets is not larger than 4, not less than 0.
     * If number of tickets is larger than 4, throw a RequestValidationException.
     * If number of tickets is smaller than 0, throw a RequestValidationException.
     * 
     * @param sum an AtomicInteger representing the current number of tickets in a purchase request.
     */
    private void checkTicketLimit(AtomicInteger sum) {
        if (sum.get() > 4) {
            throw new RequestValidationException("purchase request exceed 4 ticket limit.");
        } else if (sum.get() < 0) {
            throw new RequestValidationException("purchase request must have at least 1 ticket.");
        }
    }

    public void setPurchaseRequestIsPaid(Long id) {
        PurchaseRequest pr = this.getPurchaseRequestById(id);
        pr.setIsPaid(true);
        purchaseRequestRepository.save(pr);
    }
}
