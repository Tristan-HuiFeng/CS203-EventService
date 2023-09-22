package com.eztix.eventservice.integration;

import com.eztix.eventservice.model.TicketType;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-it.properties")
@AutoConfigureMockMvc
public class TicketTypeServiceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TicketTypeRepository ticketTypeRepo;

    @Test
    public void addNewTicketType() throws Exception {
        // given
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

        Optional<TicketType> retrieved = ticketTypeRepo.findById(id);

        assertThat(retrieved).isNotNull();

    }

    @Test
    public void updateTicketType() throws Exception {
        // given
        TicketType ticketType = new TicketType();
        ticketType.setId(1L);
        ticketType.setDescription("test description");
        ticketType.setOccupied_count(0);
        ticketType.setPrice(0);
        ticketType.setReserved_count(0);
        ticketType.setTotal_vacancy(0);
        ticketType.setType("test type");

        TicketType savedTicketType = ticketTypeRepo.save(ticketType);
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

        String description = JsonPath.parse(result.getContentAsString()).read("$.description", String.class);


        assertThat(description).isEqualTo(ticketType.getDescription());

    }



}
