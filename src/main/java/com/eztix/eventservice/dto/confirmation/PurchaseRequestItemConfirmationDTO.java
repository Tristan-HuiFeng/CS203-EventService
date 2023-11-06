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
public class PurchaseRequestItemConfirmationDTO {

    Long id;

    int quantityRequested;

    String ticketType;

    double price;

    OffsetDateTime eventStartDateTime;

    OffsetDateTime eventEndDateTime;
}
