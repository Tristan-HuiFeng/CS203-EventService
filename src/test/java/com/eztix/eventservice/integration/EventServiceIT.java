package com.eztix.eventservice.integration;

import com.eztix.eventservice.dto.request.NewActivity;
import com.eztix.eventservice.dto.request.NewAdmissionPolicy;
import com.eztix.eventservice.dto.request.NewEvent;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.repository.EventRepository;
import com.eztix.eventservice.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import lombok.core.configuration.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Role;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-it.properties")
@AutoConfigureMockMvc
public class EventServiceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventService eventService;

    static private NewEvent eventDTO;
    static private Event event;

    @BeforeAll
    static void setup() {
        eventDTO = new NewEvent();
        eventDTO.setName("Test Event");
        eventDTO.setCategory("concert");
        eventDTO.setArtist("artist1");
        eventDTO.setDescription("This is a test event");
        eventDTO.setBannerURL("url1");
        eventDTO.setSeatMapURL("url2");
        eventDTO.setLocation("location");
        eventDTO.setIsFeatured(false);
        eventDTO.setActivities(new ArrayList<NewActivity>());
        eventDTO.setAdmissionPolicies(new ArrayList<NewAdmissionPolicy>());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @WithMockUser(roles = "admin")
    public void addNewEvent() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDTO)));

        // then
        MockHttpServletResponse result = resultActions
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn()
                .getResponse();

        Long id = JsonPath.parse(result.getContentAsString()).read("$.id", Long.class);

        Optional<Event> retrieved = eventRepository.findById(id);

        assertThat(retrieved).isNotNull();

    }

    @Test
    @WithMockUser(roles = "admin")
    public void updateEvent() throws Exception {
        // given
        Event savedEvent = eventService.addNewEvent(eventDTO);
        savedEvent.setName("Test Event Update");

        // when
        String jsonEvent = objectMapper.writeValueAsString(savedEvent);
        ResultActions resultActions = mockMvc.perform(put("/api/v1/event/{id}", savedEvent.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonEvent));

        // then
        MockHttpServletResponse result = resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse();

        String name = JsonPath.parse(result.getContentAsString()).read("$.name");


        assertThat(name).isEqualTo(savedEvent.getName());

    }



}
