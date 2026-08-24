/**
 * URL Security Utilities
 * Protects against Open Redirects, XSS via javascript:/data: URLs, and malformed URI manipulations.
 */

// Characters that could indicate protocol-relative URLs or sneaky redirection attempts
const DANGEROUS_PROTOCOLS = ["javascript:", "data:", "vbscript:", "file:"];

/**
 * Validates a redirect URL to ensure it is strictly a safe relative path on the same origin.
 * Rejects:
 * - Protocol-relative URLs: //evil.com, ///evil.com
 * - Backslash evasion: /\evil.com, /\\evil.com, \evil.com
 * - Dangerous schemes: javascript:alert(1), data:text/html,...
 * - External URLs: https://evil.com, http://evil.com
 * - Control characters & null bytes
 *
 * @param {string} url - Target URL to validate
 * @param {string} defaultPath - Fallback path if invalid (default: "/home")
 * @returns {string} Safe relative path or defaultPath
 */
export const validateSafeRedirect = (url, defaultPath = "/home") => {
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
 * Validates whether a URL is safe for navigation or linking.
 * Allows legitimate http:, https:, blob:, mailto:, tel:, and same-origin relative paths.
 * Blocks javascript:, data:text/html, vbscript:, etc.
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

/**
 * Resolves profile image URLs safely across environments
 * Handles absolute URLs, relative URLs, uploads, data/blob URLs, and fallbacks.
 *
 * @param {string} image - Image path or URL
 * @param {string} fallback - Fallback image asset
 * @returns {string} Fully qualified or safe relative image URL
 */
export const resolveImageUrl = (image, fallback = "") => {
  if (!image || typeof image !== "string") return fallback;
  const trimmed = image.trim();
  if (!trimmed || trimmed === "null" || trimmed === "undefined") return fallback;

  if (
    trimmed.startsWith("http://") ||
    trimmed.startsWith("https://") ||
    trimmed.startsWith("blob:") ||
    trimmed.startsWith("data:")
  ) {
    return trimmed;
  }

  const backendUrl = (
    import.meta.env.VITE_BACKEND_URL ||
    import.meta.env.VITE_API_BASE_URL ||
    ""
  ).replace(/\/+$/, "");

  if (trimmed.startsWith("/")) {
    return backendUrl ? `${backendUrl}${trimmed}` : trimmed;
  }

  if (trimmed.startsWith("uploads/")) {
    return backendUrl ? `${backendUrl}/${trimmed}` : `/${trimmed}`;
  }

  return backendUrl ? `${backendUrl}/uploads/${trimmed}` : `/uploads/${trimmed}`;
};
