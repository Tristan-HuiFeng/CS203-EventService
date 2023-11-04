package com.eztix.eventservice.dto.prretrieval;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class PurchaseRequestItemRetrivalDTO {

    private Long id;

    private Double price;

    private String ticketType;

    private Integer quantityRequested;

    private OffsetDateTime eventStartTime;

    private OffsetDateTime eventEndTime;
}
