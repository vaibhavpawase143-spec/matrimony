package com.example.util;

/**
 * Utility for sanitizing log entries and audit records against Log Injection (CRLF injection),
 * control characters, and unescaped input tampering.
 */
public final class LogSanitizer {

    private LogSanitizer() {
        // Utility class
    }

    /**
     * Sanitizes user-provided or client-controlled values to prevent CRLF log injection,
     * header injection, and control character tampering.
     */
    public static String sanitize(String input) {
        if (input == null) {
            return null;
        }
        // Replace CR, LF, null bytes, and non-printable control characters with spaces
        return input.replaceAll("[\\r\\n\\x00-\\x1F\\x7F]", " ").trim();
    }

    /**
     * Sanitizes and truncates input to maximum length safely.
     */
    public static String sanitizeAndTruncate(String input, int maxLength) {
        if (input == null) {
            return null;
        }
        String sanitized = sanitize(input);
        return sanitized.length() <= maxLength ? sanitized : sanitized.substring(0, maxLength);
    }
}
