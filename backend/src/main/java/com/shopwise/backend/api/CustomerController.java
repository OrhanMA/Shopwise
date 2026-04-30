package com.shopwise.backend.api;

import com.shopwise.backend.api.dto.CustomerRequest;
import com.shopwise.backend.api.dto.CustomerResponse;
import com.shopwise.backend.service.CustomerService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<CustomerResponse> listCustomers() {
        return customerService.listCustomers().stream().map(CustomerResponse::from).toList();
    }

    @GetMapping("/{id}")
    public CustomerResponse getCustomer(@PathVariable long id) {
        return CustomerResponse.from(customerService.getCustomer(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(@Valid @RequestBody CustomerRequest request) {
        return CustomerResponse.from(customerService.createCustomer(request));
    }

    @PutMapping("/{id}")
    public CustomerResponse updateCustomer(@PathVariable long id, @Valid @RequestBody CustomerRequest request) {
        return CustomerResponse.from(customerService.updateCustomer(id, request));
    }

    @PostMapping("/login")
    public CustomerResponse simulateLogin(@RequestBody CustomerLoginRequest request) {
        return CustomerResponse.from(customerService.findByEmail(request.email()));
    }

    public record CustomerLoginRequest(String email) {
    }
}
