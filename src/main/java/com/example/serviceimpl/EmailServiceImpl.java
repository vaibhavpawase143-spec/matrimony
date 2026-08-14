package com.example.serviceimpl;

import com.example.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    // 🔥 ngrok URL from application.properties

    @Value("${spring.mail.from}")
    private String fromEmail;
    @Value("${app.backend-url}")
    private String backendUrl;

    @Value("${app.frontend-url}")
    private String frontendUrl;
    /**
     * 🔹 Send Verification Email
     */
    @Override
    @Async("emailTaskExecutor")
    public void sendVerificationEmail(String to, String token) {

        // 🔥 DEBUG START
        System.out.println("🔥 EMAIL METHOD CALLED");
        System.out.println("TO: " + to);
        System.out.println("TOKEN: " + token);
        // 🔥 DEBUG END

        String verificationUrl = backendUrl + "/api/auth/verify?token=" + token;

        String subject = "Verify your account";

        Context context = new Context();

        context.setVariable(
                "verificationUrl",
                verificationUrl
        );

        String body =
                templateEngine.process(
                        "email/verification",
                        context
                );

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // ✅ HTML enabled

            mailSender.send(message);

            log.info("Verification email sent to {}", to);
        } catch (MessagingException e) {
            log.error("❌ EMAIL FAILED");
            e.printStackTrace();
        }
    }

    @Override
    @Async("emailTaskExecutor")
    public void sendWelcomeEmail(String to, String firstName) {

        String subject = "Welcome to Gathbandhan Matrimony!";

        Context context = new Context();

        context.setVariable("firstName", firstName);


        String body = templateEngine.process(
                "email/welcome",
                context
        );

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);
            log.info("Welcome email sent to {}", to);

        }catch (MessagingException e) {

            log.error(
                    "Failed to send forgot password email to {}",
                    to,
                    e
            );

        }

    }
    @Override
    @Async("emailTaskExecutor")
    public void sendPremiumReminderEmail(
            String to,
            String firstName,
            int daysRemaining
    ) {

        String subject = "Your Premium Membership Expires in " + daysRemaining + " Day(s)";

        Context context = new Context();

        context.setVariable("firstName", firstName);
        context.setVariable("daysRemaining", daysRemaining);

        // Change this to your renewal page if different
        context.setVariable(
                "renewUrl",
                frontendUrl + "/subscription"
        );

        String body = templateEngine.process(
                "email/premium-reminder",
                context
        );

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true,
                            "UTF-8"
                    );

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);

            log.info(
                    "Premium reminder email sent to {} ({} day(s) remaining)",
                    to,
                    daysRemaining
            );

        } catch (MessagingException e) {

            log.error(
                    "Failed to send premium reminder email to {}",
                    to,
                    e
            );

        }
    }
    @Override
    @Async("emailTaskExecutor")
    public void sendPremiumExpiredEmail(
            String to,
            String firstName
    ) {

        String subject = "Your Premium Membership Has Expired";

        Context context = new Context();

        context.setVariable("firstName", firstName);

        String body = templateEngine.process(
                "email/premium-expired",
                context
        );

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true,
                            "UTF-8"
                    );

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);

            log.info("Premium expired email sent to {}", to);

        } catch (MessagingException e) {

            log.error(
                    "Failed to send premium expired email to {}",
                    to,
                    e
            );

        }
    }
    @Override
    @Async("emailTaskExecutor")
    public void sendForgotPasswordEmail(
            String to,
            String token
    ) {

        String subject = "Reset Your Password";

        String resetUrl =
                frontendUrl +
                        "/reset-password?token=" +
                        token;

        Context context = new Context();

        context.setVariable(
                "resetUrl",
                resetUrl
        );

        String body =
                templateEngine.process(
                        "email/forgot-password",
                        context
                );

        try {

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true,
                            "UTF-8"
                    );

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);

            System.out.println(
                    "✅ PASSWORD RESET EMAIL SENT TO: "
                            + to
            );

        } catch (MessagingException e) {

            e.printStackTrace();

        }

    }
    @Override
    @Async("emailTaskExecutor")
    public void sendPasswordChangedEmail(
            String to,
            String firstName
    ) {

        String subject = "Password Changed Successfully";

        Context context = new Context();

        context.setVariable(
                "firstName",
                firstName
        );

        String body =
                templateEngine.process(
                        "email/password-changed",
                        context
                );

        try {

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true,
                            "UTF-8"
                    );

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);

            System.out.println(
                    "✅ PASSWORD CHANGED EMAIL SENT TO: "
                            + to
            );

        } catch (MessagingException e) {

            System.out.println(
                    "❌ PASSWORD CHANGED EMAIL FAILED"
            );

            e.printStackTrace();

        }

    }
    @Override
    @Async("emailTaskExecutor")
    public void sendAnnouncementEmail(
            String to,
            String firstName,
            String title,
            String message
    ) {

        Context context = new Context();

        context.setVariable("firstName", firstName);
        context.setVariable("title", title);
        context.setVariable("message", message);

        String body = templateEngine.process(
                "email/announcement",
                context
        );

        try {

            MimeMessage mimeMessage =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            mimeMessage,
                            true,
                            "UTF-8"
                    );

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(title);
            helper.setText(body, true);

            mailSender.send(mimeMessage);

            System.out.println(
                    "✅ ANNOUNCEMENT EMAIL SENT TO: " + to
            );

        } catch (MessagingException e) {

            System.out.println(
                    "❌ ANNOUNCEMENT EMAIL FAILED"
            );

            e.printStackTrace();

        }

    }
    /**
     * 🔹 Send Real-time OTP Email
     */
    @Override
    @Async("emailTaskExecutor")
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

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);
            log.info("✅ OTP email ({}) sent to {}", purposeTitle, to);
        } catch (Exception e) {
            log.error("❌ Failed to send OTP email to {}", to, e);
        }
    }

    /**
     * 🔹 Generic Email (optional)
     */
    @Override
    @Async("emailTaskExecutor")
    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);

            System.out.println("✅ EMAIL SENT SUCCESSFULLY TO: " + to);

        } catch (Exception e) {
            System.out.println("❌ EMAIL FAILED");
            e.printStackTrace();
        }
    }
}

