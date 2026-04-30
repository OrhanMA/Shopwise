package com.shopwise.backend.service;

import com.shopwise.backend.api.dto.CustomerRequest;
import com.shopwise.backend.domain.Customer;
import com.shopwise.backend.domain.Shop;
import com.shopwise.backend.repository.CustomerRepository;
import com.shopwise.backend.repository.ShopRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {
    public static final long DEMO_SHOP_ID = 1L;

    private final CustomerRepository customerRepository;
    private final ShopRepository shopRepository;

    public CustomerService(CustomerRepository customerRepository, ShopRepository shopRepository) {
        this.customerRepository = customerRepository;
        this.shopRepository = shopRepository;
    }

    public List<Customer> listCustomers() {
        return customerRepository.findByShopIdOrderByLastNameAscFirstNameAsc(DEMO_SHOP_ID);
    }

    public Customer getCustomer(long id) {
        return customerRepository.findByIdAndShopId(id, DEMO_SHOP_ID)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable."));
    }

    public Customer findByEmail(String email) {
        return customerRepository.findByShopIdAndEmailIgnoreCase(DEMO_SHOP_ID, email.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Aucun client ne correspond à cet email."));
    }

    @Transactional
    public Customer createCustomer(CustomerRequest request) {
        String normalizedEmail = request.email().trim().toLowerCase();
        customerRepository.findByShopIdAndEmailIgnoreCase(DEMO_SHOP_ID, normalizedEmail)
                .ifPresent(customer -> {
                    throw new BusinessRuleException("Un client existe déjà avec cet email.");
                });
        Shop shop = shopRepository.findById(DEMO_SHOP_ID)
                .orElseThrow(() -> new ResourceNotFoundException("Commerce de démonstration introuvable."));
        Customer customer = new Customer();
        customer.setShop(shop);
        applyRequest(customer, request);
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomer(long id, CustomerRequest request) {
        Customer customer = getCustomer(id);
        String normalizedEmail = request.email().trim().toLowerCase();
        customerRepository.findByShopIdAndEmailIgnoreCase(DEMO_SHOP_ID, normalizedEmail)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new BusinessRuleException("Un autre client existe déjà avec cet email.");
                });
        applyRequest(customer, request);
        return customer;
    }

    private void applyRequest(Customer customer, CustomerRequest request) {
        customer.setFirstName(request.firstName().trim());
        customer.setLastName(request.lastName().trim());
        customer.setEmail(request.email().trim().toLowerCase());
        customer.setPhone(request.phone());
    }
}
