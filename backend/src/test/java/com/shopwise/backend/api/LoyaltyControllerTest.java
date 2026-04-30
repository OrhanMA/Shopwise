package com.shopwise.backend.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.shopwise.backend.api.dto.CustomerResponse;
import com.shopwise.backend.api.dto.LoyaltySummaryResponse;
import com.shopwise.backend.api.dto.LoyaltyTransactionResponse;
import com.shopwise.backend.domain.LoyaltyTransactionType;
import com.shopwise.backend.service.LoyaltyService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LoyaltyController.class)
class LoyaltyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoyaltyService loyaltyService;

    @Test
    void getSummaryShouldReturnBalanceAndTransactions() throws Exception {
        when(loyaltyService.getSummary(1L)).thenReturn(new LoyaltySummaryResponse(
                new CustomerResponse(1L, "Alice", "Martin", "alice@example.com", "+33600000000", 200),
                200,
                List.of(new LoyaltyTransactionResponse(
                        1L, 100, "Rendez-vous honoré", LoyaltyTransactionType.EARNED, LocalDateTime.of(2026, 4, 30, 10, 0)
                ))
        ));

        mockMvc.perform(get("/api/customers/1/loyalty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(200))
                .andExpect(jsonPath("$.transactions[0].transactionType").value("EARNED"));
    }
}
