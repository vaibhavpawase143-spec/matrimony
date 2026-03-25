package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/user/test")
    public String userTest() {
        return "User API working ✅";
    }

    @GetMapping("/api/public/test")
    public String publicTest() {
        return "Public API working ✅";
    }
}