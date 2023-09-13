package com.eztix.eventservice.ActivityTest;

import com.eztix.eventservice.model.Activity;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.repository.ActivityRepository;
import com.eztix.eventservice.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.*;
@SpringBootTest
@TestPropertySource(locations = "classpath:application-it.properties")
@AutoConfigureMockMvc
public class ActivityServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private EventRepository eventRepository;

    @Test
    public void addNewActivity() throws Exception {

//         given event
        Event event = new Event();
        event.setId(1L);
        event.setName("Test Event");
        event.setCategory("concert");
        event.setArtist("artist1");
        event.setDescription("This is a test event");
        event.setBannerURL("url1");
        event.setSeatMapURL("url2");
        event.setIsFeatured(false);

        //given activity
        Activity activity = new Activity();
        activity.setActivityId(1L);
        activity.setActivityName("Test Activity");
        activity.setEvent(event);
        activity.setStartDateTime(LocalDateTime.now().plusDays(1));
        activity.setEndDateTime(LocalDateTime.now().plusDays(3));
        activity.setLocation("Test Location");

        // when
        ResultActions resultActions = mockMvc.perform(post("/activity/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(activity)));
        // then
        MockHttpServletResponse result = resultActions
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn()
                .getResponse();

        Long id = JsonPath.parse(result.getContentAsString()).read("$.activityId", Long.class);

        Optional<Activity> retrieved = activityRepository.findById(activity.getActivityId());

        assertThat(retrieved).isNotNull();
    }

    @Test
    public void updateActivity() throws Exception {

// given
        Event event = new Event();
        event.setId(2L);
        event.setName("Test Event");
        event.setCategory("concert");
        event.setArtist("artist1");
        event.setDescription("This is a test event");
        event.setBannerURL("url1");
        event.setSeatMapURL("url2");
        event.setIsFeatured(false);
        eventRepository.save(event);

        Activity activity = new Activity();
        activity.setActivityId(2L);
        activity.setActivityName("Test Activity");
        activity.setEvent(event);
        activity.setStartDateTime(LocalDateTime.now().plusDays(1));
        activity.setEndDateTime(LocalDateTime.now().plusDays(3));
        activity.setLocation("Test Location");
        activityRepository.save(activity);

        activity.setActivityName("Activity name test update");
        // when
        ResultActions resultActions = mockMvc.perform(put("/activity/{id}", activity.getActivityId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(activity)));

        // then
        MockHttpServletResponse result = resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse();

        String name = JsonPath.parse(result.getContentAsString()).read("$.activityName", String.class);


        assertThat(name).isEqualTo(activity.getActivityName());

    }

}

