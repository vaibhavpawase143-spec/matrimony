package com.example.serviceimpl;

import com.example.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, String subject, String body) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom("vaibhavpawase143@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

            System.out.println("✅ EMAIL SENT SUCCESSFULLY TO: " + to);

        } catch (Exception e) {
            System.out.println("❌ EMAIL FAILED");
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}