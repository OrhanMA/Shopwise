package com.shopwise.backend.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopwise.backend.api.dto.CustomerRequest;
import com.shopwise.backend.domain.Customer;
import com.shopwise.backend.domain.Shop;
import com.shopwise.backend.service.BusinessRuleException;
import com.shopwise.backend.service.CustomerService;
import com.shopwise.backend.testutil.TestEntityUtils;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @Test
    void listCustomersShouldReturnPayload() throws Exception {
        when(customerService.listCustomers()).thenReturn(List.of(createCustomer(1L, "Alice", "Martin")));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Alice"))
                .andExpect(jsonPath("$[0].lastName").value("Martin"));
    }

    @Test
    void createCustomerShouldReturnCreated() throws Exception {
        when(customerService.createCustomer(any())).thenReturn(createCustomer(10L, "Alice", "Martin"));

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CustomerRequest(
                                "Alice", "Martin", "alice@example.com", "+33600000000"
                        ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void updateCustomerShouldReturnConflictOnDuplicateEmail() throws Exception {
        when(customerService.updateCustomer(any(Long.class), any()))
                .thenThrow(new BusinessRuleException("Un autre client existe déjà avec cet email."));

        mockMvc.perform(put("/api/customers/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CustomerRequest(
                                "Alice", "Martin", "alice@example.com", "+33600000000"
                        ))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Un autre client existe déjà avec cet email."));
    }

    @Test
    void simulateLoginShouldReturnCustomer() throws Exception {
        when(customerService.findByEmail("alice@example.com")).thenReturn(createCustomer(2L, "Alice", "Martin"));

        mockMvc.perform(post("/api/customers/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"alice@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.firstName").value("Alice"));
    }

    private Customer createCustomer(long id, String firstName, String lastName) {
        Shop shop = new Shop();
        TestEntityUtils.setField(shop, "id", 1L);
        Customer customer = new Customer();
        TestEntityUtils.setField(customer, "id", id);
        customer.setShop(shop);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(firstName.toLowerCase() + "@example.com");
        customer.setPhone("+33600000000");
        return customer;
    }
}
