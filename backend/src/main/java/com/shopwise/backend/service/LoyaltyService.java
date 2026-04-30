package com.shopwise.backend.service;

import com.shopwise.backend.api.dto.CustomerResponse;
import com.shopwise.backend.api.dto.LoyaltySummaryResponse;
import com.shopwise.backend.api.dto.LoyaltyTransactionResponse;
import com.shopwise.backend.domain.Customer;
import com.shopwise.backend.repository.LoyaltyTransactionRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LoyaltyService {
    private final CustomerService customerService;
    private final LoyaltyTransactionRepository loyaltyTransactionRepository;

    public LoyaltyService(CustomerService customerService, LoyaltyTransactionRepository loyaltyTransactionRepository) {
        this.customerService = customerService;
        this.loyaltyTransactionRepository = loyaltyTransactionRepository;
    }

    public LoyaltySummaryResponse getSummary(long customerId) {
        Customer customer = customerService.getCustomer(customerId);
        List<LoyaltyTransactionResponse> transactions = loyaltyTransactionRepository
                .findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(LoyaltyTransactionResponse::from)
                .toList();
        return new LoyaltySummaryResponse(CustomerResponse.from(customer), customer.getLoyaltyPoints(), transactions);
    }
}
