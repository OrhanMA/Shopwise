package com.shopwise.backend.repository;

import com.shopwise.backend.domain.Appointment;
import com.shopwise.backend.domain.AppointmentStatus;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {
    List<Appointment> findByCustomerIdOrderByAppointmentDateDescAppointmentTimeDesc(Long customerId);
    boolean existsByIdAndStatus(Long id, AppointmentStatus status);
}
