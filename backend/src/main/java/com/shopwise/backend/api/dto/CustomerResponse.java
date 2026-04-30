package com.shopwise.backend.api.dto;

import com.shopwise.backend.domain.Customer;

public record CustomerResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        int loyaltyPoints
) {
    public static CustomerResponse from(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getLoyaltyPoints()
        );
    }
}
