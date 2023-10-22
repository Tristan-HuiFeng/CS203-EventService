package com.eztix.eventservice.dto.request;

import com.eztix.eventservice.model.TicketType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class NewActivity {
    @NotNull
    private OffsetDateTime startDateTime;

    @NotNull
    private OffsetDateTime  endDateTime;

    private List<NewTicketType> ticketTypes;
}
