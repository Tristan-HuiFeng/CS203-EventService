package com.eztix.eventservice.unit;

import com.eztix.eventservice.dto.request.NewActivity;
import com.eztix.eventservice.dto.request.NewAdmissionPolicy;
import com.eztix.eventservice.dto.request.NewEvent;
import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.repository.EventRepository;
import com.eztix.eventservice.service.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;
    @InjectMocks
    private EventService testEventService;


    @Test
    void givenNewEvent_whenAddEvent_thenSuccess() {
        // given
        NewEvent event = new NewEvent();
        event.setName("Test Event");
        event.setCategory("concert");
        event.setArtist("artist1");
        event.setDescription("This is a test event");
        event.setBannerURL("urk1");
        event.setSeatMapURL("url2");
        event.setIsFeatured(false);
        event.setLocation("somewhere over the rainbow");
        List<NewActivity> activities = new ArrayList<>();
        event.setActivities(activities);
        List<NewAdmissionPolicy> admissionPolicies = new ArrayList<>();
        event.setAdmissionPolicies(admissionPolicies);

        // when
        testEventService.addNewEvent(event);

        // then
        ArgumentCaptor<Event> eventArgumentCaptor =
                ArgumentCaptor.forClass(Event.class);

        verify(eventRepository).save(eventArgumentCaptor.capture());

        Event capturedEvent = eventArgumentCaptor.getValue();
        NewEvent capturedNewEvent = new NewEvent();
        capturedNewEvent.setName(capturedEvent.getName());
        capturedNewEvent.setArtist(event.getArtist());
        capturedNewEvent.setDescription(event.getDescription());
        capturedNewEvent.setBannerURL(event.getBannerURL());
        capturedNewEvent.setSeatMapURL(event.getSeatMapURL());
        capturedNewEvent.setIsFeatured(event.getIsFeatured());
        capturedNewEvent.setLocation(event.getLocation());
        capturedNewEvent.setCategory(event.getCategory());
        capturedNewEvent.setActivities(new ArrayList<>());
        capturedNewEvent.setAdmissionPolicies(new ArrayList<>());

        assertThat(event).isEqualTo(capturedNewEvent);
    }

    @Test
    void givenIdNotInDB_whenRetrieveById_throwResourceNotFoundException() {

        // given
        given(eventRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testEventService.getEventById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("event with id %d does not exist.", 1L);

    }

    @Test
    void givenEventExist_whenRetrieve_thenSuccessful() {

        // given
        Event event = new Event();
        event.setName("Test Event");
        event.setCategory("concert");
        event.setArtist("artist1");
        event.setDescription("This is a test event");
        event.setBannerURL("url1");
        event.setSeatMapURL("url2");
        event.setIsFeatured(false);

        given(eventRepository.findById(event.getId())).willReturn(Optional.of(event));

        // when

        Event retrievedEvent = testEventService.getEventById(event.getId());
        // then
        assertThat(retrievedEvent).isEqualTo(event);

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
        // when
        // then
        assertThatThrownBy(() -> testEventService.updateEvent(event))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("event id cannot be null.");

    }

    @Test
    void givenIdNotInDB_whenUpdate_throwResourceNotFoundException() {
        // given
        Event event = new Event();
        event.setId(1L);
        event.setName("Test Event");
        event.setCategory("concert");
        event.setArtist("artist1");
        event.setDescription("This is a test event");
        event.setBannerURL("url1");
        event.setSeatMapURL("url2");
        event.setIsFeatured(false);

        given(eventRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testEventService.updateEvent(event))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("event with id %s does not exist.", event.getId()));

    }

    @Test
    void getAllEventsFeatured() {
        testEventService.getAllEvents(true, "none", "none");
        verify(eventRepository).findAllByIsFeaturedTrueOrderByFeatureSequence();
    }

    @Test
    void getAllEventsNotFeatured() {
        testEventService.getAllEvents(false, "none", "none");
        verify(eventRepository).findAll();
    }

}