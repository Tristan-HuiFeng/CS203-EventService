package com.eztix.eventservice.service;

import com.eztix.eventservice.dto.request.NewSalesRound;
import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.model.SalesRound;
import com.eztix.eventservice.model.TicketSalesLimit;
import com.eztix.eventservice.model.TicketType;
import com.eztix.eventservice.repository.SalesRoundRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SalesRoundService {

    private final SalesRoundRepository salesRoundRepository;
    @Setter
    @Autowired
    private EventService eventService;
    private final PurchaseRequestService purchaseRequestService;
    private final TaskScheduler taskScheduler;
    private final TicketTypeService ticketTypeService;
    private final TicketSalesLimitService ticketSalesLimitService;

    /**
     * 
     * @param eventId
     * @param salesRounds
     * @return
     */
    // Add new SalesRound
    public SalesRound[] addSalesRounds(long eventId, NewSalesRound[] salesRounds) {
        Event event = eventService.getEventById(eventId);

        return Arrays.stream(salesRounds).map((salesRound) -> {
            SalesRound inputSalesRound = SalesRound.builder()
                    .roundStart(salesRound.getRoundStart())
                    .roundEnd(salesRound.getRoundEnd())
                    .purchaseStart(salesRound.getPurchaseStart())
                    .purchaseEnd(salesRound.getPurchaseEnd())
                    .salesType(salesRound.getSalesType())
                    .event(event)
                    .build();
            SalesRound savedSalesRound =  salesRoundRepository.save(inputSalesRound);

            salesRound.getTicketSalesLimitList().forEach(newTicketSalesLimit -> {
                TicketType ticketType = ticketTypeService.getTicketTypeById(newTicketSalesLimit.getTicketTypeId());

                TicketSalesLimit ticketSalesLimit = TicketSalesLimit.builder()
                        .ticketType(ticketType)
                        .salesRound(savedSalesRound)
                        .limitVacancy(newTicketSalesLimit.getLimitVacancy())
                        .occupiedVacancy(0)
                        .build();

                ticketSalesLimitService.addNewTicketSalesLimit(ticketSalesLimit);
            });

            // Schedule a process purchase request when the sales round ends
            taskScheduler.schedule(() -> purchaseRequestService.processPurchaseRequests(savedSalesRound.getId()),
                    savedSalesRound.getRoundEnd().toInstant());

            Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
            for (Thread thread : threadSet) {
                if (thread.getName().startsWith("MyExecutor")) {
                    System.out.println(thread.getName() + " " + thread.getState());
                    for (StackTraceElement s : thread.getStackTrace()) {
                        System.out.println(s);
                    }
                }
            }

            return savedSalesRound;
        }).toArray(SalesRound[]::new);

//        if (salesRound.getId() != null) {
//            throw new RequestValidationException("not allowed to specify id for new sales round");
//        }
//        if (salesRound.getTicketSalesLimits().isEmpty()) {
//            throw new RequestValidationException("sales round must have at least one ticket sales limit");
//        }

    }

    /**
     * 
     * @param id
     * @return
     */
    // Get SalesRound by id
    public SalesRound getSalesRoundById(Long id) {

        return salesRoundRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("sales round with id %d does not exist.", id))
        );

    }

    /**
     * 
     * @param eventId
     * @return
     */
    public Iterable<SalesRound> getSalesRoundByEventId(Long eventId) {

        return salesRoundRepository.findByEventId(eventId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("event with id %d does not have any sales round.", eventId))
        );

    }

    /**
     * 
     * @param salesRound
     * @return
     */
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

    /**
     * 
     */
    // Delete all SalesRound
    public void deleteAllSalesRounds() {
        salesRoundRepository.deleteAll();
    }

}
