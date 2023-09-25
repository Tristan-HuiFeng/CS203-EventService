package com.eztix.eventservice.dto;

import lombok.Data;

@Data
public class PurchaseRequestItemDTO {

    private Long ticketTypeId;

    private Integer quantityRequested;

}
