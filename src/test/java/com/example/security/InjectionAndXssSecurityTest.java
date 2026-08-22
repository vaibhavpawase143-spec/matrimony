package com.example.security;

import com.example.config.BaseIntegrationTest;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Task 3 — Injection & XSS Payload Handling Security Tests")
class InjectionAndXssSecurityTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("SQL Injection: Harmless SQL injection in user login must NOT bypass authentication")
    void testUserLogin_SqlInjectionPayload_RejectedSafely() throws Exception {
        String payload = """
                {
                    "email": "' OR '1'='1",
                    "password": "' OR '1'='1"
                }
                """;

        String responseBody = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Must not contain database syntax errors or stack traces
        assertFalse(responseBody.contains("PSQLException"), "Must not leak PostgreSQL exception");
        assertFalse(responseBody.contains("syntax error"), "Must not leak SQL syntax error");
        assertFalse(responseBody.contains("org.postgresql"), "Must not leak database package");
    }

    @Test
    @DisplayName("SQL Injection: SQL comment injection in login must NOT bypass authentication")
    void testUserLogin_SqlCommentInjection_RejectedSafely() throws Exception {
        String payload = """
                {
                    "email": "admin'--",
                    "password": "anyPassword"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("SQL Injection: Admin login injection payload must NOT bypass authentication")
    void testAdminLogin_SqlInjectionPayload_RejectedSafely() throws Exception {
        String payload = """
                {
                    "email": "' UNION SELECT 1, 'admin', 'hash' --",
                    "password": "password123"
                }
                """;

        String responseBody = mockMvc.perform(post("/api/admins/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertFalse(responseBody.contains("UNION"), "Response must not reflect SQL query");
        assertFalse(responseBody.contains("PSQLException"), "Response must not leak SQL exceptions");
    }

    @Test
    @DisplayName("XSS Payload: Script tag in registration name field must be handled safely without error")
    void testRegister_ScriptTagPayload_HandledSafely() throws Exception {
        String email = "xss_test_user@example.com";
        userRepository.findByEmail(email).ifPresent(userRepository::delete);

        String payload = """
                {
                    "firstName": "<script>alert('xss')</script>",
                    "lastName": "SecurityTest",
                    "email": "xss_test_user@example.com",
                    "phone": "9876543211",
                    "password": "password123",
                    "recaptchaToken": "valid-token"
                }
                """;

        // Should either register safely as plain text or handle validation
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("XSS Payload: Image onerror tag in input field handled safely")
    void testRegister_ImageOnErrorPayload_HandledSafely() throws Exception {
        String email = "img_xss_user@example.com";
        userRepository.findByEmail(email).ifPresent(userRepository::delete);

        String payload = """
                {
                    "firstName": "<img src=x onerror=alert(1)>",
                    "lastName": "XssPayload",
                    "email": "img_xss_user@example.com",
                    "phone": "9876543212",
                    "password": "password123",
                    "recaptchaToken": "valid-token"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
