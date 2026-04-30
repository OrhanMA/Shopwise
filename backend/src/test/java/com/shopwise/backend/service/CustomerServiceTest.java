package com.shopwise.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.shopwise.backend.api.dto.CustomerRequest;
import com.shopwise.backend.domain.Customer;
import com.shopwise.backend.domain.Shop;
import com.shopwise.backend.repository.CustomerRepository;
import com.shopwise.backend.repository.ShopRepository;
import com.shopwise.backend.testutil.TestEntityUtils;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ShopRepository shopRepository;

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService(customerRepository, shopRepository);
    }

    @Test
    void createCustomerShouldNormalizeEmailBeforeSaving() {
        Shop shop = new Shop();
        TestEntityUtils.setField(shop, "id", 1L);
        when(customerRepository.findByShopIdAndEmailIgnoreCase(1L, "alice@example.com"))
                .thenReturn(Optional.empty());
        when(shopRepository.findById(1L)).thenReturn(Optional.of(shop));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer customer = invocation.getArgument(0);
            TestEntityUtils.setField(customer, "id", 99L);
            return customer;
        });

        Customer result = customerService.createCustomer(new CustomerRequest(
                " Alice ",
                " Martin ",
                " Alice@Example.com ",
                "+33600000000"
        ));

        assertThat(result.getEmail()).isEqualTo("alice@example.com");
        assertThat(result.getFirstName()).isEqualTo("Alice");
        assertThat(result.getLastName()).isEqualTo("Martin");
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void createCustomerShouldRejectDuplicateEmail() {
        Customer existing = new Customer();
        TestEntityUtils.setField(existing, "id", 3L);
        when(customerRepository.findByShopIdAndEmailIgnoreCase(1L, "alice@example.com"))
                .thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> customerService.createCustomer(new CustomerRequest(
                "Alice",
                "Martin",
                "alice@example.com",
                "+33600000000"
        )))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Un client existe déjà avec cet email.");
    }

    @Test
    void updateCustomerShouldRejectExistingEmailOwnedByAnotherCustomer() {
        Shop shop = new Shop();
        TestEntityUtils.setField(shop, "id", 1L);
        Customer current = new Customer();
        TestEntityUtils.setField(current, "id", 10L);
        current.setShop(shop);
        Customer existing = new Customer();
        TestEntityUtils.setField(existing, "id", 20L);
        existing.setShop(shop);

        when(customerRepository.findByIdAndShopId(10L, 1L)).thenReturn(Optional.of(current));
        when(customerRepository.findByShopIdAndEmailIgnoreCase(1L, "alice@example.com"))
                .thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> customerService.updateCustomer(10L, new CustomerRequest(
                "Alice", "Martin", "alice@example.com", "+33600000000"
        )))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Un autre client existe déjà avec cet email.");
    }

    @Test
    void getCustomerShouldThrowWhenMissing() {
        when(customerRepository.findByIdAndShopId(999L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.getCustomer(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Client introuvable.");
    }
}
