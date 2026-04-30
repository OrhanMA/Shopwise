package com.shopwise.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.shopwise.backend.domain.Appointment;
import com.shopwise.backend.domain.AppointmentStatus;
import com.shopwise.backend.domain.Customer;
import com.shopwise.backend.domain.LoyaltyTransaction;
import com.shopwise.backend.domain.ServiceOffering;
import com.shopwise.backend.domain.Shop;
import com.shopwise.backend.repository.AppointmentRepository;
import com.shopwise.backend.repository.CustomerRepository;
import com.shopwise.backend.repository.LoyaltyTransactionRepository;
import com.shopwise.backend.repository.ServiceOfferingRepository;
import com.shopwise.backend.testutil.TestEntityUtils;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ServiceOfferingRepository serviceOfferingRepository;
    @Mock
    private LoyaltyTransactionRepository loyaltyTransactionRepository;

    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        appointmentService = new AppointmentService(
                appointmentRepository,
                customerRepository,
                serviceOfferingRepository,
                loyaltyTransactionRepository
        );
    }

    @Test
    void honorAppointmentShouldAwardPointsAndCreateTransaction() {
        Shop shop = createShop();
        Customer customer = createCustomer(shop);
        Appointment appointment = createAppointment(shop, customer, AppointmentStatus.SCHEDULED);
        when(appointmentRepository.findByIdAndShopId(appointment.getId(), CustomerService.DEMO_SHOP_ID))
                .thenReturn(Optional.of(appointment));

        Appointment result = appointmentService.honorAppointment(appointment.getId());

        assertThat(result.getStatus()).isEqualTo(AppointmentStatus.HONORED);
        assertThat(customer.getLoyaltyPoints()).isEqualTo(100);
        ArgumentCaptor<LoyaltyTransaction> captor = ArgumentCaptor.forClass(LoyaltyTransaction.class);
        verify(loyaltyTransactionRepository).save(captor.capture());
        assertThat(captor.getValue().getPoints()).isEqualTo(100);
        assertThat(captor.getValue().getReason()).isEqualTo("Rendez-vous honoré");
    }

    @Test
    void honorAppointmentShouldRejectCancelledAppointment() {
        Shop shop = createShop();
        Customer customer = createCustomer(shop);
        Appointment appointment = createAppointment(shop, customer, AppointmentStatus.CANCELLED);
        when(appointmentRepository.findByIdAndShopId(appointment.getId(), CustomerService.DEMO_SHOP_ID))
                .thenReturn(Optional.of(appointment));

        assertThatThrownBy(() -> appointmentService.honorAppointment(appointment.getId()))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Un rendez-vous annulé ne peut pas être honoré.");
        verify(loyaltyTransactionRepository, never()).save(any());
    }

    @Test
    void cancelAppointmentShouldRejectHonoredAppointment() {
        Shop shop = createShop();
        Customer customer = createCustomer(shop);
        Appointment appointment = createAppointment(shop, customer, AppointmentStatus.HONORED);
        when(appointmentRepository.findByIdAndShopId(appointment.getId(), CustomerService.DEMO_SHOP_ID))
                .thenReturn(Optional.of(appointment));

        assertThatThrownBy(() -> appointmentService.cancelAppointment(appointment.getId()))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Un rendez-vous honoré ne peut pas être annulé.");
    }

    @Test
    void honorAppointmentShouldReturnAlreadyHonoredAppointmentWithoutCreatingTransaction() {
        Shop shop = createShop();
        Customer customer = createCustomer(shop);
        Appointment appointment = createAppointment(shop, customer, AppointmentStatus.HONORED);
        when(appointmentRepository.findByIdAndShopId(appointment.getId(), CustomerService.DEMO_SHOP_ID))
                .thenReturn(Optional.of(appointment));

        Appointment result = appointmentService.honorAppointment(appointment.getId());

        assertThat(result.getStatus()).isEqualTo(AppointmentStatus.HONORED);
        verify(loyaltyTransactionRepository, never()).save(any());
    }

    @Test
    void cancelAppointmentShouldCancelScheduledAppointment() {
        Shop shop = createShop();
        Customer customer = createCustomer(shop);
        Appointment appointment = createAppointment(shop, customer, AppointmentStatus.SCHEDULED);
        when(appointmentRepository.findByIdAndShopId(appointment.getId(), CustomerService.DEMO_SHOP_ID))
                .thenReturn(Optional.of(appointment));

        Appointment result = appointmentService.cancelAppointment(appointment.getId());

        assertThat(result.getStatus()).isEqualTo(AppointmentStatus.CANCELLED);
    }

    @Test
    void createAppointmentShouldRejectUnknownCustomer() {
        when(customerRepository.findByIdAndShopId(99L, CustomerService.DEMO_SHOP_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> appointmentService.createAppointment(new com.shopwise.backend.api.dto.AppointmentRequest(
                99L, 5L, LocalDate.now(), LocalTime.NOON, "Note"
        )))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Client introuvable.");
    }

    private Shop createShop() {
        Shop shop = new Shop();
        TestEntityUtils.setField(shop, "id", 1L);
        return shop;
    }

    private Customer createCustomer(Shop shop) {
        Customer customer = new Customer();
        TestEntityUtils.setField(customer, "id", 12L);
        customer.setShop(shop);
        customer.setFirstName("Alice");
        customer.setLastName("Martin");
        customer.setEmail("alice@example.com");
        customer.setPhone("+33600000000");
        return customer;
    }

    private Appointment createAppointment(Shop shop, Customer customer, AppointmentStatus status) {
        ServiceOffering service = new ServiceOffering();
        TestEntityUtils.setField(service, "id", 5L);
        TestEntityUtils.setField(service, "shop", shop);
        TestEntityUtils.setField(service, "name", "Conseil produit");

        Appointment appointment = new Appointment();
        TestEntityUtils.setField(appointment, "id", 33L);
        appointment.setShop(shop);
        appointment.setCustomer(customer);
        appointment.setService(service);
        appointment.setAppointmentDate(LocalDate.now());
        appointment.setAppointmentTime(LocalTime.NOON);
        if (status == AppointmentStatus.HONORED) {
            appointment.honor();
        } else if (status == AppointmentStatus.CANCELLED) {
            appointment.cancel();
        }
        return appointment;
    }
}
