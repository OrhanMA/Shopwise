package com.shopwise.backend.api.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentRequest(
        @NotNull Long customerId,
        @NotNull Long serviceId,
        @FutureOrPresent @NotNull LocalDate appointmentDate,
        @NotNull LocalTime appointmentTime,
        String notes
) {
}
