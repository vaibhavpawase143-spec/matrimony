/**
 * URL Security Utilities for Admin Portal
 * Protects against Open Redirects, XSS via javascript:/data: URLs, and unsafe URI manipulations.
 */

const DANGEROUS_PROTOCOLS = ["javascript:", "data:", "vbscript:", "file:"];

/**
 * Validates a redirect URL to ensure it is strictly a safe relative path on the same origin.
 *
 * @param {string} url - Target URL to validate
 * @param {string} defaultPath - Fallback path if invalid (default: "/dashboard")
 * @returns {string} Safe relative path or defaultPath
 */
export const validateSafeRedirect = (url, defaultPath = "/dashboard") => {
  if (!url || typeof url !== "string") {
    return defaultPath;
  }

  const trimmed = url.trim();
  if (!trimmed) {
    return defaultPath;
  }

  // Reject control characters (0x00 - 0x1F, 0x7F)
  if (/[\x00-\x1F\x7F]/.test(trimmed)) {
    return defaultPath;
  }

  // Reject protocol-relative and backslash-based paths
  if (
    trimmed.startsWith("//") ||
    trimmed.startsWith("/\\") ||
    trimmed.startsWith("\\") ||
    trimmed.startsWith("/\t")
  ) {
    return defaultPath;
  }

  // Reject scheme indicators like http:, https:, javascript:, data:
  if (trimmed.includes(":")) {
    return defaultPath;
  }

  // Must start with single forward slash
  if (!trimmed.startsWith("/")) {
    return defaultPath;
  }

  return trimmed;
};

/**
 * Validates whether a URL is safe for navigation, images, or links.
 *
 * @param {string} url - URL string to inspect
 * @returns {boolean} True if safe, false otherwise
 */
export const isSafeUrl = (url) => {
  if (!url || typeof url !== "string") return false;

  const trimmed = url.trim();
  if (!trimmed) return false;

  // Block control characters
  if (/[\x00-\x1F\x7F]/.test(trimmed)) return false;

  const lower = trimmed.toLowerCase();

  // If it's a data URL, only allow safe image MIME types
  if (lower.startsWith("data:")) {
    return /^data:image\/(png|jpeg|jpg|gif|webp|svg\+xml);base64,/i.test(trimmed);
  }

  for (const protocol of DANGEROUS_PROTOCOLS) {
    if (lower.startsWith(protocol)) {
      return false;
    }
  }

  return true;
};

/**
 * Sanitizes a URL, returning a fallback if the URL contains dangerous schemes.
 *
 * @param {string} url - The URL to sanitize
 * @param {string} fallback - Fallback URL to return if unsafe (default: "")
 * @returns {string} Sanitized URL or fallback
 */
export const sanitizeUrl = (url, fallback = "") => {
  if (isSafeUrl(url)) {
    return url.trim();
  }
  return fallback;
};
