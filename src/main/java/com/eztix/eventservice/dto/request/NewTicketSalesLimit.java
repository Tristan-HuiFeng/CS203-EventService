package com.eztix.eventservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class NewTicketSalesLimit {

    @NotNull
    private int limitVacancy;

    @NotNull
    private long ticketTypeId;

}
