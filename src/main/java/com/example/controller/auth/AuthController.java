package com.example.controller.auth;

import com.example.dto.request.UserLoginRequestDTO;
import com.example.dto.request.UserRegisterRequestDTO;
import com.example.dto.response.LoginResponse;
import com.example.dto.response.MessageResponse;
import com.example.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    // =========================
    // ✅ REGISTER (PUBLIC API)
    // =========================
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(
            @Valid @RequestBody UserRegisterRequestDTO request
    ) {

        userService.register(request);

        return ResponseEntity.ok(
                new MessageResponse("User registered successfully")
        );
    }

    // =========================
    // 🔐 LOGIN (PUBLIC API)
    // =========================
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody UserLoginRequestDTO request
    ) {

        String token = userService.loginAndGenerateToken(
                request.getEmail(),
                request.getPassword()
        );

        return ResponseEntity.ok(
                new LoginResponse(token)
        );
    }
}