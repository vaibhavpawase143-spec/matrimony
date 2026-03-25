package com.example.controller.auth;

import com.example.dto.request.UserLoginRequestDTO;
import com.example.dto.response.LoginResponse;
import com.example.model.User;
import com.example.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    // =========================
    // ✅ REGISTER (PUBLIC)
    // =========================
    @PostMapping("/register")
    public String register(@RequestBody User user) {

        userService.register(user);   // ✅ save only

        return "User registered successfully";   // ✅ DO NOT return entity
    }

    // =========================
    // 🔐 LOGIN (JWT)
    // =========================
    @PostMapping("/login")
    public LoginResponse login(@RequestBody UserLoginRequestDTO request) {

        String token = userService.loginAndGenerateToken(
                request.getEmail(),
                request.getPassword()
        );

        return new LoginResponse(token);   // ✅ correct
    }
}