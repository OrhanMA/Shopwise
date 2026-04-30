package com.shopwise.backend.service;

import com.shopwise.backend.api.dto.AppointmentRequest;
import com.shopwise.backend.domain.Appointment;
import com.shopwise.backend.domain.AppointmentStatus;
import com.shopwise.backend.domain.Customer;
import com.shopwise.backend.domain.LoyaltyTransaction;
import com.shopwise.backend.domain.LoyaltyTransactionType;
import com.shopwise.backend.domain.ServiceOffering;
import com.shopwise.backend.repository.AppointmentRepository;
import com.shopwise.backend.repository.CustomerRepository;
import com.shopwise.backend.repository.LoyaltyTransactionRepository;
import com.shopwise.backend.repository.ServiceOfferingRepository;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentService {
    private static final int HONORED_APPOINTMENT_POINTS = 100;

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final ServiceOfferingRepository serviceOfferingRepository;
    private final LoyaltyTransactionRepository loyaltyTransactionRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            CustomerRepository customerRepository,
            ServiceOfferingRepository serviceOfferingRepository,
            LoyaltyTransactionRepository loyaltyTransactionRepository
    ) {
        this.appointmentRepository = appointmentRepository;
        this.customerRepository = customerRepository;
        this.serviceOfferingRepository = serviceOfferingRepository;
        this.loyaltyTransactionRepository = loyaltyTransactionRepository;
    }

    public List<Appointment> listAppointments(LocalDate date, AppointmentStatus status, Long customerId) {
        return appointmentRepository.findAll(filterAppointments(date, status, customerId));
    }

    @Transactional
    public Appointment createAppointment(AppointmentRequest request) {
        Customer customer = customerRepository.findByIdAndShopId(request.customerId(), CustomerService.DEMO_SHOP_ID)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable."));
        ServiceOffering service = serviceOfferingRepository.findByIdAndShopId(request.serviceId(), CustomerService.DEMO_SHOP_ID)
                .orElseThrow(() -> new ResourceNotFoundException("Service introuvable."));

        Appointment appointment = new Appointment();
        appointment.setShop(customer.getShop());
        appointment.setCustomer(customer);
        appointment.setService(service);
        appointment.setAppointmentDate(request.appointmentDate());
        appointment.setAppointmentTime(request.appointmentTime());
        appointment.setNotes(request.notes());
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment honorAppointment(long id) {
        Appointment appointment = getAppointment(id);
        if (appointment.getStatus() == AppointmentStatus.HONORED) {
            return appointment;
        }
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new BusinessRuleException("Un rendez-vous annulé ne peut pas être honoré.");
        }
        appointment.honor();
        appointment.getCustomer().addLoyaltyPoints(HONORED_APPOINTMENT_POINTS);
        loyaltyTransactionRepository.save(new LoyaltyTransaction(
                appointment.getShop(),
                appointment.getCustomer(),
                appointment,
                HONORED_APPOINTMENT_POINTS,
                "Rendez-vous honoré",
                LoyaltyTransactionType.EARNED
        ));
        return appointment;
    }

    @Transactional
    public Appointment cancelAppointment(long id) {
        Appointment appointment = getAppointment(id);
        if (appointment.getStatus() == AppointmentStatus.HONORED) {
            throw new BusinessRuleException("Un rendez-vous honoré ne peut pas être annulé.");
        }
        appointment.cancel();
        return appointment;
    }

    private Appointment getAppointment(long id) {
        return appointmentRepository.findByIdAndShopId(id, CustomerService.DEMO_SHOP_ID)
                .orElseThrow(() -> new ResourceNotFoundException("Rendez-vous introuvable."));
    }

    private Specification<Appointment> filterAppointments(LocalDate date, AppointmentStatus status, Long customerId) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("shop").get("id"), CustomerService.DEMO_SHOP_ID));
            if (date != null) {
                predicates.add(builder.equal(root.get("appointmentDate"), date));
            }
            if (status != null) {
                predicates.add(builder.equal(root.get("status"), status));
            }
            if (customerId != null) {
                predicates.add(builder.equal(root.get("customer").get("id"), customerId));
            }
            query.orderBy(builder.asc(root.get("appointmentDate")), builder.asc(root.get("appointmentTime")));
            return builder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
