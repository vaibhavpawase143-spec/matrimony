package com.example.client;

import com.example.dto.request.KundliMatchRequestDTO;
import com.example.service.ProkeralaTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class ProkeralaClient {

    private final WebClient webClient;
    private final ProkeralaTokenService tokenService;

    public String matchKundli(KundliMatchRequestDTO req) {

        String token = tokenService.getToken();

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.prokerala.com")
                        .path("/v2/astrology/kundli-matching")

                        .queryParam("ayanamsa", 1)

                        .queryParam("boy_dob", req.getMale().getDate())
                        .queryParam("boy_tob", req.getMale().getTime())
                        .queryParam("boy_lat", req.getMale().getLatitude())
                        .queryParam("boy_lon", req.getMale().getLongitude())

                        .queryParam("girl_dob", req.getFemale().getDate())
                        .queryParam("girl_tob", req.getFemale().getTime())
                        .queryParam("girl_lat", req.getFemale().getLatitude())
                        .queryParam("girl_lon", req.getFemale().getLongitude())

                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .onStatus(status -> status.isError(),
                        response -> response.bodyToMono(String.class)
                                .map(error -> new RuntimeException("API Error: " + error)))
                .bodyToMono(String.class)
                .block();
    }
}