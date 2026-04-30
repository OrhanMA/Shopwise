package com.shopwise.backend.repository;

import com.shopwise.backend.domain.Customer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByShopIdOrderByLastNameAscFirstNameAsc(Long shopId);
    Optional<Customer> findByIdAndShopId(Long id, Long shopId);
    Optional<Customer> findByShopIdAndEmailIgnoreCase(Long shopId, String email);
}
