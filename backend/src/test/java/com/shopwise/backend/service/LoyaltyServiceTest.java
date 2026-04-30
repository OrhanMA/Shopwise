package com.shopwise.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.shopwise.backend.domain.Customer;
import com.shopwise.backend.domain.LoyaltyTransaction;
import com.shopwise.backend.domain.LoyaltyTransactionType;
import com.shopwise.backend.domain.Shop;
import com.shopwise.backend.repository.LoyaltyTransactionRepository;
import com.shopwise.backend.testutil.TestEntityUtils;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoyaltyServiceTest {

    @Mock
    private CustomerService customerService;
    @Mock
    private LoyaltyTransactionRepository loyaltyTransactionRepository;

    private LoyaltyService loyaltyService;

    @BeforeEach
    void setUp() {
        loyaltyService = new LoyaltyService(customerService, loyaltyTransactionRepository);
    }

    @Test
    void getSummaryShouldExposeBalanceAndSortedTransactions() {
        Shop shop = new Shop();
        TestEntityUtils.setField(shop, "id", 1L);
        Customer customer = new Customer();
        TestEntityUtils.setField(customer, "id", 1L);
        customer.setShop(shop);
        customer.setFirstName("Alice");
        customer.setLastName("Martin");
        customer.setEmail("alice@example.com");
        customer.setPhone("+33600000000");
        customer.addLoyaltyPoints(200);

        LoyaltyTransaction transaction = new LoyaltyTransaction(shop, customer, null, 100, "Rendez-vous honoré", LoyaltyTransactionType.EARNED);
        TestEntityUtils.setField(transaction, "id", 99L);
        TestEntityUtils.setField(transaction, "createdAt", LocalDateTime.of(2026, 4, 30, 9, 0));

        when(customerService.getCustomer(1L)).thenReturn(customer);
        when(loyaltyTransactionRepository.findByCustomerIdOrderByCreatedAtDesc(1L)).thenReturn(List.of(transaction));

        var summary = loyaltyService.getSummary(1L);

        assertThat(summary.balance()).isEqualTo(200);
        assertThat(summary.customer().email()).isEqualTo("alice@example.com");
        assertThat(summary.transactions()).hasSize(1);
        assertThat(summary.transactions().getFirst().reason()).isEqualTo("Rendez-vous honoré");
    }
}
