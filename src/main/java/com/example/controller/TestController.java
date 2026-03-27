package com.example.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class TestController {

    // 🔒 Protected (JWT required)
    @GetMapping("/api/user/test")
    public String userTest() {
        return "User API working ✅";
    }

    // ✅ PUBLIC (already allowed in your security)
    @GetMapping("/api/public/test")
    public String publicTest() {
        return "Project is running successfully 🚀";
    }

    // 🔥 IMPORTANT: Use this instead of /test
    @GetMapping("/api/public/run")
    public String run() {
        return "Demo working without JWT 🚀";
    }


}