package com.example.security;

import com.example.config.BaseIntegrationTest;
import com.example.config.CorsConfig;
import com.example.config.WebSocketAuthInterceptor;
import com.example.model.Profile;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.AdminRepository;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Task 8 Baseline — CORS, Security Headers & API Security Configuration Tests")
class SecurityHeadersCorsTest extends BaseIntegrationTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private com.example.repository.ProfileRepository profileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Profile createTestProfile(User user) {
        Profile profile = profileRepository.findByUserId(user.getId()).orElseGet(Profile::new);
        profile.setUser(user);
        profile.setAbout("Test profile about");
        profile.setIsActive(true);
        profile.setIsPremium(false);
        return profileRepository.saveAndFlush(profile);
    }

    private Role getOrCreateRole(String roleName) {
        return roleRepository.findByName(roleName).orElseGet(() -> {
            Role r = new Role();
            r.setName(roleName);
            r.setIsActive(true);
            return roleRepository.save(r);
        });
    }

    private User getOrCreateTestUser(String email, String rawPassword) {
        Role role = getOrCreateRole("ROLE_USER");
        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setFirstName("CorsUser");
        user.setLastName("Test");
        user.setEmail(email);
        user.setPhone("99" + String.format("%08d", Math.abs((email + System.currentTimeMillis()).hashCode() % 100000000)));
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setIsActive(true);
        user.setEmailVerified(true);
        user.setPhoneVerified(true);
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setRoles(Set.of(role));
        return userRepository.saveAndFlush(user);
    }

    // =========================================================================
    // SECTION A: CORS ALLOWED & DISALLOWED ORIGINS
    // =========================================================================

    @Test
    @DisplayName("1. CORS: Trusted frontend origin http://localhost:3000 receives Allow-Origin and Allow-Credentials")
    void testCors_TrustedFrontendOrigin_Allowed() throws Exception {
        mockMvc.perform(get("/api/master/religions")
                        .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"))
                .andExpect(header().string("Access-Control-Allow-Credentials", "true"))
                .andExpect(header().string("Vary", org.hamcrest.Matchers.containsString("Origin")));
    }

    @Test
    @DisplayName("2. CORS: Trusted admin origin http://localhost:5173 receives Allow-Origin")
    void testCors_TrustedAdminOrigin_Allowed() throws Exception {
        mockMvc.perform(get("/api/master/religions")
                        .header("Origin", "http://localhost:5173"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"))
                .andExpect(header().string("Access-Control-Allow-Credentials", "true"));
    }

    @Test
    @DisplayName("3. CORS: Trusted loopback origin http://127.0.0.1:3000 receives Allow-Origin")
    void testCors_TrustedLoopbackOrigin_Allowed() throws Exception {
        mockMvc.perform(get("/api/master/religions")
                        .header("Origin", "http://127.0.0.1:3000"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://127.0.0.1:3000"))
                .andExpect(header().string("Access-Control-Allow-Credentials", "true"));
    }

    @Test
    @DisplayName("4. CORS: Unconfigured arbitrary LAN origin http://192.168.1.100:3000 is rejected with HTTP 403 Forbidden")
    void testCors_LanOriginPattern_Rejected() throws Exception {
        mockMvc.perform(get("/api/master/religions")
                        .header("Origin", "http://192.168.1.100:3000"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }

    @Test
    @DisplayName("5. CORS: Malicious origin http://evil-attacker.com is rejected with HTTP 403 Forbidden")
    void testCors_MaliciousOrigin_Rejected() throws Exception {
        mockMvc.perform(get("/api/master/religions")
                        .header("Origin", "http://evil-attacker.com"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }

    @Test
    @DisplayName("6. CORS: Null origin (file:/// or sandboxed iframe) is rejected with HTTP 403 Forbidden")
    void testCors_NullOrigin_Rejected() throws Exception {
        mockMvc.perform(get("/api/master/religions")
                        .header("Origin", "null"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }

    @Test
    @DisplayName("7. CORS: Lookalike attack origin http://localhost.attacker.com is rejected with HTTP 403 Forbidden")
    void testCors_LookalikeOrigin_Rejected() throws Exception {
        mockMvc.perform(get("/api/master/religions")
                        .header("Origin", "http://localhost.attacker.com"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }

    @Test
    @DisplayName("8. CORS: Disallowed port http://localhost:9999 is rejected with HTTP 403 Forbidden")
    void testCors_DisallowedPort_Rejected() throws Exception {
        mockMvc.perform(get("/api/master/religions")
                        .header("Origin", "http://localhost:9999"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }

    // =========================================================================
    // SECTION B: CORS PRE-FLIGHT (OPTIONS) BEHAVIOR
    // =========================================================================

    @Test
    @DisplayName("9. Preflight: Trusted origin OPTIONS preflight returns HTTP 200 with methods and headers")
    void testPreflight_TrustedOrigin_Returns200WithHeaders() throws Exception {
        mockMvc.perform(options("/api/profiles/me")
                        .header("Origin", "http://localhost:3000")
                        .header("Access-Control-Request-Method", "PUT")
                        .header("Access-Control-Request-Headers", "Authorization,Content-Type"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"))
                .andExpect(header().string("Access-Control-Allow-Credentials", "true"))
                .andExpect(header().string("Access-Control-Max-Age", "3600"))
                .andExpect(header().exists("Access-Control-Allow-Methods"))
                .andExpect(header().exists("Access-Control-Allow-Headers"));
    }

    @Test
    @DisplayName("10. Preflight: Malicious origin OPTIONS preflight is rejected with HTTP 403 Forbidden")
    void testPreflight_MaliciousOrigin_RejectedWith403() throws Exception {
        mockMvc.perform(options("/api/profiles/me")
                        .header("Origin", "http://evil-attacker.com")
                        .header("Access-Control-Request-Method", "POST")
                        .header("Access-Control-Request-Headers", "Authorization"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }

    @Test
    @DisplayName("11. Preflight: Exposed headers include Authorization and Content-Disposition")
    void testPreflight_ExposedHeaders_Configured() throws Exception {
        mockMvc.perform(options("/api/auth/login")
                        .header("Origin", "http://localhost:3000")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Expose-Headers", org.hamcrest.Matchers.containsString("Authorization")));
    }

    // =========================================================================
    // SECTION C: CORS CANNOT BYPASS AUTHENTICATION OR AUTHORIZATION
    // =========================================================================

    @Test
    @DisplayName("12. CORS Bypass: Sending trusted Origin header does NOT bypass missing JWT authentication (HTTP 401)")
    void testCorsBypass_MissingJwt_Rejected401() throws Exception {
        mockMvc.perform(get("/api/profiles/me")
                        .header("Origin", "http://localhost:3000"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    @DisplayName("13. CORS Bypass: Sending trusted Origin header with forged JWT is rejected (HTTP 401)")
    void testCorsBypass_ForgedJwt_Rejected401() throws Exception {
        mockMvc.perform(get("/api/profiles/me")
                        .header("Origin", "http://localhost:3000")
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.fake.signature"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("14. CORS Bypass: Sending trusted Origin header does NOT bypass role-based access control (HTTP 403)")
    void testCorsBypass_UserTokenOnAdminEndpoint_Rejected403() throws Exception {
        User user = getOrCreateTestUser("cors_user_authz@example.com", "Pass123!");
        String token = jwtUtil.generateToken(user.getEmail(), List.of("ROLE_USER"), "sess-cors-1", "USER");

        mockMvc.perform(get("/api/admin/dashboard")
                        .header("Origin", "http://localhost:3000")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    // =========================================================================
    // SECTION D: SECURITY HTTP HEADERS BASELINE
    // =========================================================================

    @Test
    @DisplayName("15. Security Headers: X-Content-Type-Options: nosniff is present (MIME sniffing protection)")
    void testSecurityHeaders_XContentTypeOptions_Nosniff() throws Exception {
        mockMvc.perform(get("/api/master/religions"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Content-Type-Options", "nosniff"));
    }

    @Test
    @DisplayName("16. Security Headers: X-Frame-Options: DENY is present (Clickjacking protection)")
    void testSecurityHeaders_XFrameOptions_Deny() throws Exception {
        mockMvc.perform(get("/api/master/religions"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Frame-Options", "DENY"));
    }

    @Test
    @DisplayName("17. Security Headers: Cache-Control and Pragma no-cache are present on protected API endpoints")
    void testSecurityHeaders_CacheControl_ProtectedEndpoints() throws Exception {
        User user = getOrCreateTestUser("cors_cache_user@example.com", "Pass123!");
        createTestProfile(user);
        String token = jwtUtil.generateToken(user.getEmail(), List.of("ROLE_USER"), "sess-cache-1", "USER");

        mockMvc.perform(get("/api/profiles/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", org.hamcrest.Matchers.containsString("no-cache")))
                .andExpect(header().string("Pragma", "no-cache"))
                .andExpect(header().string("Expires", "0"));
    }

    @Test
    @DisplayName("18. Security Headers: Content-Security-Policy header is present with frame-ancestors and safe directives")
    void testSecurityHeaders_ContentSecurityPolicy_Present() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/master/religions"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Content-Security-Policy"))
                .andReturn();

        String csp = result.getResponse().getHeader("Content-Security-Policy");
        assertNotNull(csp, "CSP header must be present");
        assertTrue(csp.contains("frame-ancestors 'self'"), "CSP must include frame-ancestors 'self'");
        assertTrue(csp.contains("default-src 'self'"), "CSP must include default-src 'self'");
        assertTrue(csp.contains("img-src 'self' data: blob: https:"), "CSP must permit self, data, blob, and https images");
    }

    @Test
    @DisplayName("19. Security Headers: Referrer-Policy is present with strict-origin-when-cross-origin")
    void testSecurityHeaders_ReferrerPolicy_Present() throws Exception {
        mockMvc.perform(get("/api/master/religions"))
                .andExpect(status().isOk())
                .andExpect(header().string("Referrer-Policy", "strict-origin-when-cross-origin"));
    }

    @Test
    @DisplayName("20. Security Headers: Permissions-Policy is present restricting camera, microphone, and geolocation")
    void testSecurityHeaders_PermissionsPolicy_Present() throws Exception {
        mockMvc.perform(get("/api/master/religions"))
                .andExpect(status().isOk())
                .andExpect(header().string("Permissions-Policy", "camera=(), microphone=(), geolocation=()"));
    }

    // =========================================================================
    // SECTION E: HTTPS & HSTS SECURE TRANSPORT
    // =========================================================================

    @Test
    @DisplayName("21. HTTPS / HSTS: Secure HTTPS requests produce Strict-Transport-Security header")
    void testHttps_SecureRequest_ProducesHsts() throws Exception {
        mockMvc.perform(get("/api/master/religions").secure(true))
                .andExpect(status().isOk())
                .andExpect(header().string("Strict-Transport-Security", org.hamcrest.Matchers.containsString("max-age=")));
    }

    @Test
    @DisplayName("22. HTTPS / HSTS: Plain HTTP requests do not emit HSTS header (safe for local development)")
    void testHttps_PlainHttpRequest_NoHsts() throws Exception {
        mockMvc.perform(get("/api/master/religions").secure(false))
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist("Strict-Transport-Security"));
    }

    // =========================================================================
    // SECTION F: CSRF ARCHITECTURE
    // =========================================================================

    @Test
    @DisplayName("23. CSRF: State-changing POST with Bearer JWT succeeds without CSRF token (Stateless REST architecture)")
    void testCsrf_BearerJwtStateChanging_Allowed() throws Exception {
        User user = getOrCreateTestUser("csrf_test_user@example.com", "Pass123!");
        String token = jwtUtil.generateToken(user.getEmail(), List.of("ROLE_USER"), "sess-csrf-1", "USER");

        String payload = String.format("""
                {
                    "userId": %d,
                    "minAge": 22,
                    "maxAge": 30,
                    "isActive": true
                }
                """, user.getId());

        mockMvc.perform(post("/api/partner-preferences")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("24. CSRF: Missing Bearer token returns HTTP 401 Unauthorized rather than CSRF error")
    void testCsrf_MissingBearer_Returns401NotCsrf() throws Exception {
        mockMvc.perform(post("/api/partner-preferences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    // =========================================================================
    // SECTION G: ACTUATOR & MANAGEMENT ENDPOINTS
    // =========================================================================

    @Test
    @DisplayName("25. Actuator: /actuator/health is publicly accessible for load balancer probes")
    void testActuator_HealthEndpoint_PubliclyAccessible() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("26. Actuator: /actuator/env is protected and returns HTTP 401 for anonymous caller")
    void testActuator_EnvEndpoint_Protected() throws Exception {
        mockMvc.perform(get("/actuator/env"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("27. Actuator: /actuator/beans is protected and returns HTTP 401 for anonymous caller")
    void testActuator_BeansEndpoint_Protected() throws Exception {
        mockMvc.perform(get("/actuator/beans"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("28. Actuator: /actuator root rejects regular ROLE_USER with HTTP 403 Forbidden")
    void testActuator_Root_UserRejected403() throws Exception {
        User user = getOrCreateTestUser("actuator_user@example.com", "Pass123!");
        String token = jwtUtil.generateToken(user.getEmail(), List.of("ROLE_USER"), "sess-act-user", "USER");

        mockMvc.perform(get("/actuator")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    // =========================================================================
    // SECTION H: SWAGGER & OPENAPI EXPOSURE
    // =========================================================================

    @Test
    @DisplayName("29. Swagger: /swagger-ui/index.html is protected and returns HTTP 401 for unauthenticated caller")
    void testSwagger_UiEndpoint_Protected() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("30. Swagger: /v3/api-docs OpenAPI JSON is protected and returns HTTP 401 for unauthenticated caller")
    void testSwagger_ApiDocsEndpoint_Protected() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("31. Swagger: /swagger-ui/index.html rejects regular ROLE_USER with HTTP 403 Forbidden")
    void testSwagger_UserRejected403() throws Exception {
        User user = getOrCreateTestUser("swagger_user@example.com", "Pass123!");
        String token = jwtUtil.generateToken(user.getEmail(), List.of("ROLE_USER"), "sess-swag-user", "USER");

        mockMvc.perform(get("/swagger-ui/index.html")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    // =========================================================================
    // SECTION I: SERVER INFORMATION DISCLOSURE & ERROR SANITIZATION
    // =========================================================================

    @Test
    @DisplayName("32. Server Info: Error responses do not leak stack traces or internal server class names")
    void testServerInfo_ErrorResponse_NoStacktraceLeak() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/master/invalid-nonexistent-endpoint-404"))
                .andExpect(status().isNotFound())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertFalse(content.contains("at org.springframework"), "Response must not contain Java stack trace");
        assertFalse(content.contains("at org.apache.catalina"), "Response must not contain Tomcat stack trace");
        assertFalse(content.contains("at org.hibernate"), "Response must not contain Hibernate stack trace");
    }

    @Test
    @DisplayName("33. Server Info: 500 error response does not leak database connection details")
    void testServerInfo_500Error_SanitizedResponse() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/profiles/999999999"))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertFalse(content.contains("jdbc:postgresql"), "Response must not contain JDBC URL");
        assertFalse(content.contains("password"), "Response must not contain database password");
        assertFalse(content.contains("jwt.secret"), "Response must not leak secrets");
    }

    // =========================================================================
    // SECTION J: API HTTP METHOD HANDLING
    // =========================================================================

    @Test
    @DisplayName("34. HTTP Method: Unsupported DELETE on POST-only endpoint returns clean HTTP 405 Method Not Allowed")
    void testHttpMethod_UnsupportedMethod_Returns405() throws Exception {
        mockMvc.perform(delete("/api/auth/login"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("METHOD_NOT_ALLOWED"));
    }

    @Test
    @DisplayName("35. HTTP Method: Unsupported PUT on POST-only /api/auth/register returns HTTP 405")
    void testHttpMethod_UnsupportedPutOnRegister_Returns405() throws Exception {
        mockMvc.perform(put("/api/auth/register"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.errorCode").value("METHOD_NOT_ALLOWED"));
    }

    // =========================================================================
    // SECTION K: STATIC RESOURCE EXPOSURE & PATH TRAVERSAL
    // =========================================================================

    @Test
    @DisplayName("36. Static Resources: Path traversal attempt on /uploads is rejected")
    void testStaticResource_PathTraversal_Rejected() throws Exception {
        mockMvc.perform(get("/uploads/../application.properties"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("37. Static Resources: Root directory listing on /uploads/ is not exposed")
    void testStaticResource_RootDirectoryListing_NotExposed() throws Exception {
        MvcResult result = mockMvc.perform(get("/uploads/")).andReturn();
        int status = result.getResponse().getStatus();
        assertTrue(status == 404 || status == 403 || status == 401 || status == 200);
        String body = result.getResponse().getContentAsString();
        assertFalse(body.contains("Index of /uploads"), "Directory listing must not be exposed");
    }

    // =========================================================================
    // SECTION L: WEBSOCKET HANDSHAKE & AUTHENTICATION
    // =========================================================================

    @Test
    @DisplayName("38. WebSocket: Handshake interceptor rejects connection without token parameter")
    void testWebSocket_HandshakeWithoutToken_Rejected() {
        WebSocketAuthInterceptor interceptor = new WebSocketAuthInterceptor(jwtUtil);
        MockHttpServletRequest servletRequest = new MockHttpServletRequest("GET", "/ws");
        ServletServerHttpRequest request = new ServletServerHttpRequest(servletRequest);
        ServletServerHttpResponse response = new ServletServerHttpResponse(new MockHttpServletResponse());
        Map<String, Object> attributes = new HashMap<>();

        boolean allowed = interceptor.beforeHandshake(request, response, null, attributes);
        assertFalse(allowed, "WebSocket handshake must be rejected when token query parameter is missing");
    }

    @Test
    @DisplayName("39. WebSocket: Handshake interceptor rejects connection with invalid token")
    void testWebSocket_HandshakeWithInvalidToken_Rejected() {
        WebSocketAuthInterceptor interceptor = new WebSocketAuthInterceptor(jwtUtil);
        MockHttpServletRequest servletRequest = new MockHttpServletRequest("GET", "/ws");
        servletRequest.setQueryString("token=invalid.token.here");
        ServletServerHttpRequest request = new ServletServerHttpRequest(servletRequest);
        ServletServerHttpResponse response = new ServletServerHttpResponse(new MockHttpServletResponse());
        Map<String, Object> attributes = new HashMap<>();

        boolean allowed = interceptor.beforeHandshake(request, response, null, attributes);
        assertFalse(allowed, "WebSocket handshake must be rejected when token is invalid");
    }

    @Test
    @DisplayName("40. WebSocket: Handshake interceptor accepts connection with valid JWT and sets username attribute")
    void testWebSocket_HandshakeWithValidToken_Accepted() {
        User user = getOrCreateTestUser("ws_handshake_user@example.com", "Pass123!");
        String token = jwtUtil.generateToken(user.getEmail(), List.of("ROLE_USER"), "sess-ws-1", "USER");

        WebSocketAuthInterceptor interceptor = new WebSocketAuthInterceptor(jwtUtil);
        MockHttpServletRequest servletRequest = new MockHttpServletRequest("GET", "/ws");
        servletRequest.setQueryString("token=" + token);
        ServletServerHttpRequest request = new ServletServerHttpRequest(servletRequest);
        ServletServerHttpResponse response = new ServletServerHttpResponse(new MockHttpServletResponse());
        Map<String, Object> attributes = new HashMap<>();

        boolean allowed = interceptor.beforeHandshake(request, response, null, attributes);
        assertTrue(allowed, "WebSocket handshake must be accepted when valid JWT is provided");
        assertEquals(user.getEmail(), attributes.get("username"), "Authenticated username must be placed in session attributes");
    }

    @Autowired
    private CorsConfig corsConfig;

    @Test
    @DisplayName("41. CORS / WebSocket: Allowed origins list does not contain LAN wildcard or hardcoded placeholders")
    void testCorsConfig_AllowedOrigins_NoWildcardOrPlaceholders() {
        List<String> origins = corsConfig.getAllowedOrigins();
        assertTrue(origins.contains("http://localhost:3000"), "Must contain localhost:3000");
        assertTrue(origins.contains("http://localhost:5173"), "Must contain localhost:5173");
        assertFalse(origins.contains("http://192.168.*:*"), "Must NOT contain LAN wildcard pattern");
        assertFalse(origins.contains("https://yourdomain.com"), "Must NOT contain hardcoded placeholder");
    }

    @Test
    @DisplayName("42. WebSocket: SockJS handshake info endpoint with trusted origin succeeds")
    void testWebSocket_SockJsInfo_TrustedOrigin_Allowed() throws Exception {
        mockMvc.perform(get("/ws/info")
                        .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"))
                .andExpect(header().string("Access-Control-Allow-Credentials", "true"));
    }

    @Test
    @DisplayName("43. WebSocket: SockJS handshake info endpoint with untrusted origin is rejected")
    void testWebSocket_SockJsInfo_UntrustedOrigin_Rejected() throws Exception {
        mockMvc.perform(get("/ws/info")
                        .header("Origin", "http://evil-attacker.com"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }
}
