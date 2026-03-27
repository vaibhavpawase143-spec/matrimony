package com.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    // 🔐 Secret key (move to application.properties in production)
    private final String SECRET_KEY = "mySuperSecretKeyForJwtTokenThatIsVerySecure12345";

    // ⏱ Token expiry (10 hours)
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10;

    // =========================
    // 🔥 GENERATE TOKEN WITH ROLES (IMPORTANT)
    // =========================
    public String generateToken(String username, List<String> roles) {

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)   // ✅ ADD ROLES
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // =========================
    // 🔍 EXTRACT USERNAME
    // =========================
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // =========================
    // 🔍 EXTRACT ROLES
    // =========================
    public List<String> extractRoles(String token) {
        return extractClaims(token).get("roles", List.class);
    }

    // =========================
    // 🔥 VALIDATE TOKEN
    // =========================
    public boolean isValid(String token, String username) {
        try {
            String extractedUsername = extractUsername(token);
            return extractedUsername.equals(username) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // =========================
    // 🔍 CHECK EXPIRY
    // =========================
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // =========================
    // 🔍 EXTRACT CLAIMS
    // =========================
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}