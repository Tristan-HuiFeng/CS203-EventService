package com.eztix.eventservice.unit;

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.Activity;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.model.PurchaseRequest;
import com.eztix.eventservice.model.SalesRound;
import com.eztix.eventservice.repository.PurchaseRequestRepository;
import com.eztix.eventservice.service.PurchaseRequestService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PurchaseRequestServiceTest {

    @Mock
    private PurchaseRequestRepository purchaseRequestRepository;
    @InjectMocks
    private PurchaseRequestService testPurchaseRequestService;

    @Test
    void givenNewPurchaseRequest_whenAddPurchaseRequest_thenSuccess() {
        // given
        Event event = new Event();
        event.setName("Test Event");
        event.setCategory("concert");
        event.setArtist("artist1");
        event.setDescription("This is a test event");
        event.setBannerURL("url1");
        event.setSeatMapURL("url2");
        event.setIsFeatured(false);
        // eventRepository.save(event);

        Activity activity = new Activity();
        activity.setEvent(event);
        activity.setStartDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        activity.setEndDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        activity.setLocation("Test Location");
        // activityRepository.save(activity);

        SalesRound salesRound = new SalesRound();
        salesRound.setRoundStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setRoundEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setPurchaseStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setPurchaseEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setSalesType("test sales type");
        salesRound.setActivity(activity);
        // salesRoundRepository.save(salesRound);

        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setCustomer("test customer");
        purchaseRequest.setQueueNumber(1L);
        purchaseRequest.setSalesRound(salesRound);
        purchaseRequest.setStatus("test status");

        // when
        testPurchaseRequestService.addNewPurchaseRequest(purchaseRequest);

        // then
        ArgumentCaptor<PurchaseRequest> eventArgumentCaptor = ArgumentCaptor.forClass(PurchaseRequest.class);

        verify(purchaseRequestRepository).save(eventArgumentCaptor.capture());

        PurchaseRequest capturedPurchaseRequest = eventArgumentCaptor.getValue();

        assertThat(capturedPurchaseRequest).isEqualTo(purchaseRequest);
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
        Event event = new Event();
        event.setName("Test Event");
        event.setCategory("concert");
        event.setArtist("artist1");
        event.setDescription("This is a test event");
        event.setBannerURL("url1");
        event.setSeatMapURL("url2");
        event.setIsFeatured(false);
        // eventRepository.save(event);

        Activity activity = new Activity();
        activity.setEvent(event);
        activity.setStartDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        activity.setEndDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        activity.setLocation("Test Location");
        // activityRepository.save(activity);

        SalesRound salesRound = new SalesRound();
        salesRound.setRoundStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setRoundEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setPurchaseStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setPurchaseEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setSalesType("test sales type");
        salesRound.setActivity(activity);
        // salesRoundRepository.save(salesRound);

        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setCustomer("test customer");
        purchaseRequest.setQueueNumber(1L);
        purchaseRequest.setSalesRound(salesRound);
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
        Event event = new Event();
        event.setName("Test Event");
        event.setCategory("concert");
        event.setArtist("artist1");
        event.setDescription("This is a test event");
        event.setBannerURL("url1");
        event.setSeatMapURL("url2");
        event.setIsFeatured(false);
        // eventRepository.save(event);

        Activity activity = new Activity();
        activity.setEvent(event);
        activity.setStartDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        activity.setEndDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        activity.setLocation("Test Location");
        // activityRepository.save(activity);

        SalesRound salesRound = new SalesRound();
        salesRound.setRoundStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setRoundEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setPurchaseStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setPurchaseEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setSalesType("test sales type");
        salesRound.setActivity(activity);
        // salesRoundRepository.save(salesRound);

        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setCustomer("test customer");
        purchaseRequest.setQueueNumber(1L);
        purchaseRequest.setSalesRound(salesRound);
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
        Event event = new Event();
        event.setName("Test Event");
        event.setCategory("concert");
        event.setArtist("artist1");
        event.setDescription("This is a test event");
        event.setBannerURL("url1");
        event.setSeatMapURL("url2");
        event.setIsFeatured(false);
        // eventRepository.save(event);

        Activity activity = new Activity();
        activity.setEvent(event);
        activity.setStartDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        activity.setEndDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        activity.setLocation("Test Location");
        // activityRepository.save(activity);

        SalesRound salesRound = new SalesRound();
        salesRound.setRoundStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setRoundEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setPurchaseStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setPurchaseEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setSalesType("test sales type");
        salesRound.setActivity(activity);
        // salesRoundRepository.save(salesRound);

        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setId(1L);
        purchaseRequest.setCustomer("test customer");
        purchaseRequest.setQueueNumber(1L);
        purchaseRequest.setSalesRound(salesRound);
        purchaseRequest.setStatus("test status");

        given(purchaseRequestRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testPurchaseRequestService.updatePurchaseRequest(purchaseRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("purchase request with id %d does not exist.", 1L);

    }

    @Test
    void getAllEvents() {
        testPurchaseRequestService.getAllPurchaseRequests();
        verify(purchaseRequestRepository).findAll();
    }
}