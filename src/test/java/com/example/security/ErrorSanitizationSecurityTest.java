package com.example.security;

import com.example.config.BaseIntegrationTest;
import com.example.config.TestSecurityUtils;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Task 3 — Error Response Sanitization & Information Leakage Prevention Tests")
class ErrorSanitizationSecurityTest extends BaseIntegrationTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private String getTestUserToken() {
        Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            Role r = new Role();
            r.setName("ROLE_USER");
            r.setIsActive(true);
            return roleRepository.save(r);
        });

        String email = "error_sanitize_user@example.com";
        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setFirstName("Sanitize");
        user.setLastName("User");
        user.setEmail(email);
        user.setPassword("password123");
        user.setIsActive(true);
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setRoles(Set.of(userRole));
        userRepository.save(user);

        return TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");
    }

    @Test
    @DisplayName("Sanitization: 400 Validation error response must NOT leak stack traces or SQL")
    void testValidationError_Sanitization() throws Exception {
        String payload = "{}";

        String responseBody = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertFalse(responseBody.contains("Exception:"), "Response must not contain exception traces");
        assertFalse(responseBody.contains("org.springframework"), "Response must not contain framework packages");
        assertFalse(responseBody.contains("SELECT "), "Response must not contain SQL queries");
        assertFalse(responseBody.contains("INSERT INTO"), "Response must not contain SQL queries");
    }

    @Test
    @DisplayName("Sanitization: Malformed JSON error response must NOT leak Jackson internal classes")
    void testMalformedJsonError_Sanitization() throws Exception {
        String malformedJson = "{invalid_json: true, ";

        String responseBody = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertFalse(responseBody.contains("com.fasterxml.jackson"), "Response must not leak Jackson parser internals");
        assertFalse(responseBody.contains("JsonParseException"), "Response must not leak internal parser exception names");
    }

    @Test
    @DisplayName("Sanitization: 404 Not Found error response must NOT leak server file paths or classes")
    void testNotFoundError_Sanitization() throws Exception {
        String token = getTestUserToken();

        String responseBody = mockMvc.perform(get("/api/non-existent-endpoint-path-12345")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertFalse(responseBody.contains("NoResourceFoundException"), "Response must not leak internal class names");
        assertFalse(responseBody.contains("org.springframework"), "Response must not leak spring packages");
    }

    @Test
    @DisplayName("Sanitization: 405 Method Not Allowed response must return standard error envelope")
    void testMethodNotAllowedError_Sanitization() throws Exception {
        String responseBody = mockMvc.perform(delete("/api/auth/register"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("METHOD_NOT_ALLOWED"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertFalse(responseBody.contains("HttpRequestMethodNotSupportedException"), "Response must not leak exception name");
    }
}
