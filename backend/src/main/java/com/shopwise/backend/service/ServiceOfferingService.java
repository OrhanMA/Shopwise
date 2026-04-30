package com.shopwise.backend.service;

import com.shopwise.backend.domain.ServiceOffering;
import com.shopwise.backend.repository.ServiceOfferingRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ServiceOfferingService {
    private final ServiceOfferingRepository serviceOfferingRepository;

    public ServiceOfferingService(ServiceOfferingRepository serviceOfferingRepository) {
        this.serviceOfferingRepository = serviceOfferingRepository;
    }

    public List<ServiceOffering> listActiveServices() {
        return serviceOfferingRepository.findByShopIdAndActiveTrueOrderByNameAsc(CustomerService.DEMO_SHOP_ID);
    }
}
