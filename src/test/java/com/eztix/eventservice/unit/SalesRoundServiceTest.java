package com.eztix.eventservice.unit;

import com.eztix.eventservice.model.TicketSalesLimit;
import com.eztix.eventservice.service.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.Activity;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.model.SalesRound;
import com.eztix.eventservice.repository.SalesRoundRepository;
import com.eztix.eventservice.service.SalesRoundService;

@ExtendWith(MockitoExtension.class)
public class SalesRoundServiceTest {

    @Mock
    private SalesRoundRepository salesRoundRepository;
    @InjectMocks
    private SalesRoundService testSalesRoundService;
    @Mock
    private static EventService eventService;

    @Test
    void givenNewSalesRound_whenAddSalesRound_thenSuccess() {
        // given
        
        SalesRound salesRound = new SalesRound();
        salesRound.setRoundStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setRoundEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setPurchaseStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setPurchaseEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setSalesType("test sales type");

        List<TicketSalesLimit> ticketSalesLimitList = new ArrayList<>();
        ticketSalesLimitList.add(new TicketSalesLimit());

        salesRound.setTicketSalesLimits(ticketSalesLimitList);

        given(eventService.getEventById(1L)).willReturn(new Event());

        // when
        testSalesRoundService.addNewSalesRound(1L, salesRound);

        // then
        ArgumentCaptor<SalesRound> salesRoundArgumentCaptor =
                ArgumentCaptor.forClass(SalesRound.class);

        verify(salesRoundRepository).save(salesRoundArgumentCaptor.capture());

        SalesRound capturedSalesRound = salesRoundArgumentCaptor.getValue();

        assertThat(capturedSalesRound).isEqualTo(salesRound);
    }

    @Test
    void givenSalesRoundIdNotInDB_whenRetrieveSalesRoundById_throwResourceNotFoundException() {

        // given
        given(salesRoundRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testSalesRoundService.getSalesRoundById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("sales round with id %d does not exist", 1L);

    }

    @Test
    void givenSalesRoundExist_whenRetrieve_thenSuccessful() {

        // given
        SalesRound salesRound = new SalesRound();
        salesRound.setRoundStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setRoundEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setPurchaseStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setPurchaseEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setSalesType("test sales type");

        given(salesRoundRepository.findById(salesRound.getId())).willReturn(Optional.of(salesRound));

        // when
        SalesRound retrievedSalesRound = testSalesRoundService.getSalesRoundById(salesRound.getId());

        // then
        assertThat(retrievedSalesRound).isEqualTo(salesRound);

    }

    @Test
    void givenNullId_whenUpdate_throwRequestValidationException() {
        // given
        SalesRound salesRound = new SalesRound();
        salesRound.setRoundStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setRoundEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setPurchaseStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setPurchaseEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setSalesType("test sales type");
        // when
        // then
        assertThatThrownBy(() -> testSalesRoundService.updateSalesRound(salesRound))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("sales round id cannot be null");

    }

    @Test
    void givenIdNotInDB_whenUpdate_throwRequestNotFoundException() {
        // given
        SalesRound salesRound = new SalesRound();
        salesRound.setId(1L);
        salesRound.setRoundStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setRoundEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setPurchaseStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setPurchaseEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setSalesType("test sales type");

        given(salesRoundRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testSalesRoundService.updateSalesRound(salesRound))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("sales round with id %s not found", salesRound.getId()));

    }

    @Test
    void getAllSalesRounds() {
        SalesRound salesRound = new SalesRound();
        salesRound.setId(1L);
        salesRound.setRoundStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setRoundEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setPurchaseStart(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(3));
        salesRound.setPurchaseEnd(OffsetDateTime.now(ZoneId.of("Asia/Singapore")).plusDays(7));
        salesRound.setSalesType("test sales type");

        List<SalesRound> salesRoundList = new ArrayList<>();
        salesRoundList.add(salesRound);

        given(salesRoundRepository.findByEventId(1L)).willReturn(Optional.of(salesRoundList));
        testSalesRoundService.getSalesRoundByEventId(1L);
        verify(salesRoundRepository).findByEventId(1L);
    }    


}
