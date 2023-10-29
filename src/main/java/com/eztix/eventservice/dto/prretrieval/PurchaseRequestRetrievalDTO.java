package com.eztix.eventservice.dto.prretrieval;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class PurchaseRequestRetrievalDTO {

    String eventName;

    Long queueNumber;

    String status;

    String bannerURL;


}
