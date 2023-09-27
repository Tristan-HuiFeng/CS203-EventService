package com.eztix.eventservice.unit;

import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import com.eztix.eventservice.model.Activity;
import com.eztix.eventservice.model.Event;
import com.eztix.eventservice.model.PurchaseRequest;
import com.eztix.eventservice.model.PurchaseRequestItem;
import com.eztix.eventservice.model.SalesRound;
import com.eztix.eventservice.model.TicketType;
import com.eztix.eventservice.repository.PurchaseRequestItemRepository;
import com.eztix.eventservice.service.PurchaseRequestItemService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PurchaseRequestItemServiceTest {

    @Mock
    private PurchaseRequestItemRepository purchaseRequestItemRepository;
    @InjectMocks
    private PurchaseRequestItemService testPurchaseRequestItemService;

    @Test
    void givenNewPurchaseRequestItem_whenAddPurchaseRequestItem_thenSuccess() {
        // given
        PurchaseRequestItem purchaseRequestItem = new PurchaseRequestItem();
        purchaseRequestItem.setQuantityApproved(0);
        purchaseRequestItem.setQuantityRequested(0);

        // when
        testPurchaseRequestItemService.addNewPurchaseRequestItem(purchaseRequestItem);

        // then
        ArgumentCaptor<PurchaseRequestItem> eventArgumentCaptor = ArgumentCaptor.forClass(PurchaseRequestItem.class);

        verify(purchaseRequestItemRepository).save(eventArgumentCaptor.capture());

        PurchaseRequestItem capturedPurchaseRequestItem = eventArgumentCaptor.getValue();

        assertThat(capturedPurchaseRequestItem).isEqualTo(purchaseRequestItem);
    }

    @Test
    void givenIdNotInDB_whenRetrieveByPurchaseRequestItemId_throwResourceNotFoundException() {

        // given
        given(purchaseRequestItemRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testPurchaseRequestItemService.getPurchaseRequestItemById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("purchase request item with id %d does not exist.", 1L);
    }

    @Test
    void givenPurchaseRequestItemExist_whenRetrieve_thenSuccessful() {

        // given

        PurchaseRequestItem purchaseRequestItem = new PurchaseRequestItem();
        purchaseRequestItem.setId(1L);
        purchaseRequestItem.setQuantityApproved(0);
        purchaseRequestItem.setQuantityRequested(0);

        given(purchaseRequestItemRepository.findById(purchaseRequestItem.getId()))
                .willReturn(Optional.of(purchaseRequestItem));

        // when
        PurchaseRequestItem retrievedPurchaseRequestItem = testPurchaseRequestItemService
                .getPurchaseRequestItemById(purchaseRequestItem.getId());
        // then
        assertThat(retrievedPurchaseRequestItem).isEqualTo(purchaseRequestItem);

    }

    @Test
    void givenNullId_whenUpdate_throwRequestValidationException() {
        // given

        PurchaseRequestItem purchaseRequestItem = new PurchaseRequestItem();
        purchaseRequestItem.setQuantityRequested(0);

        // when
        // then
        assertThatThrownBy(() -> testPurchaseRequestItemService.updatePurchaseRequestItem(purchaseRequestItem))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("purchase request item id cannot be null.");

    }

    @Test
    void givenIdNotInDB_whenUpdate_throwResourceNotFoundException() {
        // given

        PurchaseRequestItem purchaseRequestItem = new PurchaseRequestItem();
        purchaseRequestItem.setId(1L);
        purchaseRequestItem.setQuantityApproved(0);
        purchaseRequestItem.setQuantityRequested(0);

        given(purchaseRequestItemRepository.findById(1L)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> testPurchaseRequestItemService.updatePurchaseRequestItem(purchaseRequestItem))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("purchase request item with id %d does not exist.", 1L);


    }

    @Test
    void getAllPurchaseRequests() {
        testPurchaseRequestItemService.getAllPurchaseRequestItems();
        verify(purchaseRequestItemRepository).findAll();
    }
}