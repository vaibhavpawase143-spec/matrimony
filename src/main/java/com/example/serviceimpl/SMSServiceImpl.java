package com.example.serviceimpl;

import com.example.service.SMSService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SMSServiceImpl implements SMSService {

    @Value("${app.sms.enabled:false}")
    private boolean smsEnabled;

    // 🔥 MSG91 AUTH KEY
    @Value("${msg91.auth.key}")
    private String authKey;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 🔹 Send OTP to phone number using MSG91
     */
    @Override
    public void sendOTP(String phone, String otp) {

        System.out.println("🔥 ENTERED SMS METHOD");
        System.out.println("📱 PHONE: " + phone);
        System.out.println("🔐 OTP: " + otp);

        if (!smsEnabled) {
            System.out.println("📱 [MOCK MODE] OTP: " + otp);
            return;
        }

        try {
            String url = "https://control.msg91.com/api/v5/otp";

            HttpHeaders headers = new HttpHeaders();
            headers.set("authkey", authKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            String body = "{\n" +
                    "  \"mobile\": \"91" + phone + "\",\n" +
                    "  \"otp\": \"" + otp + "\"\n" +
                    "}";

            HttpEntity<String> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, request, String.class);

            System.out.println("✅ MSG91 RESPONSE: " + response.getBody());

        } catch (Exception e) {
            System.out.println("❌ SMS FAILED: " + e.getMessage());
            throw new RuntimeException("Failed to send OTP");
        }
    }

    /**
     * 🔹 Send generic SMS
     */
    @Override
    public void sendSMS(String phone, String message) {
        sendOTP(phone, message);
    }
}