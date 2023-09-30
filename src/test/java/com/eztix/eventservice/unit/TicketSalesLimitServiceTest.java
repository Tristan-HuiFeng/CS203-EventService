package com.eztix.eventservice.unit;

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.TicketSalesLimit;
import com.eztix.eventservice.repository.TicketSalesLimitRepository;
import com.eztix.eventservice.service.TicketSalesLimitService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TicketSalesLimitServiceTest {

    @Mock
    private TicketSalesLimitRepository ticketSalesLimitRepository;
    @InjectMocks
    private TicketSalesLimitService testTicketSalesLimitService;

    @Test
    void givenNewTicketSalesLimit_whenAddTicketSalesLimit_thenSuccess() {
        // given

        TicketSalesLimit ticketSalesLimit = new TicketSalesLimit();
        ticketSalesLimit.setLimitVacancy(0);
        ticketSalesLimit.setOccupiedVacancy(0);

        // when
        testTicketSalesLimitService.addNewTicketSalesLimit(ticketSalesLimit);

        // then
        ArgumentCaptor<TicketSalesLimit> eventArgumentCaptor = ArgumentCaptor.forClass(TicketSalesLimit.class);

        verify(ticketSalesLimitRepository).save(eventArgumentCaptor.capture());

        TicketSalesLimit capturedTicketSalesLimit = eventArgumentCaptor.getValue();

        assertThat(capturedTicketSalesLimit).isEqualTo(ticketSalesLimit);
    }

    @Test
    void givenIdNotInDB_whenRetrieveByTicketSalesLimitId_throwResourceNotFoundException() {

        // given
        given(ticketSalesLimitRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testTicketSalesLimitService.getTicketSalesLimitById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("ticket sales limit with id %d does not exist.", 1L);
    }

    @Test
    void givenTicketSalesLimitExist_whenRetrieve_thenSuccessful() {

        // given

        TicketSalesLimit ticketSalesLimit = new TicketSalesLimit();
        ticketSalesLimit.setId(1L);
        ticketSalesLimit.setLimitVacancy(0);
        ticketSalesLimit.setOccupiedVacancy(0);

        given(ticketSalesLimitRepository.findById(ticketSalesLimit.getId()))
                .willReturn(Optional.of(ticketSalesLimit));

        // when
        TicketSalesLimit retrievedTicketSalesLimit = testTicketSalesLimitService
                .getTicketSalesLimitById(ticketSalesLimit.getId());
        // then
        assertThat(retrievedTicketSalesLimit).isEqualTo(ticketSalesLimit);

    }

    @Test
    void givenNullId_whenUpdate_throwRequestValidationException() {
        // given

        TicketSalesLimit ticketSalesLimit = new TicketSalesLimit();
        ticketSalesLimit.setLimitVacancy(0);
        ticketSalesLimit.setOccupiedVacancy(0);

        // when
        // then
        assertThatThrownBy(() -> testTicketSalesLimitService.updateTicketSalesLimit(ticketSalesLimit))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("ticket sales limit id cannot be null.");

    }

    @Test
    void givenIdNotInDB_whenUpdate_throwResourceNotFoundException() {
        // given

        TicketSalesLimit ticketSalesLimit = new TicketSalesLimit();
        ticketSalesLimit.setId(1L);
        ticketSalesLimit.setLimitVacancy(0);
        ticketSalesLimit.setOccupiedVacancy(0);

        given(ticketSalesLimitRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testTicketSalesLimitService.updateTicketSalesLimit(ticketSalesLimit))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("ticket sales limit with id %d does not exist.", 1L);

    }

    @Test
    void getAllTicketSalesLimits() {
        testTicketSalesLimitService.getAllTicketSalesLimits();
        verify(ticketSalesLimitRepository).findAll();
    }
}