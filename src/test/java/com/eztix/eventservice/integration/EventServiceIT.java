package com.eztix.eventservice.integration;

import com.eztix.eventservice.dto.request.NewActivity;
import com.eztix.eventservice.dto.request.NewAdmissionPolicy;
import com.eztix.eventservice.dto.request.NewEvent;
import com.eztix.eventservice.dto.request.NewSalesRound;
import com.eztix.eventservice.dto.request.NewTicketSalesLimit;
import com.eztix.eventservice.model.Activity;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.model.PurchaseRequest;
import com.eztix.eventservice.model.SalesRound;
import com.eztix.eventservice.model.TicketSalesLimit;
import com.eztix.eventservice.model.TicketType;
import com.eztix.eventservice.repository.ActivityRepository;
import com.eztix.eventservice.repository.EventRepository;
import com.eztix.eventservice.repository.PurchaseRequestRepository;
import com.eztix.eventservice.repository.SalesRoundRepository;
import com.eztix.eventservice.repository.TicketSalesLimitRepository;
import com.eztix.eventservice.repository.TicketTypeRepository;
import com.eztix.eventservice.service.ActivityService;
import com.eztix.eventservice.service.EventService;
import com.eztix.eventservice.service.TicketTypeService;
import com.eztix.eventservice.service.SalesRoundService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import lombok.core.configuration.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Role;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Stream;

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

        @Autowired
        private ActivityRepository activityRepository;

        @Autowired
        private TicketTypeRepository ticketTypeRepository;

        @Autowired
        private TicketSalesLimitRepository ticketSalesLimitRepository;

        @Autowired
        private SalesRoundRepository salesRoundRepository;

        @Autowired
        private SalesRoundService salesRoundService;

        @Autowired
        private PurchaseRequestRepository purchaseRequestRepository;

        static private NewEvent eventDTO;
        static private Event event;
        static private Activity activity;
        static private TicketType ticketType;
        static private TicketSalesLimit ticketSalesLimit;
        static private NewSalesRound[] salesRoundDTOs;
        static private NewSalesRound salesRoundDTO;
        static private SalesRound salesRound;
        static private SalesRound[] salesRounds;
        static TaskScheduler taskScheduler;

        /**
         * Setup
         */
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

                activity = new Activity();
                activity.setStartDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
                activity.setEndDateTime(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(14));

                ticketType = new TicketType();
                ticketType.setOccupiedCount(0);
                ticketType.setPrice(0);
                ticketType.setReservedCount(0);
                ticketType.setTotalVacancy(100);
                ticketType.setType("test type");

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        /**
         * Teardown
         */
        @AfterEach
        static void cleanUp() {
                ((ThreadPoolTaskScheduler) taskScheduler).shutdown();
        }

        /**
         * Flow of Integration Test (testing main flow)
         * 1. Create event
         * 2. Create sales round
         * 3. Process purchase requests
         * 
         */

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
        /*
         * @Test
         * 
         * @WithMockUser(roles = "admin")
         * public void updateEvent() throws Exception {
         * // given
         * event = eventService.addNewEvent(eventDTO);
         * event.setName("Test Event Update");
         * 
         * // when
         * String updatedName = "Test Event Update";
         * 
         * String updatedEventJson = "{\"id\":\"" + event.getId() + "\", \"name\":\"" +
         * updatedName + "\"}";
         * ResultActions resultActions = mockMvc.perform(put("/api/v1/event/{id}",
         * event.getId())
         * .contentType(MediaType.APPLICATION_JSON)
         * // .content(objectMapper.writeValueAsString(event)));
         * .content(updatedEventJson));
         * 
         * // then
         * MockHttpServletResponse result = resultActions
         * .andExpect(status().isOk())
         * .andDo(print())
         * .andReturn()
         * .getResponse();
         * 
         * String name = JsonPath.parse(result.getContentAsString()).read("$.name");
         * 
         * // Fetch the updated event from the repository
         * Event updatedEvent = eventRepository.findById(event.getId()).orElse(null);
         * 
         * assertThat(updatedEvent).isNotNull();
         * assertThat(updatedEvent.getName()).isEqualTo(name);
         * }
         */

        @Test
        @WithMockUser(roles = "admin")
        public void addNewSalesRound() throws Exception {
                // given
                event = eventService.addNewEvent(eventDTO);
                activity.setEvent(event);
                activityRepository.save(activity); // associated with event and saved
                ticketType.setActivity(activity);
                ticketTypeRepository.save(ticketType); // associated with activity and saved
                salesRoundDTOs = createMockNewSalesRounds();
                // salesRounds = salesRoundService.addSalesRounds(event.getId(),
                // salesRoundDTOs); // associated with event and saved

                // when
                ResultActions resultActions = mockMvc.perform(post("/api/v1/event/{eventId}/sales-round", event.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(salesRoundDTOs)));

                // then
                MockHttpServletResponse result = resultActions
                                .andExpect(status().isCreated())
                                .andDo(print())
                                .andReturn()
                                .getResponse();

                // Long id = JsonPath.parse(result.getContentAsString()).read("$.id",
                // Long.class);

                Optional<Iterable<SalesRound>> retrieved = salesRoundRepository.findByEventId(event.getId());

                assertThat(retrieved).isNotNull();
                Iterator<SalesRound> salesRoundIterator = retrieved.get().iterator();
                SalesRound retrievedSalesRoundFromRepo = salesRoundIterator.hasNext() ? salesRoundIterator.next()
                                : null;
                assertThat(retrievedSalesRoundFromRepo).isNotNull();
                if (retrievedSalesRoundFromRepo != null) {
                        // assertThat(salesRoundDTOs[0].getPurchaseEnd()).isEqualTo(retrievedSalesRoundFromRepo.getPurchaseEnd());
                        // assertThat(salesRoundDTOs[0].getPurchaseStart()).isEqualTo(retrievedSalesRoundFromRepo.getPurchaseStart());
                        // assertThat(salesRoundDTOs[0].getRoundEnd()).isEqualTo(retrievedSalesRoundFromRepo.getRoundEnd());
                        // assertThat(salesRoundDTOs[0].getRoundStart()).isEqualTo(retrievedSalesRoundFromRepo.getRoundStart());
                        assertThat(salesRoundDTOs[0].getSalesType())
                                        .isEqualTo(retrievedSalesRoundFromRepo.getSalesType());
                }

        }

        @Test
        @WithMockUser(roles = "admin")
        public void processPurchaseRequests() throws Exception {
                // given
                event = eventService.addNewEvent(eventDTO);
                activity.setEvent(event);
                activityRepository.save(activity); // associated with event and saved
                ticketType.setActivity(activity);
                ticketTypeRepository.save(ticketType); // associated with activity and saved
                salesRoundDTOs = createMockNewSalesRounds();

                // when
                ResultActions salesRoundResultActions = mockMvc
                                .perform(post("/api/v1/event/{eventId}/sales-round", event.getId())
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(salesRoundDTOs)));

                salesRoundResultActions.andExpect(status().isCreated());
                Optional<Iterable<SalesRound>> retrieved = salesRoundRepository.findByEventId(event.getId());
                assertThat(retrieved).isPresent();

                Iterable<SalesRound> salesRounds = retrieved.get();
                Long salesRoundId = salesRounds.iterator().next().getId();

                // wait for the scheduled processPurchaseRequests to run
                // Thread.sleep(5000);

                Stream<PurchaseRequest> purchaseRequests = purchaseRequestRepository.findBySalesRoundId(salesRoundId);
                // verify changes and assert based on the changes expected in the purchase
                // requests after processing
        }

        private NewSalesRound[] createMockNewSalesRounds() {
                NewSalesRound mockSalesRoundDTO = new NewSalesRound();
                mockSalesRoundDTO.setRoundStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).minusDays(3));
                mockSalesRoundDTO.setRoundEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
                mockSalesRoundDTO.setPurchaseStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).minusDays(3));
                mockSalesRoundDTO.setPurchaseEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
                mockSalesRoundDTO.setSalesType("test sales type");
                mockSalesRoundDTO.setTicketSalesLimitList(createMockTicketSalesLimits());

                NewSalesRound[] mockNewSalesRounds = { mockSalesRoundDTO };
                return mockNewSalesRounds;
        }

        private List<NewTicketSalesLimit> createMockTicketSalesLimits() {
                NewTicketSalesLimit mockTicketSalesLimitDTO = new NewTicketSalesLimit();
                mockTicketSalesLimitDTO.setLimitVacancy(100);
                mockTicketSalesLimitDTO.setTicketTypeId(ticketType.getId());

                List<NewTicketSalesLimit> mockNewTicketSalesLimits = new ArrayList<>();
                mockNewTicketSalesLimits.add(mockTicketSalesLimitDTO);
                return mockNewTicketSalesLimits;
        }

}
