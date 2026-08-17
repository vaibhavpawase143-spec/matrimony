package com.example.serviceimpl;

import com.example.config.RecaptchaProperties;
import com.example.dto.response.RecaptchaResponse;
import com.example.service.RecaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class RecaptchaServiceImpl implements RecaptchaService {

    private final RecaptchaProperties properties;
    private final WebClient webClient;

    public RecaptchaServiceImpl(RecaptchaProperties properties,
                                @Qualifier("recaptchaWebClient") WebClient webClient) {
        this.properties = properties;
        this.webClient = webClient;
    }

    @Override
    public void verify(String token, String expectedAction) {

        /*
         * ---------------------------------------------------------
         * 1. reCAPTCHA disabled check (Controlled by configuration)
         * ---------------------------------------------------------
         */
        if (!properties.isEnabled()) {
            log.info("reCAPTCHA verification is disabled by environment configuration.");
            return;
        }

        /*
         * ---------------------------------------------------------
         * 2. Validate token presence
         * ---------------------------------------------------------
         */
        if (token == null || token.isBlank()) {
            log.warn("reCAPTCHA verification failed: Token is missing.");
            throw new RuntimeException("reCAPTCHA token is missing. Please try again.");
        }

        /*
         * ---------------------------------------------------------
         * 3. Prepare Google reCAPTCHA verification form data
         * ---------------------------------------------------------
         */
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("secret", properties.getSecretKey());
        formData.add("response", token);

        RecaptchaResponse response;

        /*
         * ---------------------------------------------------------
         * 4. Call Google reCAPTCHA API securely with robust timeout & error handling
         * ---------------------------------------------------------
         */
        try {
            log.debug("Sending reCAPTCHA verification request to Google for expected action: {}", expectedAction);

            response = webClient
                    .post()
                    .uri(properties.getVerifyUrl())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(RecaptchaResponse.class)
                    .timeout(Duration.ofSeconds(8))
                    .block();

        } catch (WebClientRequestException e) {
            log.error("reCAPTCHA connection/network error while reaching Google API: {}", e.getMessage(), e);
            throw new RuntimeException("reCAPTCHA verification service is temporarily unavailable. Please try again.");
        } catch (Exception e) {
            if (e.getCause() instanceof TimeoutException || (e.getMessage() != null && e.getMessage().contains("Timeout"))) {
                log.error("reCAPTCHA verification timed out while reaching Google API: {}", e.getMessage());
                throw new RuntimeException("reCAPTCHA verification service is temporarily unavailable. Please try again.");
            }
            log.error("reCAPTCHA verification failed unexpectedly: {}", e.getMessage(), e);
            throw new RuntimeException("reCAPTCHA verification service is temporarily unavailable. Please try again.");
        }

        /*
         * ---------------------------------------------------------
         * 5. Validate response structure
         * ---------------------------------------------------------
         */
        if (response == null) {
            log.error("reCAPTCHA response from Google was null.");
            throw new RuntimeException("reCAPTCHA verification failed. Please try again.");
        }

        /*
         * ---------------------------------------------------------
         * 6. Google verification result
         * ---------------------------------------------------------
         */
        if (!response.isSuccess()) {
            log.warn("Google reCAPTCHA verification failed with error codes: {}", response.getErrorCodes());
            throw new RuntimeException("reCAPTCHA verification failed. Please try again.");
        }

        /*
         * ---------------------------------------------------------
         * 7. Validate expected action
         * ---------------------------------------------------------
         */
        if (expectedAction == null || expectedAction.isBlank()) {
            log.error("Expected reCAPTCHA action parameter is missing.");
            throw new RuntimeException("reCAPTCHA verification failed.");
        }

        if (!expectedAction.equalsIgnoreCase(response.getAction())) {
            log.warn("reCAPTCHA action mismatch! Expected: {}, Received: {}", expectedAction, response.getAction());
            throw new RuntimeException("reCAPTCHA verification failed.");
        }

        /*
         * ---------------------------------------------------------
         * 8. Validate score against threshold
         * ---------------------------------------------------------
         */
        Double scoreThreshold = properties.getScoreThreshold() != null ? properties.getScoreThreshold() : 0.5;
        if (response.getScore() == null || response.getScore() < scoreThreshold) {
            log.warn("reCAPTCHA score too low! Threshold: {}, Received score: {}", scoreThreshold, response.getScore());
            throw new RuntimeException("Suspicious request detected. Please try again.");
        }

        /*
         * ---------------------------------------------------------
         * 9. Successful verification
         * ---------------------------------------------------------
         */
        log.info("reCAPTCHA verified successfully. Action: {}, Score: {}", response.getAction(), response.getScore());
    }
}
