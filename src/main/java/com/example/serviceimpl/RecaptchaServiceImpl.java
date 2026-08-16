package com.example.serviceimpl;

import com.example.config.RecaptchaProperties;
import com.example.dto.response.RecaptchaResponse;
import com.example.service.RecaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RecaptchaServiceImpl implements RecaptchaService {

    private final RecaptchaProperties properties;

    private final WebClient webClient = WebClient.builder()
            .build();

    @Override
    public void verify(String token, String expectedAction) {

        /*
         * ---------------------------------------------------------
         * 1. reCAPTCHA disabled
         * ---------------------------------------------------------
         */
        if (!properties.isEnabled()) {
            System.out.println("reCAPTCHA disabled for development");
            return;
        }

        /*
         * ---------------------------------------------------------
         * 2. Validate token
         * ---------------------------------------------------------
         */
        if (token == null || token.isBlank()) {
            throw new RuntimeException("reCAPTCHA token is missing.");
        }

        /*
         * ---------------------------------------------------------
         * 3. Development/test bypass
         * ---------------------------------------------------------
         */
        if ("development".equalsIgnoreCase(token)
                || "test".equalsIgnoreCase(token)) {

            System.out.println("Development reCAPTCHA token bypassed");
            return;
        }

        /*
         * ---------------------------------------------------------
         * 4. Prepare Google reCAPTCHA verification request
         * ---------------------------------------------------------
         */
        MultiValueMap<String, String> formData =
                new LinkedMultiValueMap<>();

        formData.add("secret", properties.getSecretKey());
        formData.add("response", token);

        RecaptchaResponse response;

        /*
         * ---------------------------------------------------------
         * 5. Call Google reCAPTCHA API
         *
         * IMPORTANT:
         * This request is made ONLY when verify() is called.
         * There is NO @PostConstruct request during application
         * startup.
         * ---------------------------------------------------------
         */
        try {

            response = webClient
                    .post()
                    .uri(properties.getVerifyUrl())
                    .bodyValue(formData)
                    .retrieve()
                    .bodyToMono(RecaptchaResponse.class)
                    .timeout(Duration.ofSeconds(3))
                    .block();

        } catch (Exception e) {

            System.err.println(
                    "reCAPTCHA verification failed: "
                            + e.getMessage()
            );

            /*
             * Do NOT allow authentication to continue when
             * production reCAPTCHA verification cannot be completed.
             */
            throw new RuntimeException(
                    "Unable to verify reCAPTCHA. Please try again."
            );
        }

        /*
         * ---------------------------------------------------------
         * 6. Validate response
         * ---------------------------------------------------------
         */
        if (response == null) {
            throw new RuntimeException(
                    "Unable to verify reCAPTCHA."
            );
        }

        /*
         * ---------------------------------------------------------
         * 7. Google verification result
         * ---------------------------------------------------------
         */
        if (!response.isSuccess()) {

            throw new RuntimeException(
                    "reCAPTCHA verification failed. Errors: "
                            + response.getErrorCodes()
            );
        }

        /*
         * ---------------------------------------------------------
         * 8. Validate expected action
         * ---------------------------------------------------------
         */
        if (expectedAction == null
                || expectedAction.isBlank()) {

            throw new RuntimeException(
                    "Expected reCAPTCHA action is missing."
            );
        }

        if (!expectedAction.equals(response.getAction())) {

            throw new RuntimeException(
                    "Invalid reCAPTCHA action."
            );
        }

        /*
         * ---------------------------------------------------------
         * 9. Validate score
         * ---------------------------------------------------------
         */
        if (response.getScore() == null
                || response.getScore()
                < properties.getScoreThreshold()) {

            throw new RuntimeException(
                    "Suspicious request detected. Please try again."
            );
        }

        /*
         * ---------------------------------------------------------
         * 10. Successful verification
         * ---------------------------------------------------------
         */
        System.out.println("===== RECAPTCHA VERIFIED =====");
        System.out.println("Success : " + response.isSuccess());
        System.out.println("Score   : " + response.getScore());
        System.out.println("Action  : " + response.getAction());
    }
}

