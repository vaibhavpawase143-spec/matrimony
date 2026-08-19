package com.example.provider;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultEmailProviderImpl implements EmailProvider {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from:no-reply@gathbandhan.com}")
    private String fromEmail;

    @Value("${app.email.bulk-api-key:#{null}}")
    private String bulkApiKey;

    @Value("${app.email.bulk-api-url:https://api.sendgrid.com/v3/mail/send}")
    private String bulkApiUrl;

    @Value("${app.email.provider-name:Gathbandhan-Bulk-Provider}")
    private String providerName;

    @Value("${broadcast.test-mode:false}")
    private boolean testMode;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendCriticalEmail(String to, String subject, String bodyHtml) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(bodyHtml, true);

            mailSender.send(message);
            log.info("[CRITICAL EMAIL SENT] To={} | Subject={}", to, subject);
        } catch (MessagingException e) {
            log.error("[CRITICAL EMAIL FAILED] To={} | Error={}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send critical email: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendBulkEmail(String to, String firstName, String title, String bodyHtml) {
        BulkEmailRecipientRequest req = new BulkEmailRecipientRequest(null, to, firstName);
        sendBatch(List.of(req), title, bodyHtml);
    }

    @Override
    public BulkEmailBatchResponse sendBatch(List<BulkEmailRecipientRequest> recipients, String title, String bodyHtml) {
        if (recipients == null || recipients.isEmpty()) {
            return BulkEmailBatchResponse.builder()
                    .success(true)
                    .acceptedCount(0)
                    .rejectedCount(0)
                    .build();
        }

        String batchId = "BATCH_" + UUID.randomUUID();

        // 1. Production Mode Validation
        if (!testMode && !isRealBulkProviderConfigured()) {
            log.error("[PRODUCTION EMAIL FAIL-FAST] Production mode is active but app.email.bulk-api-key is missing or fake.");
            throw new IllegalStateException(
                    "PRODUCTION CONFIGURATION ERROR: Real bulk email provider API key (app.email.bulk-api-key) is missing or invalid. " +
                    "Production broadcasts for 1,000,000+ users require a real transactional bulk email provider API key. " +
                    "Simulated or missing keys are forbidden in production mode."
            );
        }

        // 2. Test-Mode / Non-Production Simulation Handling
        if (testMode || !isRealBulkProviderConfigured()) {
            log.info("[TEST_ONLY SIMULATED BULK EMAIL PROVIDER] Dispatched batch of {} recipients via {} | BatchID={}",
                    recipients.size(), providerName, batchId);
            return BulkEmailBatchResponse.builder()
                    .providerBatchId(batchId)
                    .success(true)
                    .acceptedCount(recipients.size())
                    .rejectedCount(0)
                    .providerMessage("Accepted by test-mode simulated provider batch handler")
                    .failedUserIds(Collections.emptyList())
                    .build();
        }

        // 3. Real HTTP REST Bulk Email Provider API Execution
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(bulkApiKey);

            List<Map<String, Object>> personalizations = new ArrayList<>();
            for (BulkEmailRecipientRequest req : recipients) {
                Map<String, Object> p = new HashMap<>();
                p.put("to", List.of(Map.of("email", req.getEmail(), "name", req.getFirstName() != null ? req.getFirstName() : "User")));
                personalizations.add(p);
            }

            Map<String, Object> payload = new HashMap<>();
            payload.put("personalizations", personalizations);
            payload.put("from", Map.of("email", fromEmail));
            payload.put("subject", title);
            payload.put("content", List.of(Map.of("type", "text/html", "value", bodyHtml)));

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);
            ResponseEntity<String> response = restTemplate.exchange(bulkApiUrl, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("[REAL BULK EMAIL PROVIDER API SUCCESS] Dispatched {} recipients via REST API {} | BatchID={}",
                        recipients.size(), providerName, batchId);
                return BulkEmailBatchResponse.builder()
                        .providerBatchId(batchId)
                        .success(true)
                        .acceptedCount(recipients.size())
                        .rejectedCount(0)
                        .providerMessage("Successfully accepted by " + providerName)
                        .failedUserIds(Collections.emptyList())
                        .build();
            } else {
                log.error("[REAL BULK EMAIL PROVIDER API REJECTED] HTTP {} | Response={}", response.getStatusCode(), response.getBody());
                List<Long> failedIds = recipients.stream().map(BulkEmailRecipientRequest::getUserId).filter(Objects::nonNull).toList();
                return BulkEmailBatchResponse.builder()
                        .providerBatchId(batchId)
                        .success(false)
                        .acceptedCount(0)
                        .rejectedCount(recipients.size())
                        .providerMessage("HTTP " + response.getStatusCode() + ": " + response.getBody())
                        .failedUserIds(failedIds)
                        .build();
            }
        } catch (Exception e) {
            log.error("[REAL BULK EMAIL PROVIDER API ERROR] Provider HTTP REST request failed: {}", e.getMessage(), e);
            List<Long> failedIds = recipients.stream().map(BulkEmailRecipientRequest::getUserId).filter(Objects::nonNull).toList();
            return BulkEmailBatchResponse.builder()
                    .providerBatchId(batchId)
                    .success(false)
                    .acceptedCount(0)
                    .rejectedCount(recipients.size())
                    .providerMessage("Provider REST API exception: " + e.getMessage())
                    .failedUserIds(failedIds)
                    .build();
        }
    }

    @Override
    public boolean isBulkApiEnabled() {
        return isRealBulkProviderConfigured();
    }

    @Override
    public boolean isRealBulkProviderConfigured() {
        return bulkApiKey != null &&
               !bulkApiKey.isBlank() &&
               !bulkApiKey.equalsIgnoreCase("simulated-bulk-api-key-prod") &&
               !bulkApiKey.equalsIgnoreCase("test-api-key") &&
               !bulkApiKey.equalsIgnoreCase("none");
    }

    @Override
    public String getProviderName() {
        return providerName;
    }
}

