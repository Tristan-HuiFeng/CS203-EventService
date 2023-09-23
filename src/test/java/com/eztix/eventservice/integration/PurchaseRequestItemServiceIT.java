package com.eztix.eventservice.integration;

import com.eztix.eventservice.model.Activity;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.model.PurchaseRequest;
import com.eztix.eventservice.model.SalesRound;
import com.eztix.eventservice.model.TicketSalesLimit;
import com.eztix.eventservice.model.TicketType;
import com.eztix.eventservice.repository.ActivityRepository;
import com.eztix.eventservice.repository.EventRepository;
import com.eztix.eventservice.repository.SalesRoundRepository;
import com.eztix.eventservice.repository.TicketSalesLimitRepository;
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
public class PurchaseRequestItemServiceIT {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private ActivityRepository activityRepository;
        @Autowired
        private EventRepository eventRepository;
        @Autowired
        private TicketSalesLimitRepository ticketSalesLimitRepo;
        @Autowired
        private SalesRoundRepository salesRoundRepository;
        @Autowired
        private TicketTypeRepository ticketTypeRepository;

        @Test
        public void addNewTicketSalesLimit() throws Exception {
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
                ticketType.setOccupiedCount(0);
                ticketType.setPrice(0);
                ticketType.setReservedCount(0);
                ticketType.setTotalVacancy(0);
                ticketType.setType("test ticket type");
                ticketType.setActivity(activity);
                ticketTypeRepository.save(ticketType);

                SalesRound salesRound = new SalesRound();
                salesRound.setId(1L);
                salesRound.setRoundStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
                salesRound.setRoundEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
                salesRound.setPurchaseStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
                salesRound.setPurchaseEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
                salesRound.setSalesType("test sales type");
                salesRound.setActivity(activity);
                salesRoundRepository.save(salesRound);

                TicketSalesLimit ticketSalesLimit = new TicketSalesLimit();
                TicketSalesLimitId ticketSalesLimitId = new TicketSalesLimitId(1L, salesRound, ticketType);
                ticketSalesLimit.setId(ticketSalesLimitId);
                ticketSalesLimit.setLimitVacancy(0);
                ticketSalesLimit.setOccupiedVacancy(0);

                PurchaseRequest purchaseRequest = new PurchaseRequest();
                purchaseRequest.setId(1L);
                purchaseRequest.setCustomer("test customer");
                purchaseRequest.setQueueNumber(1L);
                purchaseRequest.setSalesRound(salesRound);
                purchaseRequest.setStatus("test status");

                // when
                ResultActions resultActions = mockMvc.perform(post("/ticketSalesLimit/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(ticketSalesLimit)));

                // then
                MockHttpServletResponse result = resultActions
                                .andExpect(status().isCreated())
                                .andDo(print())
                                .andReturn()
                                .getResponse();

                Long id = JsonPath.parse(result.getContentAsString()).read("$.id", Long.class);

                Optional<TicketSalesLimit> retrieved = ticketSalesLimitRepo.findById(id);

                assertThat(retrieved).isNotNull();

        }

        @Test
        public void updateTicketSalesLimit() throws Exception {
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
                ticketType.setOccupiedCount(0);
                ticketType.setPrice(0);
                ticketType.setReservedCount(0);
                ticketType.setTotalVacancy(0);
                ticketType.setType("test ticket type");
                ticketType.setActivity(activity);
                ticketTypeRepository.save(ticketType);

                SalesRound salesRound = new SalesRound();
                salesRound.setId(1L);
                salesRound.setRoundStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
                salesRound.setRoundEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
                salesRound.setPurchaseStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
                salesRound.setPurchaseEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
                salesRound.setSalesType("test sales type");
                salesRound.setActivity(activity);
                salesRoundRepository.save(salesRound);

                TicketSalesLimit ticketSalesLimit = new TicketSalesLimit();
                TicketSalesLimitId ticketSalesLimitId = new TicketSalesLimitId(1L, salesRound, ticketType);
                ticketSalesLimit.setId(ticketSalesLimitId);
                ticketSalesLimit.setLimitVacancy(0);
                ticketSalesLimit.setOccupiedVacancy(0);

                ticketSalesLimitRepo.save(ticketSalesLimit);
                ticketSalesLimit.setLimitVacancy(835);

                // when
                ResultActions resultActions = mockMvc
                                .perform(put("/updateTicketSalesLimit/{ticketSalesLimit_Id}",
                                                ticketSalesLimit.getId().getId())
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(ticketSalesLimit)));

                // then
                MockHttpServletResponse result = resultActions
                                .andExpect(status().isOk())
                                .andDo(print())
                                .andReturn()
                                .getResponse();

                int limit_vacancy = JsonPath.parse(result.getContentAsString()).read("$.limit_vacancy", Integer.class);

                assertThat(limit_vacancy).isEqualTo(ticketSalesLimit.getLimitVacancy());

        }

}
