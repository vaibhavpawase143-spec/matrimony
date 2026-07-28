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

    @Override
    public void verify(String token, String expectedAction) {

        if (token == null || token.isBlank()) {
            throw new RuntimeException("reCAPTCHA token is missing.");
        }

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("secret", properties.getSecretKey());
        formData.add("response", token);

        RecaptchaResponse response = webClient
                .post()
                .uri(properties.getVerifyUrl())
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(RecaptchaResponse.class)
                .block();

        if (response == null) {
            throw new RuntimeException("Unable to verify reCAPTCHA.");
        }

        if (!response.isSuccess()) {
            throw new RuntimeException("reCAPTCHA verification failed.");
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