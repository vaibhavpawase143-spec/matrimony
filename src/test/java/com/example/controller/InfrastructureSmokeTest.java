package com.example.controller;

import com.example.config.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InfrastructureSmokeTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Smoke Test: Public Health endpoint should return HTTP 200 OK")
    void publicHealthEndpointShouldReturn200() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Smoke Test: Unauthenticated access to admin endpoints should return HTTP 401 Unauthorized")
    void unauthenticatedAdminAccessShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/admin/statistics"))
                .andExpect(status().isUnauthorized());
    }
}
