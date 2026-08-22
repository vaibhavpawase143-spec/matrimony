package com.example.security;

import com.example.config.BaseIntegrationTest;
import com.example.config.TestSecurityUtils;
import com.example.model.Admin;
import com.example.model.Role;
import com.example.repository.AdminRepository;
import com.example.repository.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Task 3 — Input, Format, Boundary & Content Validation Security Tests")
class InputValidationSecurityTest extends BaseIntegrationTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    private String getAdminToken() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
            Role r = new Role();
            r.setName("ROLE_ADMIN");
            r.setIsActive(true);
            return roleRepository.save(r);
        });

        String email = "input_val_admin@example.com";
        Admin admin = adminRepository.findByEmailIgnoreCase(email).orElseGet(Admin::new);
        admin.setName("Input Admin");
        admin.setUsername("input_val_admin");
        admin.setEmail(email);
        admin.setPassword("password123");
        admin.setIsActive(true);
        admin.setRole(adminRole);
        admin.setSessionId("sess-input-val-admin");
        adminRepository.save(admin);

        return jwtUtil.generateToken(
                email,
                List.of("ROLE_ADMIN", "ADMIN_CREATE", "ADMIN_VIEW"),
                "sess-input-val-admin",
                "ADMIN"
        );
    }

    // =========================================================================
    // A. REQUIRED FIELD VALIDATION
    // =========================================================================

    @Test
    @DisplayName("Registration: Missing required fields must return HTTP 400 with VALIDATION_ERROR")
    void testRegister_MissingRequiredFields_Returns400() throws Exception {
        String payload = "{}";

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.fieldErrors.email").isNotEmpty())
                .andExpect(jsonPath("$.fieldErrors.firstName").isNotEmpty())
                .andExpect(jsonPath("$.fieldErrors.lastName").isNotEmpty())
                .andExpect(jsonPath("$.fieldErrors.password").isNotEmpty());
    }

    @Test
    @DisplayName("Registration: Blank values for required fields must return HTTP 400")
    void testRegister_BlankRequiredFields_Returns400() throws Exception {
        String payload = """
                {
                    "firstName": "   ",
                    "lastName": "   ",
                    "email": "   ",
                    "phone": "   ",
                    "password": "   ",
                    "recaptchaToken": "valid-token"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("Admin Creation: Missing required fields must return HTTP 400")
    void testAdminCreate_MissingRequiredFields_Returns400() throws Exception {
        String token = getAdminToken();
        String payload = """
                {
                    "name": "",
                    "username": "",
                    "email": "",
                    "password": ""
                }
                """;

        mockMvc.perform(post("/api/admins")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    // =========================================================================
    // B. LENGTH & BOUNDARY VALIDATION
    // =========================================================================

    @Test
    @DisplayName("Registration: Password shorter than 6 characters must return HTTP 400")
    void testRegister_PasswordTooShort_Returns400() throws Exception {
        String payload = """
                {
                    "firstName": "Short",
                    "lastName": "Pass",
                    "email": "shortpass@example.com",
                    "phone": "9876543210",
                    "password": "123",
                    "recaptchaToken": "valid-token"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.fieldErrors.password").value("Password must be at least 6 characters"));
    }

    @Test
    @DisplayName("Registration: Phone number exceeding 10 digits must return HTTP 400")
    void testRegister_PhoneTooLong_Returns400() throws Exception {
        String payload = """
                {
                    "firstName": "Long",
                    "lastName": "Phone",
                    "email": "longphone@example.com",
                    "phone": "12345678901234567890",
                    "password": "password123",
                    "recaptchaToken": "valid-token"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.fieldErrors.phone").value("Phone must be 10 digits"));
    }

    @Test
    @DisplayName("Admin Creation: Username boundary check (3-20 chars) must reject invalid length")
    void testAdminCreate_UsernameBoundaryViolation_Returns400() throws Exception {
        String token = getAdminToken();
        String payloadShortUsername = """
                {
                    "name": "Boundary Admin",
                    "username": "ab",
                    "email": "boundaryadmin@example.com",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/admins")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadShortUsername))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.username").value("Username must be 3-20 characters"));
    }

    @Test
    @DisplayName("Registration: Excessively long input string (5000+ chars) must be safely rejected")
    void testRegister_ExcessivelyLongString_Returns400() throws Exception {
        String hugeString = "A".repeat(5000);
        String payload = String.format("""
                {
                    "firstName": "%s",
                    "lastName": "Test",
                    "email": "hugeinput@example.com",
                    "phone": "9876543210",
                    "password": "password123",
                    "recaptchaToken": "valid-token"
                }
                """, hugeString);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    // =========================================================================
    // C. TYPE & FORMAT VALIDATION
    // =========================================================================

    @Test
    @DisplayName("Registration: Invalid email format must return HTTP 400")
    void testRegister_InvalidEmailFormat_Returns400() throws Exception {
        String payload = """
                {
                    "firstName": "Invalid",
                    "lastName": "Email",
                    "email": "not-a-valid-email-address",
                    "phone": "9876543210",
                    "password": "password123",
                    "recaptchaToken": "valid-token"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.email").value("Invalid email format"));
    }

    @Test
    @DisplayName("Admin API: Non-numeric path variable ID must return HTTP 400 TYPE_MISMATCH")
    void testAdminGet_InvalidIdTypeMismatch_Returns400() throws Exception {
        String token = getAdminToken();

        mockMvc.perform(get("/api/admins/abc-not-a-number")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("TYPE_MISMATCH"));
    }

    // =========================================================================
    // D. JSON & CONTENT VALIDATION
    // =========================================================================

    @Test
    @DisplayName("JSON Validation: Malformed JSON body must return HTTP 400 MALFORMED_JSON")
    void testRegister_MalformedJson_Returns400() throws Exception {
        String malformedJson = "{\"firstName\": \"John\", \"email\": ";

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("MALFORMED_JSON"))
                .andExpect(jsonPath("$.message").value("Malformed JSON request body or invalid field format."));
    }

    @Test
    @DisplayName("Content Type Validation: Unsupported Media Type (XML/Text) must return HTTP 415")
    void testRegister_UnsupportedMediaType_Returns415() throws Exception {
        String textPayload = "firstName=John&lastName=Doe";

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(textPayload))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("UNSUPPORTED_MEDIA_TYPE"));
    }
}
