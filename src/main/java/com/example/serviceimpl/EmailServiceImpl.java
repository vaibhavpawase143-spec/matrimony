package com.example.serviceimpl;

import com.example.provider.EmailProvider;
import com.example.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailProvider emailProvider;
    private final TemplateEngine templateEngine;

    @Value("${app.backend-url}")
    private String backendUrl;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    /**
     * 🔹 Send Verification Email (CRITICAL TRANSACTIONAL)
     */
    @Override
    @Async("criticalEmailExecutor")
    public void sendVerificationEmail(String to, String token) {
        String verificationUrl = backendUrl + "/api/auth/verify?token=" + token;
        String subject = "Verify your account";

        Context context = new Context();
        context.setVariable("verificationUrl", verificationUrl);

        String body = templateEngine.process("email/verification", context);

        try {
            emailProvider.sendCriticalEmail(to, subject, body);
            log.info("Verification email sent to {}", to);
        } catch (Exception e) {
            log.error("❌ Verification email failed to {}", to, e);
        }
    }

    /**
     * 🔹 Send Welcome Email (CRITICAL TRANSACTIONAL)
     */
    @Override
    @Async("criticalEmailExecutor")
    public void sendWelcomeEmail(String to, String firstName) {
        String subject = "Welcome to Gathbandhan Matrimony!";

        Context context = new Context();
        context.setVariable("firstName", firstName);

        String body = templateEngine.process("email/welcome", context);

        try {
            emailProvider.sendCriticalEmail(to, subject, body);
            log.info("Welcome email sent to {}", to);
        } catch (Exception e) {
            log.error("Failed to send welcome email to {}", to, e);
        }
    }

    /**
     * 🔹 Send Premium Reminder Email (BULK EMAIL EXECUTOR)
     */
    @Override
    @Async("bulkEmailExecutor")
    public void sendPremiumReminderEmail(String to, String firstName, int daysRemaining) {
        String subject = "Your Premium Membership Expires in " + daysRemaining + " Day(s)";

        Context context = new Context();
        context.setVariable("firstName", firstName);
        context.setVariable("daysRemaining", daysRemaining);
        context.setVariable("renewUrl", frontendUrl + "/subscription");

        String body = templateEngine.process("email/premium-reminder", context);

        try {
            emailProvider.sendBulkEmail(to, firstName, subject, body);
            log.info("Premium reminder email sent to {} ({} day(s) remaining)", to, daysRemaining);
        } catch (Exception e) {
            log.error("Failed to send premium reminder email to {}", to, e);
        }
    }

    /**
     * 🔹 Send Premium Expired Email (BULK EMAIL EXECUTOR)
     */
    @Override
    @Async("bulkEmailExecutor")
    public void sendPremiumExpiredEmail(String to, String firstName) {
        String subject = "Your Premium Membership Has Expired";

        Context context = new Context();
        context.setVariable("firstName", firstName);

        String body = templateEngine.process("email/premium-expired", context);

        try {
            emailProvider.sendBulkEmail(to, firstName, subject, body);
            log.info("Premium expired email sent to {}", to);
        } catch (Exception e) {
            log.error("Failed to send premium expired email to {}", to, e);
        }
    }

    /**
     * 🔹 Send Forgot Password Email (CRITICAL TRANSACTIONAL)
     */
    @Override
    @Async("criticalEmailExecutor")
    public void sendForgotPasswordEmail(String to, String token) {
        String subject = "Reset Your Password";
        String resetUrl = frontendUrl + "/reset-password?token=" + token;

        Context context = new Context();
        context.setVariable("resetUrl", resetUrl);

        String body = templateEngine.process("email/forgot-password", context);

        try {
            emailProvider.sendCriticalEmail(to, subject, body);
            log.info("✅ PASSWORD RESET EMAIL SENT TO: {}", to);
        } catch (Exception e) {
            log.error("❌ PASSWORD RESET EMAIL FAILED to {}", to, e);
        }
    }

    /**
     * 🔹 Send Password Changed Email (CRITICAL TRANSACTIONAL)
     */
    @Override
    @Async("criticalEmailExecutor")
    public void sendPasswordChangedEmail(String to, String firstName) {
        String subject = "Password Changed Successfully";

        Context context = new Context();
        context.setVariable("firstName", firstName);

        String body = templateEngine.process("email/password-changed", context);

        try {
            emailProvider.sendCriticalEmail(to, subject, body);
            log.info("✅ PASSWORD CHANGED EMAIL SENT TO: {}", to);
        } catch (Exception e) {
            log.error("❌ PASSWORD CHANGED EMAIL FAILED to {}", to, e);
        }
    }

    /**
     * 🔹 Send Announcement Email (BULK EMAIL EXECUTOR)
     */
    @Override
    @Async("bulkEmailExecutor")
    public void sendAnnouncementEmail(String to, String firstName, String title, String message) {
        Context context = new Context();
        context.setVariable("firstName", firstName);
        context.setVariable("title", title);
        context.setVariable("message", message);

        String body = templateEngine.process("email/announcement", context);

        try {
            emailProvider.sendBulkEmail(to, firstName != null ? firstName : "User", title, body);
            log.info("✅ ANNOUNCEMENT EMAIL SENT TO: {}", to);
        } catch (Exception e) {
            log.error("❌ ANNOUNCEMENT EMAIL FAILED to {}", to, e);
            throw new RuntimeException("Announcement email dispatch failed: " + e.getMessage(), e);
        }
    }

    /**
     * 🔹 Send Real-time OTP Email (CRITICAL TRANSACTIONAL)
     */
    @Override
    @Async("criticalEmailExecutor")
    public void sendOTPEmail(String to, String otp, String purpose) {
        String purposeTitle = "Verification Code";
        if ("LOGIN".equalsIgnoreCase(purpose)) {
            purposeTitle = "Login Verification Code";
        } else if ("PASSWORD_RESET".equalsIgnoreCase(purpose)) {
            purposeTitle = "Password Reset Code";
        }

        String subject = purposeTitle + ": " + otp + " - Gathbandhan";

        Context context = new Context();
        context.setVariable("otp", otp);
        context.setVariable("purposeTitle", purposeTitle);

        try {
            String body = templateEngine.process("email/otp", context);
            emailProvider.sendCriticalEmail(to, subject, body);
            log.info("✅ OTP email ({}) sent to {}", purposeTitle, to);
        } catch (Exception e) {
            log.error("❌ Failed to send OTP email to {}", to, e);
        }
    }

    /**
     * 🔹 Generic Email (CRITICAL TRANSACTIONAL BY DEFAULT)
     */
    @Override
    @Async("criticalEmailExecutor")
    public void sendEmail(String to, String subject, String body) {
        try {
            emailProvider.sendCriticalEmail(to, subject, body);
            log.info("✅ EMAIL SENT SUCCESSFULLY TO: {}", to);
        } catch (Exception e) {
            log.error("❌ EMAIL FAILED to {}", to, e);
        }
    }
}
