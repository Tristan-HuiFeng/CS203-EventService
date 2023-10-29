package com.eztix.eventservice.dto.confirmation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseRequestConfirmationDTO {
    Long id;

    String status;

    List<PurchaseRequestItemConfirmationDTO> purchaseRequestItems;

}
