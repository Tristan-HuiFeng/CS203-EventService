package com.eztix.eventservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketTypeDTO {
    private OffsetDateTime startDateTime;
    private OffsetDateTime endDateTime;
    private String ticketType;
}
