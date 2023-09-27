package com.eztix.eventservice.unit;

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.Activity;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.repository.ActivityRepository;
import com.eztix.eventservice.service.ActivityService;
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
class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;
    @InjectMocks
    private ActivityService testActivityService;


    @Test
    void givenNewActivity_whenAddActivity_thenSuccess() {
        // given
        Event event = new Event();
        event.setId(1L);
        event.setName("Test Event");
        event.setCategory("concert");
        event.setArtist("artist1");
        event.setDescription("This is a test event");
        event.setBannerURL("urk1");
        event.setSeatMapURL("url2");
        event.setIsFeatured(false);

        Activity activity = new Activity();
        activity.setId(1L);
        activity.setEvent(event);
        activity.setStartDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        activity.setEndDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));

        // when
        testActivityService.addNewActivity(1L, activity);

        // then
        ArgumentCaptor<Activity> activityArgumentCaptor =
                ArgumentCaptor.forClass(Activity.class);

        verify(activityRepository).save(activityArgumentCaptor.capture());

        Activity capturedActivity = activityArgumentCaptor.getValue();

        assertThat(capturedActivity).isEqualTo(activity);
    }

    @Test
    void givenIdNotInDB_whenRetrieveById_throwResourceNotFoundException() {

        // given
        given(activityRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testActivityService.getActivityById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("activity with id %d does not exist.", 1L);

    }

    @Test
    void givenActivityExist_whenRetrieve_thenSuccessful() {

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

        Activity activity = new Activity();
        activity.setId(1L);
        activity.setEvent(event);
        activity.setStartDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        activity.setEndDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));

        given(activityRepository.findById(activity.getId())).willReturn(Optional.of(activity));

        // when

        Activity retrievedActivity = testActivityService.getActivityById(activity.getId());
        // then
        assertThat(retrievedActivity).isEqualTo(activity);

    }

    @Test
    void givenNullId_whenUpdate_throwRequestValidationException() {
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

        Activity activity = new Activity();
        activity.setEvent(event);
        activity.setStartDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        activity.setEndDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        // when
        // then
        assertThatThrownBy(() -> testActivityService.updateActivity(activity))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("activity id cannot be null.");

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

        Activity activity = new Activity();
        activity.setId(1L);
        activity.setEvent(event);
        activity.setStartDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        activity.setEndDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));

        given(activityRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testActivityService.updateActivity(activity))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("activity with id %s does not exist.", activity.getId()));

    }

    @Test
    void getAllEvents() {
        testActivityService.getAllActivity();
        verify(activityRepository).findAll();
    }
}