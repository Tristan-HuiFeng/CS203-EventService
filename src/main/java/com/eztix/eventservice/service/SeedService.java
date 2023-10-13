package com.eztix.eventservice.service;

import com.eztix.eventservice.model.PurchaseRequest;
import com.eztix.eventservice.model.SalesRound;
import com.eztix.eventservice.repository.PurchaseRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeedService {
    private final PurchaseRequestRepository purchaseRequestRepository;
    private final SalesRoundService salesRoundService;

    public void createPRBySalesRound() {
        SalesRound sr = salesRoundService.getSalesRoundById(0L);

        for (int i = 0; i < 2001; i++) {
            PurchaseRequest pr = PurchaseRequest.builder()
                    .salesRound(sr)
                    .status("status")
                    .id((long) i)
                    .customerId(String.valueOf(i))
                    .build();
            purchaseRequestRepository.save(pr);
        }

    }
}
