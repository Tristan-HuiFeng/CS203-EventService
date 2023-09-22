package com.eztix.eventservice.integration;

import com.eztix.eventservice.model.Activity;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.repository.ActivityRepository;
import com.eztix.eventservice.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.time.ZoneId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
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
public class ActivityServiceIT {

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
        activity.setName("Test Activity");
        activity.setEvent(event);
        activity.setStartDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        activity.setEndDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        activity.setLocation("Test Location");

        // when
        ResultActions resultActions = mockMvc.perform(post("/activity/add").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(activity)));
        // then
        MockHttpServletResponse result = resultActions.andExpect(status().isCreated()).andDo(print()).andReturn().getResponse();

        Long id = JsonPath.parse(result.getContentAsString()).read("$.activityId", Long.class);

        Optional<Activity> retrieved = activityRepository.findById(activity.getId());

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
        activity.setId(2L);
        activity.setName("Test Activity");
        activity.setEvent(event);
        activity.setStartDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        activity.setEndDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        activity.setLocation("Test Location");
        activityRepository.save(activity);

        activity.setName("Activity name test update");
        // when
        ResultActions resultActions = mockMvc.perform(put("/activity/{id}", activity.getId()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(activity)));

        // then
        MockHttpServletResponse result = resultActions.andExpect(status().isOk()).andDo(print()).andReturn().getResponse();

        String name = JsonPath.parse(result.getContentAsString()).read("$.activityName", String.class);

        assertThat(name).isEqualTo(activity.getName());

    }

}