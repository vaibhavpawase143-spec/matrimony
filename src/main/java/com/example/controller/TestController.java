package com.example.controller;

import com.example.service.EmailService;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TestController {

    private final EmailService emailService;
    private final SimpMessagingTemplate messagingTemplate; // 🔥 ADD THIS

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

    // 📧 EMAIL TEST
    @GetMapping("/api/public/email")
    public String testEmail() {

        emailService.sendEmail(
                "yourgmail@gmail.com",
                "Test Email from Spring Boot",
                "Hello Vaibhav 🚀 Email integration is working!"
        );

        return "Email sent successfully ✅";
    }

    // ================= 🔥 WEBSOCKET TEST =================
    @GetMapping("/api/public/ws-test")
    public String testWebSocket() {

        messagingTemplate.convertAndSendToUser(
                "rahul@gmail.com", // 👈 receiver email (must match websocket user)
                "/queue/messages",
                "🔥 Test message from backend"
        );

        return "WebSocket message sent ✅";
    }

    // ================= 🔥 TYPING TEST =================
    @GetMapping("/api/public/ws-typing")
    public String testTyping() {

        messagingTemplate.convertAndSendToUser(
                "rahul@gmail.com",
                "/queue/typing",
                "✍️ Typing from backend"
        );

        return "Typing event sent ✅";
    }
}