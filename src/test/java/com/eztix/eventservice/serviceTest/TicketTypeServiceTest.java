package com.eztix.eventservice.serviceTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.eztix.eventservice.model.TicketType;
import com.eztix.eventservice.repository.TicketTypeRepository;
import com.eztix.eventservice.service.TicketTypeService;

public class TicketTypeServiceTest {

    @Mock
    private TicketTypeRepository ticketTypeRepository;

    private TicketTypeService ticketTypeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.ticketTypeService = new TicketTypeService(this.ticketTypeRepository);
    }

    @Test
    public void testAddTicketType() {

        // Arrange
        TicketType mockTicketType = new TicketType();

        // Act
        TicketType addedTicketType = ticketTypeService.addNewTicketType(mockTicketType);
        verify(ticketTypeRepository).save(mockTicketType);

        // Assert
        assertNotNull(addedTicketType.getId());
    }

    @Test
    public void testGetTicketTypeById() {

        // Arrange
        TicketType mockTicketType = new TicketType();

        // Mock behaviour
        when(ticketTypeRepository.findById(1L)).thenReturn(Optional.of(mockTicketType));

        // Act
        TicketType result = ticketTypeService.getTicketTypeById(1L);

        // Assert
        verify(ticketTypeRepository).findById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId().longValue());
    }

    @Test
    public void testGetAllTicketType() {

        // Arrange
        TicketType mockTicketType = new TicketType();
        ticketTypeRepository.save(mockTicketType);

        // Act
        Iterable<TicketType> mockAllTicketType = ticketTypeService.getAllTicketTypes();

        // Assert
        verify(ticketTypeRepository).findAll();

        // Check iterable
        List<TicketType> ticketTypeList = StreamSupport
                .stream(mockAllTicketType.spliterator(), false)
                .collect(Collectors.toList());

        assertEquals(1, ticketTypeList.size());
        assertEquals(1L, ticketTypeList.get(0).getId().longValue());
    }

    @Test
    public void testUpdateTicketType() {

        // Arrange
        TicketType mockTicketType = new TicketType();
        mockTicketType.setDescription("test description");

        // Mock behaviour
        ArgumentCaptor<TicketType> mockArgumentCaptor = ArgumentCaptor.forClass(TicketType.class);
        doAnswer((Answer<TicketType>) invocation -> {
            TicketType savedTicketType = mockArgumentCaptor.getValue();
            TicketType argTicketType = invocation.getArgument(0);
            savedTicketType.setDescription(argTicketType.getDescription());
            return savedTicketType;
        }).when(ticketTypeRepository).save(mockArgumentCaptor.capture());

        // Act
        ticketTypeService.updateTicketType(mockTicketType);

        // Assert
        verify(ticketTypeRepository).save(mockTicketType);
        TicketType savedTicketType = mockArgumentCaptor.getValue();

        assertEquals("test description", savedTicketType.getDescription());
    }

}
