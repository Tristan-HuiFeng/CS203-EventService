package com.eztix.eventservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class NewSalesRound {
    @NotNull
    private OffsetDateTime roundStart;

    @NotNull
    private OffsetDateTime roundEnd;

    private OffsetDateTime purchaseStart;

    private OffsetDateTime purchaseEnd;

    @NotNull
    private String salesType;

    private List<NewTicketSalesLimit> ticketSalesLimitList;
}
