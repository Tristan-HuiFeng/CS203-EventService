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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
        // when
        // then
        assertThatThrownBy(() -> testPurchaseRequestService.processPurchaseRequests(1L))
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
    
        // when
        SalesRound retrievedSalesRound = testSalesRoundService.getSalesRoundById(salesRound.getId());
    
        // then
        assertThat(retrievedSalesRound).isEqualTo(salesRound);
        assertThatThrownBy(() -> testPurchaseRequestService.processPurchaseRequests(salesRound.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("purchase requests with sales round id %d do not exist.", salesRound.getId()));
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
    
        List<PurchaseRequest> purchaseRequests = createSamplePurchaseRequests(10, salesRound);
    
        // Mock the behavior of the purchaseRequestRepository
        when(purchaseRequestRepository.countBySalesRoundId(1L)).thenReturn(10L);
    
        when(purchaseRequestRepository.findBySalesRoundId(anyLong(), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(purchaseRequests.subList(0, 5), PageRequest.of(0, 5), 10))
                .thenReturn(new PageImpl<>(purchaseRequests.subList(5, 10), PageRequest.of(1, 5), 10));
    
        // Process the PurchaseRequests using the algorithm
        testPurchaseRequestService.processPurchaseRequests(1L);
    
        // Extract queue numbers after processing
        List<Long> queueNumAfterProcess1 = purchaseRequests.stream()
                .map(PurchaseRequest::getQueueNumber)
                .collect(Collectors.toList());
    
        // Process the PurchaseRequests again using the algorithm
        testPurchaseRequestService.processPurchaseRequests(1L);
    
        // Extract queue numbers after processing the second time
        List<Long> queueNumAfterProcess2 = purchaseRequests.stream()
                .map(PurchaseRequest::getQueueNumber)
                .collect(Collectors.toList());
    
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