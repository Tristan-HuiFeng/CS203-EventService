package com.eztix.eventservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseRequestDTO {

    private Long salesRoundId;

    //private String customerId;

    private List<PurchaseRequestItemDTO> purchaseRequestItems;
}
