package com.shopwise.backend.repository;

import com.shopwise.backend.domain.Appointment;
import com.shopwise.backend.domain.AppointmentStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {
    @Override
    @EntityGraph(attributePaths = {"customer", "service", "shop"})
    List<Appointment> findAll(Specification<Appointment> spec);

    @EntityGraph(attributePaths = {"customer", "service", "shop"})
    Optional<Appointment> findByIdAndShopId(Long id, Long shopId);

    List<Appointment> findByCustomerIdOrderByAppointmentDateDescAppointmentTimeDesc(Long customerId);
    boolean existsByIdAndStatus(Long id, AppointmentStatus status);
}
