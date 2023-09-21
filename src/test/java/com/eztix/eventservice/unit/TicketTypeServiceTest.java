package com.eztix.eventservice.serviceTest;

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


    @Test
    void givenNewTicketType_whenAddTicketType_thenSuccess() {
        // given
        TicketType ticketType = new TicketType();
        ticketType.setDescription("test description");
        ticketType.setOccupied_count(0);
        ticketType.setPrice(0);
        ticketType.setReserved_count(0);
        ticketType.setTotal_vacancy(0);
        ticketType.setType("test type");

        // when
        testTicketTypeService.addNewTicketType(ticketType);

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
        ticketType.setDescription("test description");
        ticketType.setOccupied_count(0);
        ticketType.setPrice(0);
        ticketType.setReserved_count(0);
        ticketType.setTotal_vacancy(0);
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
        ticketType.setDescription("test description");
        ticketType.setOccupied_count(0);
        ticketType.setPrice(0);
        ticketType.setReserved_count(0);
        ticketType.setTotal_vacancy(0);
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
        ticketType.setDescription("test description");
        ticketType.setOccupied_count(0);
        ticketType.setPrice(0);
        ticketType.setReserved_count(0);
        ticketType.setTotal_vacancy(0);
        ticketType.setType("test type");

        given(ticketTypeRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testTicketTypeService.updateTicketType(ticketType))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("ticket type with id %s does not exist.", ticketType.getId()));

    }

    @Test
    void getAllTicketTypes() {
        testTicketTypeService.getAllTicketTypes();
        verify(ticketTypeRepository).findAll();
    }    


}
