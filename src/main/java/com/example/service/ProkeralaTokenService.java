package com.example.service;

import com.example.dto.response.ProkeralaTokenResponseDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ProkeralaTokenService {

    private final WebClient webClient;

    @Value("${prokerala.api.client-id}")
    private String clientId;

    @Value("${prokerala.api.client-secret}")
    private String clientSecret;

    private String accessToken;
    private Instant expiryTime;

    // ✅ STEP 1: Verify config at startup
    @PostConstruct
    public void init() {
        System.out.println("===== PROKERALA CONFIG =====");
        System.out.println("CLIENT_ID: " + clientId);

        if (clientSecret != null) {
            System.out.println("CLIENT_SECRET LENGTH: " + clientSecret.length());
        } else {
            System.out.println("CLIENT_SECRET: NULL");
        }

        System.out.println("============================");
    }

    public String getToken() {

        System.out.println("🔍 CLIENT_ID (runtime): " + clientId);

        // ✅ 1. Return cached token
        if (accessToken != null && expiryTime != null &&
                Instant.now().isBefore(expiryTime)) {

            System.out.println("🟢 Using cached token");
            return accessToken;
        }

        System.out.println("🔥 Calling Prokerala Token API");

        // ✅ 2. Call API (RAW BODY - MOST RELIABLE)
        ProkeralaTokenResponseDTO response = webClient.post()
                .uri("https://api.prokerala.com/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue(
                        "grant_type=client_credentials" +
                                "&client_id=" + clientId +
                                "&client_secret=" + clientSecret
                )
                .retrieve()
                // ✅ STEP 3: SHOW REAL ERROR
                .onStatus(status -> status.isError(),
                        res -> res.bodyToMono(String.class)
                                .map(error -> new RuntimeException("❌ REAL API ERROR: " + error))
                )
                .bodyToMono(ProkeralaTokenResponseDTO.class)
                .block();

        // ✅ 4. Null safety
        if (response == null || response.getAccessToken() == null) {
            throw new RuntimeException("❌ Failed to fetch token from Prokerala API");
        }

        // ✅ 5. Save token
        accessToken = response.getAccessToken();
        expiryTime = Instant.now().plusSeconds(response.getExpiresIn() - 60);

        System.out.println("✅ Token Generated Successfully");

        return accessToken;
    }
}