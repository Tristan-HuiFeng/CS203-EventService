package com.eztix.eventservice.dto.confirmation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesRoundConfirmationDTO {

    OffsetDateTime roundStart;

    OffsetDateTime roundEnd;

    String salesType;

}
