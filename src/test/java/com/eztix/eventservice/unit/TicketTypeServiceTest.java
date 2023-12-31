package com.eztix.eventservice.unit;

import com.eztix.eventservice.model.Activity;
import com.eztix.eventservice.service.ActivityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.TicketType;
import com.eztix.eventservice.repository.TicketTypeRepository;
import com.eztix.eventservice.service.TicketTypeService;

@ExtendWith(MockitoExtension.class)
public class TicketTypeServiceTest {

    @Mock
    private TicketTypeRepository ticketTypeRepository;
    @InjectMocks
    private TicketTypeService testTicketTypeService;
    @Mock
    private ActivityService activityService;

    @Test
    void givenNewTicketType_whenAddTicketType_thenSuccess() {
        // given
        TicketType ticketType = new TicketType();
        ticketType.setOccupiedCount(0);
        ticketType.setPrice(0);
        ticketType.setReservedCount(0);
        ticketType.setTotalVacancy(0);
        ticketType.setType("test type");

        // when
        testTicketTypeService.addNewTicketType(new Activity(), ticketType);

        // then
        ArgumentCaptor<TicketType> ticketTypeArgumentCaptor =
                ArgumentCaptor.forClass(TicketType.class);

        verify(ticketTypeRepository).save(ticketTypeArgumentCaptor.capture());

        TicketType capturedTicketType = ticketTypeArgumentCaptor.getValue();

        assertThat(capturedTicketType).isEqualTo(ticketType);
    }

    @Test
    void givenTicketTypeIdNotInDB_whenRetrieveTicketTypeById_throwResourceNotFoundException() {

        // given
        given(ticketTypeRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testTicketTypeService.getTicketTypeById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("ticket type with id %d does not exist.", 1L);

    }

    @Test
    void givenTicketTypeExist_whenRetrieve_thenSuccessful() {

        // given
        TicketType ticketType = new TicketType();
        ticketType.setId(1L);
        ticketType.setOccupiedCount(0);
        ticketType.setPrice(0);
        ticketType.setReservedCount(0);
        ticketType.setTotalVacancy(0);
        ticketType.setType("test type");

        given(ticketTypeRepository.findById(ticketType.getId())).willReturn(Optional.of(ticketType));

        // when
        TicketType retrievedTicketType = testTicketTypeService.getTicketTypeById(ticketType.getId());

        // then
        assertThat(retrievedTicketType).isEqualTo(ticketType);

    }

    @Test
    void givenNullId_whenUpdate_throwRequestValidationException() {
        // given
        TicketType ticketType = new TicketType();
        ticketType.setOccupiedCount(0);
        ticketType.setPrice(0);
        ticketType.setReservedCount(0);
        ticketType.setTotalVacancy(0);
        ticketType.setType("test type");
        // when
        // then
        assertThatThrownBy(() -> testTicketTypeService.updateTicketType(ticketType))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("ticket type id cannot be null.");

    }

    @Test
    void givenIdNotInDB_whenUpdate_throwResourceNotFoundException() {
        // given
        TicketType ticketType = new TicketType();
        ticketType.setId(1L);
        ticketType.setOccupiedCount(0);
        ticketType.setPrice(0);
        ticketType.setTotalVacancy(0);
        ticketType.setType("test type");

        given(ticketTypeRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testTicketTypeService.updateTicketType(ticketType))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("ticket type with id %s does not exist.", ticketType.getId()));

    }

    @Test
    void getAllTicketTypesByActivityId() {
        TicketType ticketType = new TicketType();
        ticketType.setId(1L);
        ticketType.setOccupiedCount(0);
        ticketType.setPrice(0);
        ticketType.setTotalVacancy(0);
        ticketType.setType("test type");

        List<TicketType> ticketTypeList = new ArrayList<>();
        ticketTypeList.add(ticketType);

        given(ticketTypeRepository.findByActivityId(1L)).willReturn(Optional.of(ticketTypeList));

        testTicketTypeService.getTicketTypeByActivityId(1L);
        verify(ticketTypeRepository).findByActivityId(1L);
    }    

    @Test
    void deleteAllTicketTypes() {
        testTicketTypeService.deleteAllTicketTypes();
        verify(ticketTypeRepository).deleteAll();
    }

}
