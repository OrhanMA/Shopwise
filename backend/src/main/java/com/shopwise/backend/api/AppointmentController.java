package com.shopwise.backend.api;

import com.shopwise.backend.api.dto.AppointmentRequest;
import com.shopwise.backend.api.dto.AppointmentResponse;
import com.shopwise.backend.domain.AppointmentStatus;
import com.shopwise.backend.service.AppointmentService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public List<AppointmentResponse> listAppointments(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) Long customerId
    ) {
        return appointmentService.listAppointments(date, status, customerId).stream()
                .map(AppointmentResponse::from)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse createAppointment(@Valid @RequestBody AppointmentRequest request) {
        return AppointmentResponse.from(appointmentService.createAppointment(request));
    }

    @PostMapping("/{id}/honor")
    public AppointmentResponse honorAppointment(@PathVariable long id) {
        return AppointmentResponse.from(appointmentService.honorAppointment(id));
    }

    @PostMapping("/{id}/cancel")
    public AppointmentResponse cancelAppointment(@PathVariable long id) {
        return AppointmentResponse.from(appointmentService.cancelAppointment(id));
    }
}
