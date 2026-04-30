package com.shopwise.backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AppointmentServiceIntegrationTest {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void cleanUp() {
        jdbcTemplate.update("DELETE FROM loyalty_transactions");
        jdbcTemplate.update("DELETE FROM appointments");
        jdbcTemplate.update("DELETE FROM services");
        jdbcTemplate.update("DELETE FROM customers");
        jdbcTemplate.update("DELETE FROM shops");
    }

    @Test
    void listAppointmentsShouldApplyAllFilters() {
        seedFixtures();

        var appointments = appointmentService.listAppointments(LocalDate.of(2026, 5, 2), com.shopwise.backend.domain.AppointmentStatus.SCHEDULED, 1L);

        assertThat(appointments).hasSize(1);
        assertThat(appointments.getFirst().getCustomer().getId()).isEqualTo(1L);
    }

    @Test
    void listAppointmentsShouldReturnAllAppointmentsWhenNoFilterIsProvided() {
        seedFixtures();

        List<?> appointments = appointmentService.listAppointments(null, null, null);

        assertThat(appointments).hasSize(2);
    }

    private void seedFixtures() {
        jdbcTemplate.update("INSERT INTO shops (id, name, email, phone) VALUES (1, 'Chez Marie', 'contact@example.com', '+33102030405')");
        jdbcTemplate.update("INSERT INTO customers (id, shop_id, first_name, last_name, email, phone, loyalty_points) VALUES (1, 1, 'Alice', 'Martin', 'alice@example.com', '+33600000000', 0)");
        jdbcTemplate.update("INSERT INTO customers (id, shop_id, first_name, last_name, email, phone, loyalty_points) VALUES (2, 1, 'Karim', 'Benali', 'karim@example.com', '+33611111111', 0)");
        jdbcTemplate.update("INSERT INTO services (id, shop_id, name, description, duration_minutes, points_reward, active) VALUES (1, 1, 'Conseil produit', 'Conseil', 30, 100, TRUE)");
        jdbcTemplate.update("INSERT INTO appointments (id, shop_id, customer_id, service_id, appointment_date, appointment_time, status, notes) VALUES (1, 1, 1, 1, ?, ?, 'SCHEDULED', 'Note 1')", Date.valueOf(LocalDate.of(2026, 5, 2)), Time.valueOf("10:00:00"));
        jdbcTemplate.update("INSERT INTO appointments (id, shop_id, customer_id, service_id, appointment_date, appointment_time, status, notes) VALUES (2, 1, 2, 1, ?, ?, 'CANCELLED', 'Note 2')", Date.valueOf(LocalDate.of(2026, 5, 3)), Time.valueOf("11:00:00"));
    }
}
