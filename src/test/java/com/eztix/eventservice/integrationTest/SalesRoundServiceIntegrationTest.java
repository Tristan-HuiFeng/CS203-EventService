package com.eztix.eventservice.integrationTest;

import com.eztix.eventservice.model.Activity;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.model.SalesRound;
import com.eztix.eventservice.repository.ActivityRepository;
import com.eztix.eventservice.repository.EventRepository;
import com.eztix.eventservice.repository.SalesRoundRepository;
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
public class SalesRoundServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SalesRoundRepository salesRoundRepo;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ActivityRepository activityRepository;

    @Test
    public void addNewSalesRound() throws Exception {
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
        
        SalesRound salesRound = new SalesRound();
        salesRound.setId(1L);
        salesRound.setRound_start(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setRound_end(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setPurchase_start(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setPurchase_end(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setSales_type("test sales type");
        salesRound.setActivity(activity);

        // when
        ResultActions resultActions = mockMvc.perform(post("/salesRound/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(salesRound)));

        // then
        MockHttpServletResponse result = resultActions
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn()
                .getResponse();

        Long id = JsonPath.parse(result.getContentAsString()).read("$.id", Long.class);

        Optional<SalesRound> retrieved = salesRoundRepo.findById(salesRound.getId());

        assertThat(retrieved).isNotNull();

    }

    @Test
    public void updateSalesRound() throws Exception {
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
        
        SalesRound salesRound = new SalesRound();
        salesRound.setId(1L);
        salesRound.setRound_start(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setRound_end(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setPurchase_start(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setPurchase_end(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setSales_type("test sales type");
        salesRound.setActivity(activity);

        salesRoundRepo.save(salesRound);

        salesRound.setSales_type("test sales type update");

        // when
        ResultActions resultActions = mockMvc.perform(put("/updateSalesRound/{salesRound_Id}", salesRound.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(salesRound)));

        // then
        MockHttpServletResponse result = resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse();

        String salesType = JsonPath.parse(result.getContentAsString()).read("$.sales_type", String.class);


        assertThat(salesType).isEqualTo(salesRound.getSales_type());

    }



}
