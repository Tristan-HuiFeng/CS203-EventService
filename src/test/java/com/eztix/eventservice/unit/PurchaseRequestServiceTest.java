package com.eztix.eventservice.unit;

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.PurchaseRequest;
import com.eztix.eventservice.repository.PurchaseRequestRepository;
import com.eztix.eventservice.service.PurchaseRequestService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PurchaseRequestServiceTest {

    @Mock
    private PurchaseRequestRepository purchaseRequestRepository;
    @InjectMocks
    private PurchaseRequestService testPurchaseRequestService;

    @Test
    void givenIdNotInDB_whenRetrieveByPurchaseRequestId_throwResourceNotFoundException() {

        // given
        given(purchaseRequestRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testPurchaseRequestService.getPurchaseRequestById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("purchase request with id %d does not exist.", 1L);
    }

    @Test
    void givenPurchaseRequestExist_whenRetrieve_thenSuccessful() {

        // given
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setCustomerId("test customer");
        purchaseRequest.setQueueNumber(1L);
        purchaseRequest.setStatus("test status");

        given(purchaseRequestRepository.findById(purchaseRequest.getId()))
                .willReturn(Optional.of(purchaseRequest));

        // when
        PurchaseRequest retrievedPurchaseRequest = testPurchaseRequestService
                .getPurchaseRequestById(purchaseRequest.getId());
        // then
        assertThat(retrievedPurchaseRequest).isEqualTo(purchaseRequest);

    }

    @Test
    void givenNullId_whenUpdate_throwRequestValidationException() {
        // given
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setCustomerId("test customer");
        purchaseRequest.setQueueNumber(1L);
        purchaseRequest.setStatus("test status");

        // when
        // then
        assertThatThrownBy(() -> testPurchaseRequestService.updatePurchaseRequest(purchaseRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("purchase request id cannot be null.");

    }

    @Test
    void givenIdNotInDB_whenUpdate_throwResourceNotFoundException() {
        // given
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setId(1L);
        purchaseRequest.setCustomerId("test customer");
        purchaseRequest.setQueueNumber(1L);
        purchaseRequest.setStatus("test status");

        given(purchaseRequestRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testPurchaseRequestService.updatePurchaseRequest(purchaseRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("purchase request with id %d does not exist.", 1L);

    }

}