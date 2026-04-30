package com.shopwise.backend.api.dto;

import java.util.List;

public record LoyaltySummaryResponse(
        CustomerResponse customer,
        int balance,
        List<LoyaltyTransactionResponse> transactions
) {
}
