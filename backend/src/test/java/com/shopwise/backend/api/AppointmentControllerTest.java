package com.shopwise.backend.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopwise.backend.api.dto.AppointmentRequest;
import com.shopwise.backend.domain.Appointment;
import com.shopwise.backend.domain.AppointmentStatus;
import com.shopwise.backend.domain.Customer;
import com.shopwise.backend.domain.ServiceOffering;
import com.shopwise.backend.domain.Shop;
import com.shopwise.backend.service.AppointmentService;
import com.shopwise.backend.testutil.TestEntityUtils;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AppointmentService appointmentService;

    @Test
    void listAppointmentsShouldFilterAndReturnPayload() throws Exception {
        when(appointmentService.listAppointments(LocalDate.of(2026, 4, 30), AppointmentStatus.SCHEDULED, 1L))
                .thenReturn(List.of(createAppointment(7L, AppointmentStatus.SCHEDULED)));

        mockMvc.perform(get("/api/appointments")
                        .param("date", "2026-04-30")
                        .param("status", "SCHEDULED")
                        .param("customerId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(7))
                .andExpect(jsonPath("$[0].status").value("SCHEDULED"))
                .andExpect(jsonPath("$[0].customer.id").value(1));
    }

    @Test
    void createAppointmentShouldReturnCreated() throws Exception {
        when(appointmentService.createAppointment(any())).thenReturn(createAppointment(8L, AppointmentStatus.SCHEDULED));

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AppointmentRequest(
                                1L, 3L, LocalDate.of(2026, 5, 1), LocalTime.of(10, 30), "Note"
                        ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(8))
                .andExpect(jsonPath("$.service.id").value(3));
    }

    @Test
    void honorAppointmentShouldReturnUpdatedStatus() throws Exception {
        when(appointmentService.honorAppointment(7L)).thenReturn(createAppointment(7L, AppointmentStatus.HONORED));

        mockMvc.perform(post("/api/appointments/7/honor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("HONORED"));
    }

    @Test
    void cancelAppointmentShouldReturnUpdatedStatus() throws Exception {
        when(appointmentService.cancelAppointment(7L)).thenReturn(createAppointment(7L, AppointmentStatus.CANCELLED));

        mockMvc.perform(post("/api/appointments/7/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    private Appointment createAppointment(long id, AppointmentStatus status) {
        Shop shop = new Shop();
        TestEntityUtils.setField(shop, "id", 1L);

        Customer customer = new Customer();
        TestEntityUtils.setField(customer, "id", 1L);
        customer.setShop(shop);
        customer.setFirstName("Alice");
        customer.setLastName("Martin");
        customer.setEmail("alice@example.com");
        customer.setPhone("+33600000000");

        ServiceOffering service = new ServiceOffering();
        TestEntityUtils.setField(service, "id", 3L);
        TestEntityUtils.setField(service, "shop", shop);
        TestEntityUtils.setField(service, "name", "Conseil produit");
        TestEntityUtils.setField(service, "description", "Conseil personnalisé");
        TestEntityUtils.setField(service, "durationMinutes", 30);
        TestEntityUtils.setField(service, "pointsReward", 100);

        Appointment appointment = new Appointment();
        TestEntityUtils.setField(appointment, "id", id);
        appointment.setShop(shop);
        appointment.setCustomer(customer);
        appointment.setService(service);
        appointment.setAppointmentDate(LocalDate.of(2026, 4, 30));
        appointment.setAppointmentTime(LocalTime.of(10, 30));
        appointment.setNotes("Note");
        if (status == AppointmentStatus.HONORED) {
            appointment.honor();
        } else if (status == AppointmentStatus.CANCELLED) {
            appointment.cancel();
        }
        return appointment;
    }
}
