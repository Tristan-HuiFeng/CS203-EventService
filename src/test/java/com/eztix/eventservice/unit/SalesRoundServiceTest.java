package com.eztix.eventservice.unit;

import com.eztix.eventservice.model.TicketSalesLimit;
import com.eztix.eventservice.service.EventService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.TaskScheduler;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eztix.eventservice.dto.request.NewSalesRound;
import com.eztix.eventservice.dto.request.NewTicketSalesLimit;
import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.Activity;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.model.SalesRound;
import com.eztix.eventservice.repository.EventRepository;
import com.eztix.eventservice.repository.SalesRoundRepository;
import com.eztix.eventservice.service.SalesRoundService;
import com.eztix.eventservice.service.TicketSalesLimitService;
import com.eztix.eventservice.service.TicketTypeService;

@ExtendWith(MockitoExtension.class)
public class SalesRoundServiceTest {

    @Mock
    private TaskScheduler taskScheduler;
    @Mock
    private TicketSalesLimitService ticketSalesLimitService;
    @Mock
    private TicketTypeService ticketTypeService;
    @Mock
    private EventService eventService;
    @Mock
    private SalesRoundRepository salesRoundRepository;
    @InjectMocks
    private SalesRoundService testSalesRoundService;

    static private Event event;

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
    }
    @BeforeEach
    void setup1() {
        testSalesRoundService.setEventService(eventService);
    }
    

    @Test
    void givenNewSalesRound_whenAddSalesRound_thenSuccess() {
        // given
        NewSalesRound[] salesRounds = createMockNewSalesRounds();
        NewSalesRound salesRound = salesRounds[0];
        // List<SalesRound> salesRoundList = new ArrayList<>();
        // SalesRound inputSalesRound = newSalesRoundToSalesRound(salesRound);
        // salesRoundList.add(inputSalesRound);

        SalesRound returnedSalesRound = newSalesRoundToSalesRound(salesRound, event);

        when(salesRoundRepository.save(any(SalesRound.class))).thenReturn(returnedSalesRound);

        // when
        testSalesRoundService.addSalesRounds(1L, salesRounds);

        // then
        ArgumentCaptor<SalesRound> salesRoundArgumentCaptor = ArgumentCaptor.forClass(SalesRound.class);

        verify(salesRoundRepository).save(salesRoundArgumentCaptor.capture());

        SalesRound capturedSalesRound = salesRoundArgumentCaptor.getValue();
        capturedSalesRound.setEvent(event);

        assertThat(capturedSalesRound).isEqualTo(returnedSalesRound);
    }

    @Test
    void givenSalesRoundIdNotInDB_whenRetrieveSalesRoundById_throwResourceNotFoundException() {

        // given
        given(salesRoundRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testSalesRoundService.getSalesRoundById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("sales round with id %d does not exist", 1L);

    }

    @Test
    void givenSalesRoundExist_whenRetrieve_thenSuccessful() {

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
        // activityRepository.save(activity);

        SalesRound salesRound = new SalesRound();
        salesRound.setRoundStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setRoundEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setPurchaseStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setPurchaseEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setSalesType("test sales type");
        // salesRound.setActivity(activity);

        given(salesRoundRepository.findById(salesRound.getId())).willReturn(Optional.of(salesRound));

        // when
        SalesRound retrievedSalesRound = testSalesRoundService.getSalesRoundById(salesRound.getId());

        // then
        assertThat(retrievedSalesRound).isEqualTo(salesRound);

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
        // activityRepository.save(activity);

        SalesRound salesRound = new SalesRound();
        salesRound.setRoundStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setRoundEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setPurchaseStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setPurchaseEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setSalesType("test sales type");
        // salesRound.setActivity(activity);
        // when
        // then
        assertThatThrownBy(() -> testSalesRoundService.updateSalesRound(salesRound))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("sales round id cannot be null");

    }

    @Test
    void givenIdNotInDB_whenUpdate_throwRequestNotFoundException() {
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
        // activityRepository.save(activity);

        SalesRound salesRound = new SalesRound();
        salesRound.setId(1L);
        salesRound.setRoundStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setRoundEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setPurchaseStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setPurchaseEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setSalesType("test sales type");
        // salesRound.setActivity(activity);

        given(salesRoundRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testSalesRoundService.updateSalesRound(salesRound))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("sales round with id %s not found", salesRound.getId()));

    }

    @Test
    void getAllSalesRounds() {
        SalesRound salesRound = new SalesRound();
        salesRound.setId(1L);
        salesRound.setRoundStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setRoundEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setPurchaseStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setPurchaseEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setSalesType("test sales type");

        List<SalesRound> salesRoundList = new ArrayList<>();
        salesRoundList.add(salesRound);

        given(salesRoundRepository.findByEventId(1L)).willReturn(Optional.of(salesRoundList));
        testSalesRoundService.getSalesRoundByEventId(1L);
        verify(salesRoundRepository).findByEventId(1L);
    }

    private NewSalesRound[] createMockNewSalesRounds() {
        NewSalesRound salesRound = new NewSalesRound();
        salesRound.setRoundStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setRoundEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setPurchaseStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setPurchaseEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setSalesType("test sales type");
        List<NewTicketSalesLimit> ticketSalesLimitList = new ArrayList<>();
        ticketSalesLimitList.add(new NewTicketSalesLimit());
        salesRound.setTicketSalesLimitList(ticketSalesLimitList);

        NewSalesRound[] salesRounds = { salesRound };
        return salesRounds;
    }

    private SalesRound newSalesRoundToSalesRound(NewSalesRound salesRound, Event event) {


        SalesRound result = SalesRound.builder()
                .roundStart(salesRound.getRoundStart())
                .roundEnd(salesRound.getRoundEnd())
                .purchaseStart(salesRound.getPurchaseStart())
                .purchaseEnd(salesRound.getPurchaseEnd())
                .salesType(salesRound.getSalesType())
                .build();
        result.setEvent(event);

        return result;
    }

}
