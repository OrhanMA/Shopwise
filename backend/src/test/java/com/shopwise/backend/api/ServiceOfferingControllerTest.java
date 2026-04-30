package com.shopwise.backend.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.shopwise.backend.domain.ServiceOffering;
import com.shopwise.backend.domain.Shop;
import com.shopwise.backend.service.ServiceOfferingService;
import com.shopwise.backend.testutil.TestEntityUtils;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ServiceOfferingController.class)
class ServiceOfferingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceOfferingService serviceOfferingService;

    @Test
    void listServicesShouldReturnPayload() throws Exception {
        when(serviceOfferingService.listActiveServices()).thenReturn(List.of(createService()));

        mockMvc.perform(get("/api/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].name").value("Conseil produit"))
                .andExpect(jsonPath("$[0].pointsReward").value(100));
    }

    private ServiceOffering createService() {
        Shop shop = new Shop();
        TestEntityUtils.setField(shop, "id", 1L);
        ServiceOffering service = new ServiceOffering();
        TestEntityUtils.setField(service, "id", 3L);
        TestEntityUtils.setField(service, "shop", shop);
        TestEntityUtils.setField(service, "name", "Conseil produit");
        TestEntityUtils.setField(service, "description", "Conseil personnalisé");
        TestEntityUtils.setField(service, "durationMinutes", 30);
        TestEntityUtils.setField(service, "pointsReward", 100);
        return service;
    }
}
