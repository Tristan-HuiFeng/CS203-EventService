package com.eztix.eventservice.unit;

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.model.PurchaseRequest;
import com.eztix.eventservice.model.SalesRound;
import com.eztix.eventservice.model.TicketSalesLimit;
import com.eztix.eventservice.repository.PurchaseRequestRepository;
import com.eztix.eventservice.repository.SalesRoundRepository;
import com.eztix.eventservice.service.EventService;
import com.eztix.eventservice.service.PurchaseRequestService;
import com.eztix.eventservice.service.SalesRoundService;

import org.apache.el.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseRequestServiceTest {

    @Mock
    private PurchaseRequestRepository purchaseRequestRepository;
    @Mock
    private SalesRoundRepository salesRoundRepository;
    @Mock
    private SalesRoundService salesRoundService;
    @InjectMocks
    private PurchaseRequestService testPurchaseRequestService;
    @InjectMocks
    private SalesRoundService testSalesRoundService;
    @Mock
    private static EventService eventService;

    @Test
    void givenIdNotInDB_whenRetrieveByPurchaseRequestId_throwResourceNotFoundException() {

        // given
        given(purchaseRequestRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testPurchaseRequestService.getPurchaseRequestById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("purchase request with id %d does not exist.", 1L);
    }

    @Test
    void givenPurchaseRequestExist_whenRetrieve_thenSuccessful() {

        // given
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setCustomerId("test customer");
        purchaseRequest.setQueueNumber(1L);
        purchaseRequest.setStatus("test status");

        given(purchaseRequestRepository.findById(purchaseRequest.getId()))
                .willReturn(Optional.of(purchaseRequest));

        // when
        PurchaseRequest retrievedPurchaseRequest = testPurchaseRequestService
                .getPurchaseRequestById(purchaseRequest.getId());
        // then
        assertThat(retrievedPurchaseRequest).isEqualTo(purchaseRequest);

    }

    @Test
    void givenNullId_whenUpdate_throwRequestValidationException() {
        // given
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setCustomerId("test customer");
        purchaseRequest.setQueueNumber(1L);
        purchaseRequest.setStatus("test status");

        // when
        // then
        assertThatThrownBy(() -> testPurchaseRequestService.updatePurchaseRequest(purchaseRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("purchase request id cannot be null.");

    }

    @Test
    void givenIdNotInDB_whenUpdate_throwResourceNotFoundException() {
        // given
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setId(1L);
        purchaseRequest.setCustomerId("test customer");
        purchaseRequest.setQueueNumber(1L);
        purchaseRequest.setStatus("test status");

        given(purchaseRequestRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testPurchaseRequestService.updatePurchaseRequest(purchaseRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("purchase request with id %d does not exist.", 1L);

    }

    @Test
    void givenSalesRoundIdNotInDB_whenProcessPR_throwResourceNotFoundException() {
        // given
        given(purchaseRequestRepository.findBySalesRoundId(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testPurchaseRequestService.processPurchaseRequest(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("sales round with id %d does not exist.", 1L);

    }

    @Test
    void givenPurchaseRequestsWithTheGivenSalesRoundIdInDB_whenProcessPR_throwResourceNotFoundException() {
        // given
        SalesRound salesRound = new SalesRound();
        salesRound.setRoundStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setRoundEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setPurchaseStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setPurchaseEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setSalesType("test sales type");

        List<TicketSalesLimit> ticketSalesLimitList = new ArrayList<>();
        ticketSalesLimitList.add(new TicketSalesLimit());
        salesRound.setTicketSalesLimits(ticketSalesLimitList);

        given(eventService.getEventById(1L)).willReturn(new Event());
        testSalesRoundService.addNewSalesRound(1L, salesRound);

        given(salesRoundRepository.findById(salesRound.getId()))
                .willReturn(Optional.of(salesRound));

        given(purchaseRequestRepository.findBySalesRoundId(salesRound.getId())).willReturn(Optional.empty());
        // when
        SalesRound retrievedSalesRound = testSalesRoundService
                .getSalesRoundById(salesRound.getId());

        // then
        assertThat(retrievedSalesRound).isEqualTo(salesRound);
        assertThatThrownBy(() -> testPurchaseRequestService.processPurchaseRequest(salesRound.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("purchase requests with sales round id %d do not exist.", salesRound.getId());

    }

    // issue: line 189: java.lang.IllegalStateException: stream has already been operated upon or closed
    @Test
    void givenSalesRoundAndPurchaseRequestInDB_whenProcessPRWithAlgorithm_thenSuccessful() {
        // given
        SalesRound salesRound = new SalesRound();
        salesRound.setRoundStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setRoundEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setPurchaseStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setPurchaseEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setSalesType("test sales type");
        salesRound.setId(1L);

        List<TicketSalesLimit> ticketSalesLimitList = new ArrayList<>();
        ticketSalesLimitList.add(new TicketSalesLimit());
        salesRound.setTicketSalesLimits(ticketSalesLimitList);

        // given(eventService.getEventById(1L)).willReturn(new Event());

        // given(salesRoundRepository.findById(salesRound.getId()).get())
        // .willReturn(salesRound);

        List<PurchaseRequest> purchaseRequests = createSamplePurchaseRequests(10, salesRound);

        // after pr process 1
        testPurchaseRequestService.algorithm(purchaseRequests.stream());
        long[] queueNumAfterProcess1 = new long[10];
        for (int i = 1; i <= 10; i++) {
            queueNumAfterProcess1[i] = purchaseRequests.get(i).getQueueNumber();
        }

        // after pr process 2
        testPurchaseRequestService.algorithm(purchaseRequests.stream());
        long[] queueNumAfterProcess2 = new long[10];
        for (int i = 1; i <= 10; i++) {
            queueNumAfterProcess2[i] = purchaseRequests.get(i).getQueueNumber();
        }

        // then
        assertNotEquals(queueNumAfterProcess1, queueNumAfterProcess2);

    }

    private List<PurchaseRequest> createSamplePurchaseRequests(int count, SalesRound salesRound) {
        return IntStream.rangeClosed(1, count)
                .mapToObj(i -> {
                    PurchaseRequest purchaseRequest = new PurchaseRequest();
                    purchaseRequest.setId((long) i);
                    purchaseRequest.setSalesRound(salesRound);
                    return purchaseRequest;
                })
                .toList();
    }

}