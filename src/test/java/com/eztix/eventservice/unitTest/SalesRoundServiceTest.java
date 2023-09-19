package com.eztix.eventservice.serviceTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.SalesRound;
import com.eztix.eventservice.repository.SalesRoundRepository;
import com.eztix.eventservice.service.SalesRoundService;

@ExtendWith(MockitoExtension.class)
public class SalesRoundServiceTest {

    @Mock
    private SalesRoundRepository salesRoundRepository;
    @InjectMocks
    private SalesRoundService testSalesRoundService;


    @Test
    void givenNewSalesRound_whenAddSalesRound_thenSuccess() {
        // given
        OffsetDateTime testOffsetDateTime = OffsetDateTime.now(ZoneOffset.ofHours(8));
        SalesRound salesRound = new SalesRound();
        salesRound.setRound_start(testOffsetDateTime);
        salesRound.setRound_end(testOffsetDateTime);
        salesRound.setPurchase_start(testOffsetDateTime);
        salesRound.setPurchase_end(testOffsetDateTime);
        salesRound.setSales_type("test sales type");

        // when
        testSalesRoundService.addNewSalesRound(salesRound);

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
                .hasMessageContaining("sales round with id %d not found", 1L);

    }

    @Test
    void givenSalesRoundExist_whenRetrieve_thenSuccessful() {

        // given
        OffsetDateTime testOffsetDateTime = OffsetDateTime.now(ZoneOffset.ofHours(8));
        SalesRound salesRound = new SalesRound();
        salesRound.setRound_start(testOffsetDateTime);
        salesRound.setRound_end(testOffsetDateTime);
        salesRound.setPurchase_start(testOffsetDateTime);
        salesRound.setPurchase_end(testOffsetDateTime);
        salesRound.setSales_type("test sales round type");

        given(salesRoundRepository.findById(salesRound.getId())).willReturn(Optional.of(salesRound));

        // when
        SalesRound retrievedSalesRound = testSalesRoundService.getSalesRoundById(salesRound.getId());

        // then
        assertThat(retrievedSalesRound).isEqualTo(salesRound);

    }

    @Test
    void givenNullId_whenUpdate_throwRequestValidationException() {
        // given
        OffsetDateTime testOffsetDateTime = OffsetDateTime.now(ZoneOffset.ofHours(8));
        SalesRound salesRound = new SalesRound();
        salesRound.setRound_start(testOffsetDateTime);
        salesRound.setRound_end(testOffsetDateTime);
        salesRound.setPurchase_start(testOffsetDateTime);
        salesRound.setPurchase_end(testOffsetDateTime);
        salesRound.setSales_type("test sales round type");
        // when
        // then
        assertThatThrownBy(() -> testSalesRoundService.updateSalesRound(salesRound))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("sales round id cannot be null");

    }

    @Test
    void givenIdNotInDB_whenUpdate_throwRequestNotFoundException() {
        // given
        OffsetDateTime testOffsetDateTime = OffsetDateTime.now(ZoneOffset.ofHours(8));
        SalesRound salesRound = new SalesRound();
        salesRound.setId(1L);
        salesRound.setRound_start(testOffsetDateTime);
        salesRound.setRound_end(testOffsetDateTime);
        salesRound.setPurchase_start(testOffsetDateTime);
        salesRound.setPurchase_end(testOffsetDateTime);
        salesRound.setSales_type("test sales type");
        // when
        // then
        assertThatThrownBy(() -> testSalesRoundService.updateSalesRound(salesRound))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("sales round with id %s not found", salesRound.getId()));

    }

    @Test
    void getAllSalesRounds() {
        testSalesRoundService.getAllSalesRounds();
        verify(salesRoundRepository).findAll();
    }    


}
