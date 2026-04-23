package com.shopwise.backend.repository;

import com.shopwise.backend.domain.LoyaltyTransaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoyaltyTransactionRepository extends JpaRepository<LoyaltyTransaction, Long> {
    List<LoyaltyTransaction> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
}
