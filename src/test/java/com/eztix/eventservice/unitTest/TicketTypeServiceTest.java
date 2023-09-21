package com.eztix.eventservice.unitTest;

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

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.Activity;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.model.TicketType;
import com.eztix.eventservice.repository.TicketTypeRepository;
import com.eztix.eventservice.service.TicketTypeService;

@ExtendWith(MockitoExtension.class)
public class TicketTypeServiceTest {

    @Mock
    private TicketTypeRepository ticketTypeRepository;
    @InjectMocks
    private TicketTypeService testTicketTypeService;


    @Test
    void givenNewTicketType_whenAddTicketType_thenSuccess() {
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
        ticketType.setType("test type");
        ticketType.setActivity(activity);

        // when
        testTicketTypeService.addNewTicketType(ticketType);

        // then
        ArgumentCaptor<TicketType> ticketTypeArgumentCaptor =
                ArgumentCaptor.forClass(TicketType.class);

        verify(ticketTypeRepository).save(ticketTypeArgumentCaptor.capture());

        TicketType capturedTicketType = ticketTypeArgumentCaptor.getValue();

        assertThat(capturedTicketType).isEqualTo(ticketType);
    }

    @Test
    void givenTicketTypeIdNotInDB_whenRetrieveTicketTypeById_throwResourceNotFoundException() {

        // given
        given(ticketTypeRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testTicketTypeService.getTicketTypeById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("ticket type with id %d does not exist.", 1L);

    }

    @Test
    void givenTicketTypeExist_whenRetrieve_thenSuccessful() {

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

        given(ticketTypeRepository.findById(ticketType.getId())).willReturn(Optional.of(ticketType));

        // when
        TicketType retrievedTicketType = testTicketTypeService.getTicketTypeById(ticketType.getId());

        // then
        assertThat(retrievedTicketType).isEqualTo(ticketType);

    }

    @Test
    void givenNullId_whenUpdate_throwRequestValidationException() {
        // given
        TicketType ticketType = new TicketType();
        ticketType.setDescription("test description");
        ticketType.setOccupiedCount(0);
        ticketType.setPrice(0);
        ticketType.setReservedCount(0);
        ticketType.setTotalVacancy(0);
        ticketType.setType("test ticket type");
        // when
        // then
        assertThatThrownBy(() -> testTicketTypeService.updateTicketType(ticketType))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("ticket type id cannot be null.");

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
        ticketType.setType("test type");
        ticketType.setActivity(activity);

        given(ticketTypeRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testTicketTypeService.updateTicketType(ticketType))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("ticket type with id %s does not exist.", ticketType.getId()));

    }

    @Test
    void getAllTicketTypes() {
        testTicketTypeService.getAllTicketTypes();
        verify(ticketTypeRepository).findAll();
    }    


}
