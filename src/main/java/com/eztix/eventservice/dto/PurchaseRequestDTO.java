package com.eztix.eventservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseRequestDTO {

    private Long eventId;

    //private String customerId;

    private List<PurchaseRequestItemDTO> purchaseRequestItems;
}
