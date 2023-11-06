package com.eztix.eventservice.unit;

import com.eztix.eventservice.dto.PurchaseRequestCreation;
import com.eztix.eventservice.dto.PurchaseRequestDTO;
import com.eztix.eventservice.dto.PurchaseRequestItemDTO;
import com.eztix.eventservice.dto.confirmation.EventConfirmationDTO;
import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.Activity;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.model.PurchaseRequest;
import com.eztix.eventservice.model.PurchaseRequestItem;
import com.eztix.eventservice.model.SalesRound;
import com.eztix.eventservice.model.TicketType;
import com.eztix.eventservice.repository.PurchaseRequestRepository;
import com.eztix.eventservice.repository.SalesRoundRepository;
import com.eztix.eventservice.service.PurchaseRequestItemService;
import com.eztix.eventservice.service.PurchaseRequestService;
import com.eztix.eventservice.service.TicketSalesLimitService;
import com.eztix.eventservice.service.TicketTypeService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseRequestServiceTest {

    @Mock
    private SalesRoundRepository salesRoundRepository;
    @Mock
    private TicketTypeService ticketTypeService;
    @Mock
    private TicketSalesLimitService ticketSalesLimitService;
    @Mock
    private PurchaseRequestItemService purchaseRequestItemService;
    @Mock
    private PurchaseRequestRepository purchaseRequestRepository;
    @InjectMocks
    private PurchaseRequestService testPurchaseRequestService;
    static private Event event;
    static private Activity activity;
    static private SalesRound salesRound;
    static private PurchaseRequest purchaseRequest;

    @BeforeAll
    static void setup() {
        event = new Event();
        event.setId(1L);
        event.setName("Test Event");
        event.setCategory("concert");
        event.setArtist("artist1");
        event.setDescription("This is a test event");
        event.setBannerURL("urk1");
        event.setSeatMapURL("url2");
        event.setLocation("location");
        event.setIsFeatured(false);

        activity = new Activity();
        activity.setEvent(event);
        activity.setStartDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        activity.setEndDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));

        salesRound = new SalesRound();
        salesRound.setEvent(event);
        salesRound.setRoundStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setRoundEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setPurchaseStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setPurchaseEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setSalesType("test sales type");

        purchaseRequest = new PurchaseRequest();
        purchaseRequest.setCustomerId("test customer");
        purchaseRequest.setQueueNumber(1L);
    }

    @Test
    void givenValidRequest_whenAddNewPurchaseRequest_thenPurchaseRequestCreated() {
        // given
        PurchaseRequestDTO purchaseRequestDTO = new PurchaseRequestDTO();
        purchaseRequestDTO.setEventId(1L);
        purchaseRequestDTO.setPurchaseRequestItems(createPurchaseRequestItemDTOs());

        OffsetDateTime now = OffsetDateTime.now();
        salesRound.setRoundStart(now.minusDays(1));
        salesRound.setRoundEnd(now.plusDays(1));
        salesRound.setPurchaseStart(now.minusDays(1));
        salesRound.setPurchaseEnd(now.plusDays(1));

        when(salesRoundRepository.findTop1ByEventIdAndRoundStartLessThanAndRoundEndGreaterThan(
                anyLong(), any(OffsetDateTime.class), any(OffsetDateTime.class)))
                .thenReturn(Optional.of(salesRound));

        PurchaseRequest savedPurchaseRequest = new PurchaseRequest();
        when(purchaseRequestRepository.save(any(PurchaseRequest.class))).thenReturn(savedPurchaseRequest);

        // when
        PurchaseRequestCreation result = testPurchaseRequestService.addNewPurchaseRequest(purchaseRequestDTO, "userId");

        // then
        assertThat(result).isNotNull();
    }

    @Test
    void givenNullEventId_whenAddNewPurchaseRequest_thenThrowsRequestValidationException() {
        PurchaseRequestDTO purchaseRequestDTO = new PurchaseRequestDTO();
        purchaseRequestDTO.setEventId(null); // Null eventId

        assertThrows(RequestValidationException.class,
                () -> testPurchaseRequestService.addNewPurchaseRequest(purchaseRequestDTO, "userId"));
    }

    @Test
    void givenNullUserId_whenAddNewPurchaseRequest_thenThrowsRequestValidationException() {
        PurchaseRequestDTO purchaseRequestDTO = new PurchaseRequestDTO();
        purchaseRequestDTO.setEventId(1L); // Set eventId

        assertThrows(RequestValidationException.class,
                () -> testPurchaseRequestService.addNewPurchaseRequest(purchaseRequestDTO, null)); // Null userId
    }

    @Test
    void givenEmptyItems_whenAddNewPurchaseRequest_thenThrowsRequestValidationException() {
        PurchaseRequestDTO purchaseRequestDTO = new PurchaseRequestDTO();
        purchaseRequestDTO.setEventId(1L); // Set eventId
        purchaseRequestDTO.setPurchaseRequestItems(new ArrayList<>());

        assertThrows(RequestValidationException.class,
                () -> testPurchaseRequestService.addNewPurchaseRequest(purchaseRequestDTO, "userId"));
    }

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

        // when
        // then
        assertThatThrownBy(() -> testPurchaseRequestService.updatePurchaseRequest(purchaseRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("purchase request id cannot be null.");

    }

    @Test
    void givenIdNotInDB_whenUpdate_throwResourceNotFoundException() {

        purchaseRequest.setId(1L);
        purchaseRequest.setSalesRound(salesRound);

        given(purchaseRequestRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testPurchaseRequestService.updatePurchaseRequest(purchaseRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("purchase request with id %d does not exist.", 1L);

    }

    @Test
    void givenPurchaseRequestId_whenGetPurchaseRequestConfirmation_thenReturnEventConfirmationDTO() {
        // given
        Long purchaseRequestId = 1L;

        // Event event = new Event();
        // event.setId(1L);

        // SalesRound salesRound = new SalesRound();
        // salesRound.setEvent(event);

        // Activity activity = new Activity();
        // activity.setEvent(event);

        TicketType ticketType = new TicketType();
        ticketType.setActivity(activity);

        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setId(1L);
        purchaseRequest.setSalesRound(salesRound);

        PurchaseRequestItem purchaseRequestItem = new PurchaseRequestItem();
        purchaseRequestItem.setTicketType(ticketType);

        purchaseRequest.setPurchaseRequestItems(Collections.singletonList(purchaseRequestItem));
        when(purchaseRequestRepository.findById(purchaseRequestId)).thenReturn(Optional.of(purchaseRequest));

        // then
        EventConfirmationDTO expectedEventConfirmationDTO = testPurchaseRequestService
                .getPurchaseRequestConfirmation(purchaseRequestId);

        // then
        assertEquals(event.getId(), expectedEventConfirmationDTO.getId());
    }

    @Test
    void givenSalesRoundId_whenProcessPurchaseRequests_thenQueueNumbersAssigned() {
        // given
        Long salesRoundId = 1L;
        int totalItemCount = 10;

        when(purchaseRequestRepository.countBySalesRoundId(salesRoundId)).thenReturn((long) totalItemCount);

        // list of PurchaseRequest
        List<PurchaseRequest> purchaseRequests = new ArrayList<>();
        for (int i = 0; i < totalItemCount; i++) {
            PurchaseRequest pr = new PurchaseRequest();
            List<PurchaseRequestItem> prItems = new ArrayList<>();
            pr.setPurchaseRequestItems(prItems);
            purchaseRequests.add(pr);
        }

        // list to a Stream
        Stream<PurchaseRequest> purchaseRequestStream = purchaseRequests.stream();

        // repository to return a Stream
        when(purchaseRequestRepository.findBySalesRoundId(salesRoundId)).thenReturn(purchaseRequestStream);

        List<Long> rng = LongStream.range(0L, totalItemCount).boxed().collect(Collectors.toList());
        Collections.shuffle(rng, new SecureRandom());
        Iterator<Long> rngIterator = rng.iterator();

        // when
        testPurchaseRequestService.processPurchaseRequests(salesRoundId);

        // then
        for (int i = 0; i < totalItemCount; i++) {
            assertNotNull(purchaseRequests.get(i).getQueueNumber());
        }
    }

    @Test
    void deleteAllPurchaseRequests() {
        testPurchaseRequestService.deleteAllPurchaseRequests();
        verify(purchaseRequestRepository).deleteAll();
    }

    List<PurchaseRequestItemDTO> createPurchaseRequestItemDTOs() {
        PurchaseRequestItemDTO purchaseRequestItem = new PurchaseRequestItemDTO();
        purchaseRequestItem.setQuantityRequested(0);
        purchaseRequestItem.setTicketTypeId(1L);
        List<PurchaseRequestItemDTO> result = new ArrayList<>();
        result.add(purchaseRequestItem);

        return result;
    }

}