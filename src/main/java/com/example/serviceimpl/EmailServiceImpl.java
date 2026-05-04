package com.example.serviceimpl;

import com.example.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    // 🔥 ngrok URL from application.properties
    @Value("${app.base-url}")
    private String baseUrl;

    /**
     * 🔹 Send Verification Email
     */
    @Override
    public void sendVerificationEmail(String to, String token) {

        // 🔥 DEBUG START
        System.out.println("🔥 EMAIL METHOD CALLED");
        System.out.println("TO: " + to);
        System.out.println("TOKEN: " + token);
        // 🔥 DEBUG END

        String verificationUrl = baseUrl + "/api/auth/verify?token=" + token;

        String subject = "Verify your account";

        String body = "<div style='font-family:Arial,sans-serif;'>"
                + "<h2>Email Verification</h2>"
                + "<p>Click the button below to verify your account:</p>"
                + "<a href='" + verificationUrl + "' "
                + "style='display:inline-block;padding:12px 20px;background:#28a745;color:white;"
                + "text-decoration:none;border-radius:5px;'>Verify Account</a>"
                + "<p style='margin-top:20px;'>Or copy this link:</p>"
                + "<p>" + verificationUrl + "</p>"
                + "<p>If you didn’t request this, you can ignore this email.</p>"
                + "</div>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("vaibhavpawase143@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // ✅ HTML enabled

            mailSender.send(message);

            System.out.println("✅ VERIFICATION EMAIL SENT TO: " + to);

        } catch (MessagingException e) {
            System.out.println("❌ EMAIL FAILED");
            e.printStackTrace();
        }
    }

    @Override
    public void sendWelcomeEmail(String to, String firstName) {

        String subject = "Welcome to Gathbandhan Matrimony!";

        String htmlBody = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Welcome to Gathbandhan</title>
                <style>
                    body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }
                    .container { max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 0 20px rgba(0,0,0,0.1); }
                    .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 50px 30px; text-align: center; }
                    .content { padding: 40px 30px; color: #333; line-height: 1.6; }
                    .button { display: inline-block; padding: 15px 30px; background: linear-gradient(135deg, #ff6b6b 0%, #feca57 100%); color: white; text-decoration: none; border-radius: 8px; font-weight: bold; margin: 20px 0; box-shadow: 0 4px 15px rgba(255, 107, 107, 0.3); transition: transform 0.2s; }
                    .button:hover { transform: translateY(-2px); }
                    .footer { background-color: #f8f9fa; padding: 20px 30px; color: #666; font-size: 14px; text-align: center; border-top: 1px solid #dee2e6; }
                    .highlight { background-color: #e8f5e8; border-left: 4px solid #28a745; padding: 15px; margin: 20px 0; border-radius: 0 5px 5px 0; }
                    .stats { display: flex; justify-content: space-around; margin: 30px 0; text-align: center; }
                    .stat-item { flex: 1; padding: 20px; }
                    .stat-number { font-size: 24px; font-weight: bold; color: #667eea; }
                    .stat-label { color: #666; font-size: 14px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🎉 Welcome " + firstName + "!</h1>
                        <p>Your journey to finding true love begins here</p>
                    </div>

                    <div class="content">
                        <h2>Congratulations! Your email has been verified.</h2>
                        <p>Thank you for joining <strong>Gathbandhan Matrimony</strong> - India's most trusted matrimony platform. We're excited to help you find your perfect life partner!</p>

                        <div class="highlight">
                            <h3>✨ What's Next?</h3>
                            <ul>
                                <li>Complete your detailed profile</li>
                                <li>Upload your best photos</li>
                                <li>Set your partner preferences</li>
                                <li>Start connecting with matches</li>
                            </ul>
                        </div>

                        <div style="text-align: center;">
                            <a href=\"" + baseUrl + "/profile" + "\" class="button">🚀 Complete Your Profile</a>
                        </div>

                        <div class="stats">
                            <div class="stat-item">
                                <div class="stat-number">10M+</div>
                                <div class="stat-label">Happy Couples</div>
                            </div>
                            <div class="stat-item">
                                <div class="stat-number">50M+</div>
                                <div class="stat-label">Active Users</div>
                            </div>
                            <div class="stat-item">
                                <div class="stat-number">25+</div>
                                <div class="stat-label">Years Experience</div>
                            </div>
                        </div>

                        <h3>💡 Pro Tips for Success:</h3>
                        <ul>
                            <li><strong>Profile Photo:</strong> Use a clear, recent photo where you're smiling</li>
                            <li><strong>About Me:</strong> Write honestly about yourself and what you're looking for</li>
                            <li><strong>Partner Preferences:</strong> Be specific about what matters most to you</li>
                            <li><strong>Stay Active:</strong> Regular logins increase your visibility</li>
                        </ul>

                        <p>Remember, finding the right partner takes time. Be patient and stay positive!</p>

                        <p>If you have any questions, our support team is here to help.</p>

                        <p>Best wishes,<br>
                        <strong>The Gathbandhan Team</strong></p>
                    </div>

                    <div class="footer">
                        <p><strong>Gathbandhan Matrimony</strong></p>
                        <p>Find your perfect match with confidence</p>
                        <p>© 2024 Gathbandhan. All rights reserved.</p>
                        <p>
                            <a href="#" style="color: #667eea; margin: 0 10px;">Privacy Policy</a> |
                            <a href="#" style="color: #667eea; margin: 0 10px;">Terms of Service</a> |
                            <a href="#" style="color: #667eea; margin: 0 10px;">Contact Support</a>
                        </p>
                    </div>
                </div>
            </body>
            </html>
        """;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("vaibhavpawase143@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // HTML enabled

            mailSender.send(message);

            System.out.println("✅ WELCOME EMAIL SENT TO: " + to);

        } catch (MessagingException e) {
            System.out.println("❌ WELCOME EMAIL FAILED");
            e.printStackTrace();
        }
    }

    /**
     * 🔹 Generic Email (optional)
     */
    @Override
    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("vaibhavpawase143@gmail.com");
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
