package com.example.controller.auth;

import com.example.dto.request.ResendVerificationRequestDTO;
import com.example.dto.request.UserLoginRequestDTO;
import com.example.dto.request.UserRegisterRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.LoginResponse;
import com.example.service.RefreshTokenService;
import com.example.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // 🔐 safer than "*"
public class AuthController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    // ================= STEP 1: SEND VERIFICATION =================
    @PostMapping("/send-verification")
    public ApiResponse<String> sendVerification(@RequestParam String email) {

        userService.sendVerification(email);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Verification email sent successfully")
                .data(null)
                .build();
    }

    // ================= STEP 2: VERIFY EMAIL =================
    @GetMapping("/verify")
    public ResponseEntity<Void> verifyEmail(
            @RequestParam String token,
            HttpServletResponse response
    ) {

        try {

            userService.verifyEmail(token);

            response.sendRedirect(
                    "http://localhost:3000/email-verified"
            );

        } catch (Exception e) {

            e.printStackTrace();

        }

        return ResponseEntity.ok().build();
    }
    // ================= DEV: BYPASS EMAIL VERIFICATION =================
    @PostMapping("/bypass-verification")
    public ApiResponse<String> bypassVerification(@RequestParam String email) {
        
        userService.bypassEmailVerification(email);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Email verification bypassed for development")
                .data(null)
                .build();
    }

    // ================= STEP 3: REGISTER =================
    @PostMapping("/register")
    public ApiResponse<String> register(@Valid @RequestBody UserRegisterRequestDTO request) {

        userService.register(request);

        return ApiResponse.<String>builder()
                .success(true)
                .message("User registered successfully")
                .data(null)
                .build();
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody UserLoginRequestDTO request) {
        System.out.println("========== LOGIN CONTROLLER ==========");
        LoginResponse loginResponse = userService.loginWithProfile(
                request.getEmail(),
                request.getPassword()
        );

        return ApiResponse.<LoginResponse>builder()
                .success(true)
                .message("Login successful")
                .data(loginResponse)
                .build();
    }

    // ================= RESEND VERIFICATION =================
    @PostMapping("/resend-verification")
    public ApiResponse<String> resendVerification(
            @Valid @RequestBody ResendVerificationRequestDTO request
    ) {

        userService.resendVerification(request.getEmail());

        return ApiResponse.<String>builder()
                .success(true)
                .message("Verification email resent successfully")
                .data(null)
                .build();
    }

    // ================= MOBILE OTP =================
    @PostMapping("/send-otp")
    public ApiResponse<String> sendOTP(@RequestParam String phone) {
        
        String otp = userService.sendOTPToPhone(phone);

        return ApiResponse.<String>builder()
                .success(true)
                .message("OTP sent successfully")
                .data("OTP: " + otp) // Show OTP in development
                .build();
    }

    @PostMapping("/verify-otp")
    public ApiResponse<String> verifyOTP(@RequestParam String phone, @RequestParam String otp) {
        
        userService.verifyPhoneOTP(phone, otp);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Phone verified successfully")
                .data(null)
                .build();
    }

    @PostMapping("/bypass-phone-verification")
    public ApiResponse<String> bypassPhoneVerification(@RequestParam String phone) {
        
        userService.bypassPhoneVerification(phone);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Phone verification bypassed for development")
                .data(null)
                .build();
    }


    @PostMapping("/logout")
    public ApiResponse<String> logout(
            Principal principal
    ) {

        userService.logout(
                principal.getName()
        );

        return ApiResponse.<String>builder()
                .success(true)
                .message("Logout successful")
                .build();
    }
}