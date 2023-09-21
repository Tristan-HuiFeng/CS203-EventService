package com.eztix.eventservice.unitTest;

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.Activity;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.model.SalesRound;
import com.eztix.eventservice.model.TicketSalesLimit;
import com.eztix.eventservice.model.TicketSalesLimitId;
import com.eztix.eventservice.model.TicketType;
import com.eztix.eventservice.repository.TicketSalesLimitRepository;
import com.eztix.eventservice.service.TicketSalesLimitService;

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
class TicketSalesLimitServiceTest {

    @Mock
    private TicketSalesLimitRepository ticketSalesLimitRepository;
    @InjectMocks
    private TicketSalesLimitService testTicketSalesLimitService;

    @Test
    void givenNewTicketSalesLimit_whenAddTicketSalesLimit_thenSuccess() {
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
        activity.setName("Test Activity");
        activity.setEvent(event);
        activity.setStartDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        activity.setEndDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        activity.setLocation("Test Location");
        // activityRepository.save(activity);

        TicketType ticketType = new TicketType();
        ticketType.setDescription("test description");
        ticketType.setOccupiedCount(0);
        ticketType.setPrice(0);
        ticketType.setReservedCount(0);
        ticketType.setTotalVacancy(0);
        ticketType.setType("test ticket type");
        ticketType.setActivity(activity);
        // ticketTypeRepository.save(ticketType);

        SalesRound salesRound = new SalesRound();
        salesRound.setRoundStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setRoundEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setPurchaseStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setPurchaseEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setSalesType("test sales type");
        salesRound.setActivity(activity);
        // salesRoundRepository.save(salesRound);

        TicketSalesLimit ticketSalesLimit = new TicketSalesLimit();
        ticketSalesLimit.setLimitVacancy(0);
        ticketSalesLimit.setOccupiedVacancy(0);
        ticketSalesLimit.setSalesRound(salesRound);
        ticketSalesLimit.setTicketType(ticketType);

        // when
        testTicketSalesLimitService.addNewTicketSalesLimit(ticketSalesLimit);

        // then
        ArgumentCaptor<TicketSalesLimit> eventArgumentCaptor = ArgumentCaptor.forClass(TicketSalesLimit.class);

        verify(ticketSalesLimitRepository).save(eventArgumentCaptor.capture());

        TicketSalesLimit capturedTicketSalesLimit = eventArgumentCaptor.getValue();

        assertThat(capturedTicketSalesLimit).isEqualTo(ticketSalesLimit);
    }

    @Test
    void givenIdNotInDB_whenRetrieveByTicketSalesLimitId_throwResourceNotFoundException() {

        // given
        given(ticketSalesLimitRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testTicketSalesLimitService.getTicketSalesLimitById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("ticket sales limit with id %d does not exist.", 1L);
    }

    @Test
    void givenTicketSalesLimitExist_whenRetrieve_thenSuccessful() {

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
        activity.setName("Test Activity");
        activity.setEvent(event);
        activity.setStartDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        activity.setEndDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        activity.setLocation("Test Location");
        // activityRepository.save(activity);

        TicketType ticketType = new TicketType();
        ticketType.setDescription("test description");
        ticketType.setOccupiedCount(0);
        ticketType.setPrice(0);
        ticketType.setReservedCount(0);
        ticketType.setTotalVacancy(0);
        ticketType.setType("test ticket type");
        ticketType.setActivity(activity);
        // ticketTypeRepository.save(ticketType);

        SalesRound salesRound = new SalesRound();
        salesRound.setRoundStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setRoundEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setPurchaseStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setPurchaseEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setSalesType("test sales type");
        salesRound.setActivity(activity);
        // salesRoundRepository.save(salesRound);

        TicketSalesLimit ticketSalesLimit = new TicketSalesLimit();
        TicketSalesLimitId ticketSalesLimitId = new TicketSalesLimitId();
        ticketSalesLimitId.setSalesRound(salesRound);
        ticketSalesLimitId.setTicketType(ticketType);
        ticketSalesLimit.setId(ticketSalesLimitId);
        ticketSalesLimit.setLimitVacancy(0);
        ticketSalesLimit.setOccupiedVacancy(0);

        given(ticketSalesLimitRepository.findById(ticketSalesLimit.getId().getId()))
                .willReturn(Optional.of(ticketSalesLimit));

        // when
        TicketSalesLimit retrievedTicketSalesLimit = testTicketSalesLimitService
                .getTicketSalesLimitById(ticketSalesLimit.getId().getId());
        // then
        assertThat(retrievedTicketSalesLimit).isEqualTo(ticketSalesLimit);

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
        activity.setName("Test Activity");
        activity.setEvent(event);
        activity.setStartDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        activity.setEndDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        activity.setLocation("Test Location");
        // activityRepository.save(activity);

        TicketType ticketType = new TicketType();
        ticketType.setDescription("test description");
        ticketType.setOccupiedCount(0);
        ticketType.setPrice(0);
        ticketType.setReservedCount(0);
        ticketType.setTotalVacancy(0);
        ticketType.setType("test ticket type");
        ticketType.setActivity(activity);
        // ticketTypeRepository.save(ticketType);

        SalesRound salesRound = new SalesRound();
        salesRound.setRoundStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setRoundEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setPurchaseStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setPurchaseEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setSalesType("test sales type");
        salesRound.setActivity(activity);
        // salesRoundRepository.save(salesRound);

        TicketSalesLimit ticketSalesLimit = new TicketSalesLimit();
        TicketSalesLimitId ticketSalesLimitId = new TicketSalesLimitId();
        ticketSalesLimitId.setSalesRound(salesRound);
        ticketSalesLimitId.setTicketType(ticketType);
        ticketSalesLimit.setId(ticketSalesLimitId);
        ticketSalesLimit.setLimitVacancy(0);
        ticketSalesLimit.setOccupiedVacancy(0);

        // when
        // then
        assertThatThrownBy(() -> testTicketSalesLimitService.updateTicketSalesLimit(ticketSalesLimit))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("ticket sales limit id cannot be null.");

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
        activity.setName("Test Activity");
        activity.setEvent(event);
        activity.setStartDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        activity.setEndDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        activity.setLocation("Test Location");
        // activityRepository.save(activity);

        TicketType ticketType = new TicketType();
        ticketType.setId(1L);
        ticketType.setDescription("test description");
        ticketType.setOccupiedCount(0);
        ticketType.setPrice(0);
        ticketType.setReservedCount(0);
        ticketType.setTotalVacancy(0);
        ticketType.setType("test ticket type");
        ticketType.setActivity(activity);
        // ticketTypeRepository.save(ticketType);

        SalesRound salesRound = new SalesRound();
        salesRound.setId(1L);
        salesRound.setRoundStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setRoundEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setPurchaseStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setPurchaseEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setSalesType("test sales type");
        salesRound.setActivity(activity);
        // salesRoundRepository.save(salesRound);

        TicketSalesLimit ticketSalesLimit = new TicketSalesLimit();
        TicketSalesLimitId ticketSalesLimitId = new TicketSalesLimitId(1L, salesRound, ticketType);
        ticketSalesLimit.setId(ticketSalesLimitId);
        ticketSalesLimit.setLimitVacancy(0);
        ticketSalesLimit.setOccupiedVacancy(0);

        given(ticketSalesLimitRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testTicketSalesLimitService.updateTicketSalesLimit(ticketSalesLimit))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("ticket sales limit with id %d does not exist.", 1L);

    }

    @Test
    void getAllEvents() {
        testTicketSalesLimitService.getAllTicketSalesLimits();
        verify(ticketSalesLimitRepository).findAll();
    }
}