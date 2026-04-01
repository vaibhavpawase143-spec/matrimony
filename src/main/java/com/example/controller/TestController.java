package com.example.controller;

import com.example.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TestController {

    private final EmailService emailService;

    // 🔒 Protected (JWT required)
    @GetMapping("/api/user/test")
    public String userTest() {
        return "User API working ✅";
    }

    // ✅ PUBLIC (no JWT)
    @GetMapping("/api/public/test")
    public String publicTest() {
        return "Project is running successfully 🚀";
    }

    // ✅ PUBLIC RUN CHECK
    @GetMapping("/api/public/run")
    public String run() {
        return "Demo working without JWT 🚀";
    }

    // 📧 EMAIL TEST (IMPORTANT 🔥)
    @GetMapping("/api/public/email")
    public String testEmail() {

        emailService.sendEmail(
                "yourgmail@gmail.com", // 👉 replace with your email
                "Test Email from Spring Boot",
                "Hello Vaibhav 🚀 Email integration is working!"
        );

        return "Email sent successfully ✅";
    }
}