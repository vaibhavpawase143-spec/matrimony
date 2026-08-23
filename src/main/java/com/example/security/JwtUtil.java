package com.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key getKey() {
        if (secret == null || secret.trim().isEmpty()) {
            throw new IllegalStateException("JWT_SECRET environment variable is missing or empty!");
        }
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalStateException("JWT_SECRET must be at least 256 bits (32 characters long) for secure HMAC-SHA256 signing!");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 🔥 GENERATE TOKEN
    public String generateToken(
            String username,
            List<String> roles,
            String sessionId,
            String accountType
    ) {
        String jti = java.util.UUID.randomUUID().toString();

        return Jwts.builder()
                .setId(jti)
                .setSubject(username)
                .claim("roles", roles)
                .claim("sessionId", sessionId)
                .claim("accountType", accountType)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 🔍 EXTRACT USERNAME
    public String extractUsername(String token) {
        Claims claims = getClaims(token);
        return claims != null ? claims.getSubject() : null;
    }

    // 🔍 EXTRACT JTI
    public String extractJti(String token) {
        Claims claims = getClaims(token);
        return claims != null ? claims.getId() : null;
    }

    // 🔍 EXTRACT EXPIRATION
    public Date extractExpiration(String token) {
        Claims claims = getClaims(token);
        return claims != null ? claims.getExpiration() : null;
    }

    // 🔍 VALIDATE TOKEN
    public boolean isValid(String token, String username) {
        if (token == null || username == null) {
            return false;
        }
        try {
            Claims claims = getClaims(token);
            if (claims == null) {
                return false;
            }
            String tokenUsername = claims.getSubject();
            return username.equalsIgnoreCase(tokenUsername) && !isExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> extractRoles(String token) {
        Claims claims = getClaims(token);
        return claims != null ? claims.get("roles", List.class) : List.of();
    }

    // 🔍 GET CLAIMS
    private Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public String extractSessionId(String token) {
        Claims claims = getClaims(token);
        return claims != null ? claims.get("sessionId", String.class) : null;
    }

    private boolean isExpired(String token) {
        Claims claims = getClaims(token);
        return claims == null || claims.getExpiration().before(new Date());
    }

    public String extractAccountType(String token) {
        Claims claims = getClaims(token);
        return claims != null ? claims.get("accountType", String.class) : null;
    }
}