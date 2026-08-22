package com.example.exception;

import com.example.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // =========================
    // 🔴 RATE LIMIT EXCEEDED (429)
    // =========================
    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleRateLimitExceeded(RateLimitExceededException ex, HttpServletRequest request) {
        log.warn("Rate limit exceeded at {} (Action: {}): {}", request.getRequestURI(), ex.getAction(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .header("Retry-After", String.valueOf(ex.getRetryAfterSeconds()))
                .body(
                        ErrorResponse.builder()
                                .success(false)
                                .timestamp(LocalDateTime.now())
                                .status(429)
                                .error("TOO_MANY_REQUESTS")
                                .errorCode("RATE_LIMIT_EXCEEDED")
                                .code("RATE_LIMIT_EXCEEDED")
                                .message(ex.getMessage() != null ? ex.getMessage() : "Too many requests. Please try again later.")
                                .path(request.getRequestURI())
                                .build()
                );
    }

    // =========================
    // 🔴 RESOURCE NOT FOUND (404)
    // =========================
    @ExceptionHandler({ResourceNotFoundException.class, org.springframework.web.servlet.resource.NoResourceFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(Exception ex, HttpServletRequest request) {
        log.warn("Resource not found at {}: {}", request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResponse.builder()
                        .success(false)
                        .timestamp(LocalDateTime.now())
                        .status(404)
                        .error("NOT_FOUND")
                        .errorCode("RESOURCE_NOT_FOUND")
                        .code("RESOURCE_NOT_FOUND")
                        .message(ex.getMessage() != null ? ex.getMessage() : "Requested resource was not found.")
                        .path(request.getRequestURI())
                        .build()
        );
    }

    // =========================
    // 🔴 VALIDATION ERROR (400)
    // =========================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> {
            fieldErrors.put(err.getField(), err.getDefaultMessage());
        });

        log.warn("Validation failed for {}: {}", request.getRequestURI(), fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .success(false)
                        .timestamp(LocalDateTime.now())
                        .status(400)
                        .error("VALIDATION_ERROR")
                        .errorCode("VALIDATION_ERROR")
                        .code("VALIDATION_ERROR")
                        .message("Please correct the highlighted fields.")
                        .fieldErrors(fieldErrors)
                        .path(request.getRequestURI())
                        .build()
        );
    }

    // =========================
    // 🔴 CONSTRAINT VIOLATION (400)
    // =========================
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            String field = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
            fieldErrors.put(field, violation.getMessage());
        });

        log.warn("Constraint violation at {}: {}", request.getRequestURI(), fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .success(false)
                        .timestamp(LocalDateTime.now())
                        .status(400)
                        .error("VALIDATION_ERROR")
                        .errorCode("CONSTRAINT_VIOLATION")
                        .code("VALIDATION_ERROR")
                        .message("Validation failed for request parameter(s).")
                        .fieldErrors(fieldErrors)
                        .path(request.getRequestURI())
                        .build()
        );
    }

    // =========================
    // 🔴 MALFORMED JSON / NOT READABLE (400)
    // =========================
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(org.springframework.http.converter.HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.warn("Malformed JSON request body at {}: {}", request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .success(false)
                        .timestamp(LocalDateTime.now())
                        .status(400)
                        .error("MALFORMED_REQUEST")
                        .errorCode("MALFORMED_JSON")
                        .code("MALFORMED_JSON")
                        .message("Malformed JSON request body or invalid field format.")
                        .path(request.getRequestURI())
                        .build()
        );
    }

    // =========================
    // 🔴 UNSUPPORTED MEDIA TYPE (415)
    // =========================
    @ExceptionHandler(org.springframework.web.HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMediaTypeNotSupported(org.springframework.web.HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        log.warn("Unsupported media type at {}: {}", request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(
                ErrorResponse.builder()
                        .success(false)
                        .timestamp(LocalDateTime.now())
                        .status(415)
                        .error("UNSUPPORTED_MEDIA_TYPE")
                        .errorCode("UNSUPPORTED_MEDIA_TYPE")
                        .code("UNSUPPORTED_MEDIA_TYPE")
                        .message("The requested content type is not supported.")
                        .path(request.getRequestURI())
                        .build()
        );
    }

    // =========================
    // 🔴 METHOD NOT ALLOWED (405)
    // =========================
    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(org.springframework.web.HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        log.warn("HTTP method not supported at {}: {}", request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(
                ErrorResponse.builder()
                        .success(false)
                        .timestamp(LocalDateTime.now())
                        .status(405)
                        .error("METHOD_NOT_ALLOWED")
                        .errorCode("METHOD_NOT_ALLOWED")
                        .code("METHOD_NOT_ALLOWED")
                        .message("HTTP method " + request.getMethod() + " is not supported for this endpoint.")
                        .path(request.getRequestURI())
                        .build()
        );
    }

    // =========================
    // 🔴 TYPE MISMATCH / INVALID ID (400)
    // =========================
    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        log.warn("Parameter type mismatch at {}: parameter={}, value={}", request.getRequestURI(), ex.getName(), ex.getValue());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .success(false)
                        .timestamp(LocalDateTime.now())
                        .status(400)
                        .error("INVALID_PARAMETER")
                        .errorCode("TYPE_MISMATCH")
                        .code("TYPE_MISMATCH")
                        .message("Invalid parameter format for '" + ex.getName() + "'.")
                        .path(request.getRequestURI())
                        .build()
        );
    }

    // =========================
    // 🔴 BAD CREDENTIALS (401)
    // =========================
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        log.warn("Bad credentials attempt at {}", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ErrorResponse.builder()
                        .success(false)
                        .timestamp(LocalDateTime.now())
                        .status(401)
                        .error("UNAUTHORIZED")
                        .errorCode("INVALID_CREDENTIALS")
                        .code("INVALID_CREDENTIALS")
                        .message("Invalid email or password.")
                        .path(request.getRequestURI())
                        .build()
        );
    }

    // =========================
    // 🔴 JWT EXPIRED (401)
    // =========================
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtExpired(ExpiredJwtException ex, HttpServletRequest request) {
        log.warn("JWT expired at {}", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ErrorResponse.builder()
                        .success(false)
                        .timestamp(LocalDateTime.now())
                        .status(401)
                        .error("TOKEN_EXPIRED")
                        .errorCode("TOKEN_EXPIRED")
                        .code("TOKEN_EXPIRED")
                        .message("Your session has expired. Please log in again.")
                        .path(request.getRequestURI())
                        .build()
        );
    }

    // =========================
    // 🔴 INVALID JWT (401)
    // =========================
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJwt(SignatureException ex, HttpServletRequest request) {
        log.warn("Invalid JWT signature at {}", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ErrorResponse.builder()
                        .success(false)
                        .timestamp(LocalDateTime.now())
                        .status(401)
                        .error("INVALID_TOKEN")
                        .errorCode("INVALID_TOKEN")
                        .code("INVALID_TOKEN")
                        .message("Your session is invalid. Please log in again.")
                        .path(request.getRequestURI())
                        .build()
        );
    }

    // =========================
    // 🔴 ACCESS DENIED (403)
    // =========================
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        log.warn("Access denied at {}: {}", request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                ErrorResponse.builder()
                        .success(false)
                        .timestamp(LocalDateTime.now())
                        .status(403)
                        .error("FORBIDDEN")
                        .errorCode("ACCESS_DENIED")
                        .code("ACCESS_DENIED")
                        .message("You don't have permission to perform this action.")
                        .path(request.getRequestURI())
                        .build()
        );
    }

    // =========================
    // 🔴 PREMIUM REQUIRED (403)
    // =========================
    @ExceptionHandler(PremiumRequiredException.class)
    public ResponseEntity<ErrorResponse> handlePremiumRequired(PremiumRequiredException ex, HttpServletRequest request) {
        log.warn("Premium required at {}: {}", request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                ErrorResponse.builder()
                        .success(false)
                        .timestamp(LocalDateTime.now())
                        .status(403)
                        .error("PREMIUM_REQUIRED")
                        .errorCode("PREMIUM_REQUIRED")
                        .code("PREMIUM_REQUIRED")
                        .message(ex.getMessage() != null ? ex.getMessage() : "A premium membership is required to access this feature.")
                        .path(request.getRequestURI())
                        .build()
        );
    }

    // =========================
    // 🔴 BAD REQUEST (400)
    // =========================
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("Bad request at {}: {}", request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .success(false)
                        .timestamp(LocalDateTime.now())
                        .status(400)
                        .error("BAD_REQUEST")
                        .errorCode("INVALID_INPUT")
                        .code("INVALID_INPUT")
                        .message(ex.getMessage() != null ? ex.getMessage() : "Invalid input provided.")
                        .path(request.getRequestURI())
                        .build()
        );
    }

    // =========================
    // 🔴 FILE SIZE EXCEEDED (400)
    // =========================
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleFileSizeExceeded(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        log.warn("File upload size exceeded at {}", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .success(false)
                        .timestamp(LocalDateTime.now())
                        .status(400)
                        .error("FILE_TOO_LARGE")
                        .errorCode("FILE_TOO_LARGE")
                        .code("FILE_TOO_LARGE")
                        .message("File size is too large. Maximum allowed size is 10 MB.")
                        .path(request.getRequestURI())
                        .build()
        );
    }

    // =========================
    // 🔴 CUSTOM BAD REQUEST EXCEPTION (400)
    // =========================
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex, HttpServletRequest request) {
        log.warn("Bad Request Exception at {}: {}", request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .success(false)
                        .timestamp(LocalDateTime.now())
                        .status(400)
                        .error("BAD_REQUEST")
                        .errorCode("INVALID_INPUT")
                        .code("INVALID_INPUT")
                        .message(ex.getMessage() != null ? ex.getMessage() : "Invalid request parameters.")
                        .path(request.getRequestURI())
                        .build()
        );
    }

    // =========================
    // 🔴 RUNTIME EXCEPTION (400)
    // =========================
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex, HttpServletRequest request) {
        log.error("Runtime exception at {}: ", request.getRequestURI(), ex);

        String message = ex.getMessage();
        String errorCode = "ERR_RUNTIME";

        if (message != null) {
            if (message.contains("User not found")) {
                message = "We couldn't find your account. Please check your details or sign up.";
                errorCode = "RESOURCE_NOT_FOUND";
            } else if (message.contains("Invalid password")) {
                message = "The password you entered is incorrect.";
                errorCode = "INVALID_CREDENTIALS";
            } else if (message.contains("Email not verified")) {
                message = "Email not verified. Please check your inbox and verify your email before logging in.";
                errorCode = "EMAIL_NOT_VERIFIED";
            } else if (message.contains("Phone not verified")) {
                message = "Phone not verified. Please verify your phone number before logging in.";
                errorCode = "PHONE_NOT_VERIFIED";
            } else if (message.contains("Profile not found")) {
                message = "Please complete your profile to continue.";
                errorCode = "RESOURCE_NOT_FOUND";
            } else if (message.contains("Profile already exists")) {
                message = "Your profile already exists. You can edit it anytime.";
                errorCode = "DUPLICATE_RESOURCE";
            } else if (message.contains("Email already exists")) {
                message = "This email is already registered. Please log in or use another email address.";
                errorCode = "DUPLICATE_RESOURCE";
            }
        } else {
            message = "An unexpected error occurred.";
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .success(false)
                        .timestamp(LocalDateTime.now())
                        .status(400)
                        .error("BAD_REQUEST")
                        .errorCode(errorCode)
                        .code(errorCode)
                        .message(message)
                        .path(request.getRequestURI())
                        .build()
        );
    }

    // =========================
    // 🔴 DATABASE CONSTRAINT VIOLATION (409)
    // =========================
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(org.springframework.dao.DataIntegrityViolationException ex, HttpServletRequest request) {
        log.warn("Database constraint violation at {}: {}", request.getRequestURI(), ex.getMessage());

        String message = "Unable to complete operation because a conflicting record exists or is currently in use.";
        String errorCode = "DUPLICATE_RESOURCE";

        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("phone") && ex.getMessage().contains("already exists")) {
                message = "This phone number is already registered. Please log in or use another phone number.";
                errorCode = "DUPLICATE_RESOURCE";
            } else if (ex.getMessage().contains("email") && ex.getMessage().contains("already exists")) {
                message = "This email is already registered. Please log in or use another email address.";
                errorCode = "DUPLICATE_RESOURCE";
            }
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ErrorResponse.builder()
                        .success(false)
                        .timestamp(LocalDateTime.now())
                        .status(409)
                        .error("CONFLICT")
                        .errorCode(errorCode)
                        .code(errorCode)
                        .message(message)
                        .path(request.getRequestURI())
                        .build()
        );
    }

    // =========================
    // 🔴 GLOBAL UNCAUGHT ERROR (500)
    // =========================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex, HttpServletRequest request) {
        log.error("Unexpected internal server error at {}: ", request.getRequestURI(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ErrorResponse.builder()
                        .success(false)
                        .timestamp(LocalDateTime.now())
                        .status(500)
                        .error("INTERNAL_SERVER_ERROR")
                        .errorCode("INTERNAL_SERVER_ERROR")
                        .code("INTERNAL_SERVER_ERROR")
                        .message("An unexpected server error occurred. Please try again later.")
                        .path(request.getRequestURI())
                        .build()
        );
    }
}