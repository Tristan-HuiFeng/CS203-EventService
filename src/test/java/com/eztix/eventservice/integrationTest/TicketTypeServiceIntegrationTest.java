package com.eztix.eventservice.integrationTest;

import com.eztix.eventservice.model.Activity;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.model.TicketType;
import com.eztix.eventservice.repository.ActivityRepository;
import com.eztix.eventservice.repository.EventRepository;
import com.eztix.eventservice.repository.TicketTypeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-it.properties")
@AutoConfigureMockMvc
public class TicketTypeServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TicketTypeRepository ticketTypeRepo;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private EventRepository eventRepository;

    @Test
    public void addNewTicketType() throws Exception {
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

        Activity activity = new Activity();
        activity.setId(1L);
        activity.setName("Test Activity");
        activity.setEvent(event);
        activity.setStartDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        activity.setEndDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        activity.setLocation("Test Location");
        activityRepository.save(activity);

        TicketType ticketType = new TicketType();
        ticketType.setId(1L);
        ticketType.setDescription("test description");
        ticketType.setOccupied_count(0);
        ticketType.setPrice(0);
        ticketType.setReserved_count(0);
        ticketType.setTotal_vacancy(0);
        ticketType.setType("test type");

        // when
        ResultActions resultActions = mockMvc.perform(post("/ticketType/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticketType)));

        // then
        MockHttpServletResponse result = resultActions
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn()
                .getResponse();

        Long id = JsonPath.parse(result.getContentAsString()).read("$.id", Long.class);

        Optional<TicketType> retrieved = ticketTypeRepo.findById(ticketType.getId());

        assertThat(retrieved).isNotNull();

    }

    @Test
    public void updateTicketType() throws Exception {
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

        Activity activity = new Activity();
        activity.setId(1L);
        activity.setName("Test Activity");
        activity.setEvent(event);
        activity.setStartDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        activity.setEndDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        activity.setLocation("Test Location");
        activityRepository.save(activity);

        TicketType ticketType = new TicketType();
        ticketType.setId(1L);
        ticketType.setDescription("test description");
        ticketType.setOccupied_count(0);
        ticketType.setPrice(0);
        ticketType.setReserved_count(0);
        ticketType.setTotal_vacancy(0);
        ticketType.setType("test type");

        ticketTypeRepo.save(ticketType);

        ticketType.setDescription("test description update");

        // when
        ResultActions resultActions = mockMvc.perform(put("/updateTicketType/{ticketType_Id}", ticketType.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticketType)));

        // then
        MockHttpServletResponse result = resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse();

        String description = JsonPath.parse(result.getContentAsString()).read("$.description");

        assertThat(description).isEqualTo(ticketType.getDescription());

        }

}
