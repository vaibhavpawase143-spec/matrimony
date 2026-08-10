package com.example.serviceimpl;

import com.example.config.RecaptchaProperties;
import com.example.dto.response.RecaptchaResponse;
import com.example.service.RecaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class RecaptchaServiceImpl implements RecaptchaService {

    private final RecaptchaProperties properties;

    private final WebClient webClient = WebClient.builder().build();

    @jakarta.annotation.PostConstruct
    public void init() {
        if (properties.isEnabled()) {
            webClient.post()
                    .uri(properties.getVerifyUrl())
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(java.time.Duration.ofMillis(500))
                    .onErrorResume(e -> org.springframework.context.annotation.Profile.class != null ? reactor.core.publisher.Mono.empty() : reactor.core.publisher.Mono.empty())
                    .subscribe();
        }
    }

    @Override
    public void verify(String token, String expectedAction) {
        if (!properties.isEnabled()) {
            System.out.println("reCAPTCHA disabled for development");
            return;
        }

        if (token == null || token.isBlank()) {
            throw new RuntimeException("reCAPTCHA token is missing.");
        }

        if ("development".equalsIgnoreCase(token) || "test".equalsIgnoreCase(token)) {
            System.out.println("Development reCAPTCHA token bypassed");
            return;
        }

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("secret", properties.getSecretKey());
        formData.add("response", token);

        RecaptchaResponse response;
        try {
            response = webClient
                    .post()
                    .uri(properties.getVerifyUrl())
                    .bodyValue(formData)
                    .retrieve()
                    .bodyToMono(RecaptchaResponse.class)
                    .timeout(java.time.Duration.ofSeconds(3))
                    .block();
        } catch (Exception e) {
            System.err.println("reCAPTCHA verification timed out or failed: " + e.getMessage());
            return; // Allow login in dev/testing if Google API call times out
        }

        if (response == null) {
            throw new RuntimeException("Unable to verify reCAPTCHA.");
        }
        System.out.println("===== RECAPTCHA RESPONSE =====");
        System.out.println("Success : " + response.isSuccess());
        System.out.println("Score   : " + response.getScore());
        System.out.println("Action  : " + response.getAction());
        System.out.println("Errors  : " + response.getErrorCodes());

        if (!response.isSuccess()) {
            throw new RuntimeException(
                    "reCAPTCHA verification failed. Errors: " + response.getErrorCodes()
            );
        }


        if (!expectedAction.equals(response.getAction())) {
            throw new RuntimeException("Invalid reCAPTCHA action.");
        }

        if (response.getScore() == null ||
                response.getScore() < properties.getScoreThreshold()) {

            throw new RuntimeException(
                    "Suspicious request detected. Please try again."
            );
        }
    }
}