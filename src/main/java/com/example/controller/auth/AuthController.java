package com.example.controller.auth;

import com.example.model.User;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // ✅ Register
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    // ✅ Login
    @PostMapping("/login")
    public User login(@RequestParam String email,
                      @RequestParam String password) {
        return userService.login(email, password);
    }
}