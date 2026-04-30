package com.shopwise.backend.repository;

import com.shopwise.backend.domain.ServiceOffering;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering, Long> {
    List<ServiceOffering> findByShopIdAndActiveTrueOrderByNameAsc(Long shopId);
    Optional<ServiceOffering> findByIdAndShopId(Long id, Long shopId);
}
