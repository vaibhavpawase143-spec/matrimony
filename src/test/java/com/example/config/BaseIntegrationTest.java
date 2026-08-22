package com.example.config;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestRabbitMQConfig.class)
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired(required = false)
    protected com.example.security.ratelimit.RateLimitService rateLimitService;

    @Autowired(required = false)
    protected com.example.security.TokenRevocationService tokenRevocationService;

    @org.junit.jupiter.api.BeforeEach
    void setUpBaseIntegrationTest() {
        if (rateLimitService != null) {
            rateLimitService.clearAll();
        }
        if (tokenRevocationService != null) {
            tokenRevocationService.clearAll();
        }
    }

    @AfterEach
    void tearDownSecurity() {
        TestSecurityUtils.clearMockAuthentication();
    }
}
