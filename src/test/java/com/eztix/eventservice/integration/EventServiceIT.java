package com.eztix.eventservice.integration;

import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

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

    @Test
    public void addNewEvent() throws Exception {
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


        // when
        ResultActions resultActions = mockMvc.perform(post("/event/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)));

        // then
        MockHttpServletResponse result = resultActions
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn()
                .getResponse();

        Long id = JsonPath.parse(result.getContentAsString()).read("$.id", Long.class);

        Optional<Event> retrieved = eventRepository.findById(event.getId());

        assertThat(retrieved).isNotNull();

    }

    //@Test
    public void updateEvent() throws Exception {
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

        eventRepository.save(event);

        event.setName("Test Event Update");

        // when
        ResultActions resultActions = mockMvc.perform(put("/event/{id}", event.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)));

        // then
        MockHttpServletResponse result = resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse();

        String name = JsonPath.parse(result.getContentAsString()).read("$.name");


        assertThat(name).isEqualTo(event.getName());

    }



}
