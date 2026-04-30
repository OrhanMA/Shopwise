package com.shopwise.backend.api.dto;

import com.shopwise.backend.domain.LoyaltyTransaction;
import com.shopwise.backend.domain.LoyaltyTransactionType;
import java.time.LocalDateTime;

public record LoyaltyTransactionResponse(
        Long id,
        int points,
        String reason,
        LoyaltyTransactionType transactionType,
        LocalDateTime createdAt
) {
    public static LoyaltyTransactionResponse from(LoyaltyTransaction transaction) {
        return new LoyaltyTransactionResponse(
                transaction.getId(),
                transaction.getPoints(),
                transaction.getReason(),
                transaction.getTransactionType(),
                transaction.getCreatedAt()
        );
    }
}
