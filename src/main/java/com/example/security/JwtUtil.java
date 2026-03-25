package com.example.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Component
public class JwtUtil {

    // 🔐 Secret key (must be 32+ chars)
    private final String SECRET = "mySuperSecretKeyForJwtTokenThatIsVerySecure12345";

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    // =========================
    // 🔐 GENERATE TOKEN (OLD - KEEP)
    // =========================
    public String generateToken(String email) {

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // =========================
    // 🔥 GENERATE TOKEN WITH ROLES (ADVANCED)
    // =========================
    public String generateToken(String email, List<String> roles) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // =========================
    // 🔍 EXTRACT EMAIL
    // =========================
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // =========================
    // 🔍 EXTRACT ROLES (NEW)
    // =========================
    public List<String> extractRoles(String token) {
        Claims claims = getClaims(token);
        return claims.get("roles", List.class);
    }

    // =========================
    // 🔍 EXTRACT EXPIRATION
    // =========================
    public Date extractExpiration(String token) {
        return getClaims(token).getExpiration();
    }

    // =========================
    // 🔍 CHECK TOKEN EXPIRY
    // =========================
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // =========================
    // 🔍 VALIDATE TOKEN (BASIC)
    // =========================
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // =========================
    // 🔥 VALIDATE TOKEN WITH USER
    // =========================
    public boolean isValid(String token, String email) {
        String extractedEmail = extractEmail(token);
        return extractedEmail.equals(email) && !isTokenExpired(token);
    }

    // =========================
    // 🔍 GET CLAIMS
    // =========================
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}