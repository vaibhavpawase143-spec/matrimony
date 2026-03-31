package com.example.exception;

import com.example.dto.response.ErrorResponse;
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
public class GlobalExceptionHandler {

    // =========================
    // 🔴 RESOURCE NOT FOUND (404)
    // =========================
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(404)
                        .error("NOT_FOUND")
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

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(400)
                        .error("VALIDATION_ERROR")
                        .message(message)
                        .build()
        );
    }

    // =========================
    // 🔴 BAD CREDENTIALS (401)
    // =========================
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(401)
                        .error("UNAUTHORIZED")
                        .message("Invalid email or password")
                        .build()
        );
    }

    // =========================
    // 🔴 JWT EXPIRED (401)
    // =========================
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtExpired(ExpiredJwtException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(401)
                        .error("TOKEN_EXPIRED")
                        .message("JWT token has expired")
                        .build()
        );
    }

    // =========================
    // 🔴 INVALID JWT (401)
    // =========================
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJwt(SignatureException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(401)
                        .error("INVALID_TOKEN")
                        .message("Invalid JWT token")
                        .build()
        );
    }

    // =========================
    // 🔴 ACCESS DENIED (403)
    // =========================
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(403)
                        .error("FORBIDDEN")
                        .message("You are not authorized")
                        .build()
        );
    }

    // =========================
    // 🔴 BAD REQUEST (400)
    // =========================
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(400)
                        .error("BAD_REQUEST")
                        .message(ex.getMessage())
                        .build()
        );
    }

    // =========================
    // 🔴 GLOBAL ERROR (500)
    // =========================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(500)
                        .error("INTERNAL_SERVER_ERROR")
                        .message(ex.getMessage())
                        .build()
        );
    }
}