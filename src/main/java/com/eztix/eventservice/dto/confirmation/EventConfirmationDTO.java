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
public class EventConfirmationDTO {

    Long id;

    String name;

    String description;

    String bannerURL;

    String location;

    OffsetDateTime endDateTime;

    OffsetDateTime startDateTime;

    SalesRoundConfirmationDTO salesRound;

    PurchaseRequestConfirmationDTO purchaseRequest;

}
