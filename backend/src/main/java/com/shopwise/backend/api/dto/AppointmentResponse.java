package com.shopwise.backend.api.dto;

import com.shopwise.backend.domain.Appointment;
import com.shopwise.backend.domain.AppointmentStatus;
import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentResponse(
        Long id,
        CustomerResponse customer,
        ServiceResponse service,
        LocalDate appointmentDate,
        LocalTime appointmentTime,
        AppointmentStatus status,
        String notes
) {
    public static AppointmentResponse from(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                CustomerResponse.from(appointment.getCustomer()),
                ServiceResponse.from(appointment.getService()),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime(),
                appointment.getStatus(),
                appointment.getNotes()
        );
    }
}
