package com.shopwise.backend.api;

import com.shopwise.backend.api.dto.ServiceResponse;
import com.shopwise.backend.service.ServiceOfferingService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/services")
public class ServiceOfferingController {
    private final ServiceOfferingService serviceOfferingService;

    public ServiceOfferingController(ServiceOfferingService serviceOfferingService) {
        this.serviceOfferingService = serviceOfferingService;
    }

    @GetMapping
    public List<ServiceResponse> listServices() {
        return serviceOfferingService.listActiveServices().stream().map(ServiceResponse::from).toList();
    }
}
