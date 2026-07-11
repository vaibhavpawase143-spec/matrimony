package com.example.exception;

import com.example.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // =========================
    // 🔴 RESOURCE NOT FOUND (404)
    // =========================
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {

        log.warn("Resource not found: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(404)
                        .error("NOT_FOUND")
                        .errorCode("ERR_404")
                        .message(ex.getMessage())
                        .build()
        );
    }

    // =========================
    // 🔴 VALIDATION ERROR (400)
    // =========================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("Validation failed: {}", message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(400)
                        .error("VALIDATION_ERROR")
                        .errorCode("ERR_400_VALIDATION")
                        .message(message)
                        .build()
        );
    }

    // =========================
    // 🔴 BAD CREDENTIALS (401)
    // =========================
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {

        log.warn("Bad credentials attempt");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(401)
                        .error("UNAUTHORIZED")
                        .errorCode("ERR_401_AUTH")
                        .message("Invalid email or password")
                        .build()
        );
    }

    // =========================
    // 🔴 JWT EXPIRED (401)
    // =========================
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtExpired(ExpiredJwtException ex) {

        log.warn("JWT expired: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(401)
                        .error("TOKEN_EXPIRED")
                        .errorCode("ERR_401_EXPIRED")
                        .message("JWT token has expired")
                        .build()
        );
    }

    // =========================
    // 🔴 INVALID JWT (401)
    // =========================
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJwt(SignatureException ex) {

        log.warn("Invalid JWT signature");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(401)
                        .error("INVALID_TOKEN")
                        .errorCode("ERR_401_INVALID")
                        .message("Invalid JWT token")
                        .build()
        );
    }

    // =========================
    // 🔴 ACCESS DENIED (403)
    // =========================
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {

        log.warn("Access denied: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(403)
                        .error("FORBIDDEN")
                        .errorCode("ERR_403")
                        .message("You are not authorized")
                        .build()
        );
    }

    // =========================
    // 🔴 BAD REQUEST (400)
    // =========================
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {

        log.warn("Bad request: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(400)
                        .error("BAD_REQUEST")
                        .errorCode("ERR_400")
                        .message(ex.getMessage())
                        .build()
        );
    }

    // =========================
    // 🔴 RUNTIME EXCEPTION (400)
    // =========================
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {

        log.error("Runtime exception: ", ex);
        
        String message = ex.getMessage();
        String errorCode = "ERR_RUNTIME";
        
        // Handle common runtime exceptions with better messages
        if (message != null) {
            if (message.contains("User not found")) {
                message = "User not found. Please check your credentials or register for a new account.";
                errorCode = "ERR_USER_NOT_FOUND";
            } else if (message.contains("Invalid password")) {
                message = "Invalid password. Please check your credentials and try again.";
                errorCode = "ERR_INVALID_PASSWORD";
            } else if (message.contains("Email not verified")) {
                message = "Email not verified. Please check your inbox and verify your email before logging in.";
                errorCode = "ERR_EMAIL_NOT_VERIFIED";
            } else if (message.contains("Phone not verified")) {
                message = "Phone not verified. Please verify your phone number before logging in.";
                errorCode = "ERR_PHONE_NOT_VERIFIED";
            } else if (message.contains("Profile not found")) {
                message = "Profile not found. Please create your profile to continue.";
                errorCode = "ERR_PROFILE_NOT_FOUND";
            } else if (message.contains("Profile already exists")) {
                message = "Profile already exists. You can update your existing profile instead.";
                errorCode = "ERR_PROFILE_EXISTS";
            } else if (message.contains("Email already exists")) {
                message = "Email already registered. Please use a different email or try logging in.";
                errorCode = "ERR_EMAIL_EXISTS";
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(400)
                        .error("BAD_REQUEST")
                        .errorCode(errorCode)
                        .message(message)
                        .build()
        );
    }

    // =========================
    // 🔴 DATABASE CONSTRAINT VIOLATION (409)
    // =========================
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(org.springframework.dao.DataIntegrityViolationException ex) {
        
        log.warn("Database constraint violation: {}", ex.getMessage());
        
        String message = "A database constraint was violated";
        String errorCode = "ERR_CONSTRAINT_VIOLATION";
        
        // Check for specific constraint violations
        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("ukdu5v5sr43g5bfnji4vb8hg5s3") || 
                ex.getMessage().contains("phone") && ex.getMessage().contains("already exists")) {
                message = "Phone number already registered. Please use a different phone number or try logging in.";
                errorCode = "ERR_PHONE_EXISTS";
            } else if (ex.getMessage().contains("email") && ex.getMessage().contains("already exists")) {
                message = "Email already registered. Please use a different email or try logging in.";
                errorCode = "ERR_EMAIL_EXISTS";
            }
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(409)
                        .error("CONFLICT")
                        .errorCode(errorCode)
                        .message(message)
                        .build()
        );
    }

    // =========================
    // 🔴 GLOBAL ERROR (500)
    // =========================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {

        log.error("Unexpected error: ", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(500)
                        .error("INTERNAL_SERVER_ERROR")
                        .errorCode("ERR_500")
                        .message("Something went wrong. Please try again later")
                        .build()
        );
    }
    @ExceptionHandler(org.springframework.web.multipart.MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleFileSizeExceeded(Exception ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(400)
                        .error("FILE_TOO_LARGE")
                        .errorCode("ERR_FILE_SIZE")
                        .message("File size exceeded limit")
                        .build()
        );
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(
            BadRequestException ex) {

        log.warn("Bad Request: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(400)
                        .error("BAD_REQUEST")
                        .errorCode("ERR_BAD_REQUEST")
                        .message(ex.getMessage())
                        .build()
        );
    }
}