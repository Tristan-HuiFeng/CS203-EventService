package com.eztix.eventservice.dto.confirmation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventConfirmationDTO {
    String name;

    String description;

    String bannerURL;

    String location;

    SalesRoundConfirmationDTO salesRound;

    PurchaseRequestConfirmationDTO purchaseRequest;

}
