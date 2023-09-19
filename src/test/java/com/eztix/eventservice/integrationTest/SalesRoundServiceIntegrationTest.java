package com.eztix.eventservice.serviceTest;

import com.eztix.eventservice.model.SalesRound;
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
import java.time.ZoneOffset;
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

    @Test
    public void addNewSalesRound() throws Exception {
        // given
        OffsetDateTime testOffsetDateTime = OffsetDateTime.now(ZoneOffset.ofHours(8));
        SalesRound salesRound = new SalesRound();
        salesRound.setRound_start(testOffsetDateTime);
        salesRound.setRound_end(testOffsetDateTime);
        salesRound.setPurchase_start(testOffsetDateTime);
        salesRound.setPurchase_end(testOffsetDateTime);
        salesRound.setSales_type("test sales round type");


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

        Optional<SalesRound> retrieved = salesRoundRepo.findById(id);

        assertThat(retrieved).isNotNull();

    }

    @Test
    public void updateSalesRound() throws Exception {
        // given
        OffsetDateTime testOffsetDateTime = OffsetDateTime.now(ZoneOffset.ofHours(8));
        SalesRound salesRound = new SalesRound();
        salesRound.setRound_start(testOffsetDateTime);
        salesRound.setRound_end(testOffsetDateTime);
        salesRound.setPurchase_start(testOffsetDateTime);
        salesRound.setPurchase_end(testOffsetDateTime);
        salesRound.setSales_type("test sales type");

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

        String salesTypeString = JsonPath.parse(result.getContentAsString()).read("$.sales_type", String.class);


        assertThat(salesTypeString).isEqualTo(salesRound.getSales_type());

    }



}
