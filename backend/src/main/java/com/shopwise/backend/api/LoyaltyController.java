package com.shopwise.backend.api;

import com.shopwise.backend.api.dto.LoyaltySummaryResponse;
import com.shopwise.backend.service.LoyaltyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers/{customerId}/loyalty")
public class LoyaltyController {
    private final LoyaltyService loyaltyService;

    public LoyaltyController(LoyaltyService loyaltyService) {
        this.loyaltyService = loyaltyService;
    }

    @GetMapping
    public LoyaltySummaryResponse getSummary(@PathVariable long customerId) {
        return loyaltyService.getSummary(customerId);
    }
}
