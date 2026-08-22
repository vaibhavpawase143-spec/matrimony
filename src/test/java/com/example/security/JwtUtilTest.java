package com.example.security;

import com.example.config.BaseIntegrationTest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("1. JWT Utility Security Regression Tests")
class JwtUtilTest extends BaseIntegrationTest {

    @Autowired
    private JwtUtil jwtUtil;

    private final String testSecret = "TestSecretKeyForAntigravityMatrimonyTestingProfile1234567890!";

    @Test
    @DisplayName("Valid JWT token generation, signature verification, and claims extraction")
    void testValidTokenGenerationAndClaims() {
        String username = "jwt_valid_user@example.com";
        List<String> roles = List.of("ROLE_USER");
        String sessionId = "sess-uuid-1234";
        String accountType = "USER";

        String token = jwtUtil.generateToken(username, roles, sessionId, accountType);

        assertNotNull(token, "Generated JWT token must not be null");
        assertTrue(jwtUtil.isValid(token, username), "Token must be valid for the subject username");
        assertEquals(username, jwtUtil.extractUsername(token), "Subject claim must match username");
        assertEquals(roles, jwtUtil.extractRoles(token), "Roles claim must match provided roles");
        assertEquals(accountType, jwtUtil.extractAccountType(token), "AccountType claim must match USER");
        assertEquals(sessionId, jwtUtil.extractSessionId(token), "SessionId claim must match provided sessionId");
    }

    @Test
    @DisplayName("Expired JWT token must be rejected by isValid and claims extraction")
    void testExpiredToken() {
        String username = "jwt_expired_user@example.com";
        String expiredToken = Jwts.builder()
                .setSubject(username)
                .claim("roles", List.of("ROLE_USER"))
                .claim("sessionId", "expired-sess")
                .claim("accountType", "USER")
                .setIssuedAt(new Date(System.currentTimeMillis() - 20000))
                .setExpiration(new Date(System.currentTimeMillis() - 10000))
                .signWith(Keys.hmacShaKeyFor(testSecret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();

        assertFalse(jwtUtil.isValid(expiredToken, username), "Expired token must not be valid");
        assertNull(jwtUtil.extractUsername(expiredToken), "extractUsername must return null for expired token");
    }

    @Test
    @DisplayName("JWT with untrusted / tampered signature must be rejected")
    void testInvalidSignature() {
        String username = "jwt_tampered_user@example.com";
        String wrongSecret = "TamperedSecretKeyThatDoesNotMatchTheConfiguredKey99999!";
        String tamperedToken = Jwts.builder()
                .setSubject(username)
                .claim("roles", List.of("ROLE_ADMIN"))
                .claim("sessionId", "tampered-sess")
                .claim("accountType", "ADMIN")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60000))
                .signWith(Keys.hmacShaKeyFor(wrongSecret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();

        assertFalse(jwtUtil.isValid(tamperedToken, username), "Tampered signature token must not be valid");
        assertNull(jwtUtil.extractUsername(tamperedToken), "extractUsername must return null for invalid signature");
    }

    @Test
    @DisplayName("Malformed JWT string must fail gracefully without throwing unhandled exceptions")
    void testMalformedJwt() {
        String malformedToken = "invalid.jwt.token.string.format";

        assertDoesNotThrow(() -> {
            boolean valid = jwtUtil.isValid(malformedToken, "jwt_user@example.com");
            assertFalse(valid, "Malformed token must return false");
            assertNull(jwtUtil.extractUsername(malformedToken), "Malformed token must return null username");
        });
    }

    @Test
    @DisplayName("JWT validation for mismatched username must fail")
    void testUsernameMismatch() {
        String token = jwtUtil.generateToken("user_a@example.com", List.of("ROLE_USER"), "sess-a", "USER");

        assertFalse(jwtUtil.isValid(token, "user_b@example.com"), "Token must not validate against mismatched username");
    }

    @Test
    @DisplayName("Null or empty token string must return false and null claims safely")
    void testNullAndEmptyTokenValidation() {
        assertFalse(jwtUtil.isValid(null, "user@example.com"));
        assertFalse(jwtUtil.isValid("", "user@example.com"));
        assertFalse(jwtUtil.isValid("   ", "user@example.com"));
        assertNull(jwtUtil.extractUsername(null));
        assertNull(jwtUtil.extractUsername(""));
    }

    @Test
    @DisplayName("JWT username validation must be case-insensitive")
    void testCaseInsensitiveUsernameValidation() {
        String token = jwtUtil.generateToken("testuser@example.com", List.of("ROLE_USER"), "sess", "USER");

        assertTrue(jwtUtil.isValid(token, "TESTUSER@EXAMPLE.COM"), "Token validation should be case-insensitive for email username");
    }
}
