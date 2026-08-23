import { isSafeUrl, sanitizeUrl } from "./urlSecurity";

/**
 * Utility for resolving backend-hosted profile images and avatars safely.
 * 
 * Supports:
 * - Relative URLs ("/uploads/user21.jpg") -> "http://localhost:9090/uploads/user21.jpg"
 * - Absolute URLs ("https://example.com/photo.jpg") -> preserved as is
 * - Fallbacks for null, undefined, empty strings, or 404 image load errors
 */

const getBackendBaseUrl = () => {
  if (import.meta.env.VITE_BACKEND_URL) {
    return import.meta.env.VITE_BACKEND_URL.replace(/\/$/, "");
  }
  return "http://localhost:9090";
};

export const BACKEND_BASE_URL = getBackendBaseUrl();

/**
 * Returns default SVG/UI avatar for fallback
 */
export const getDefaultAvatar = (name = "User") => {
  const safeName = encodeURIComponent(name || "User");
  return `https://ui-avatars.com/api/?name=${safeName}&background=7C3AED&color=fff&size=200`;
};

/**
 * Resolves raw image path/URL to fully-qualified web URL or default avatar fallback.
 */
export const getImageUrl = (url, fallbackName = "User") => {
  if (!url || typeof url !== "string" || !url.trim() || !isSafeUrl(url)) {
    return getDefaultAvatar(fallbackName);
  }

  const cleanUrl = url.trim();

  // If already absolute URL, blob, or safe data image URL
  if (
    cleanUrl.startsWith("http://") ||
    cleanUrl.startsWith("https://") ||
    cleanUrl.startsWith("blob:") ||
    cleanUrl.startsWith("data:image/")
  ) {
    return cleanUrl;
  }

  // Relative path (e.g. "/uploads/user21.jpg")
  const path = cleanUrl.startsWith("/") ? cleanUrl : `/${cleanUrl}`;
  return `${BACKEND_BASE_URL}${path}`;
};

/**
 * onError event handler to prevent infinite loops and show default avatar on broken images
 */
export const handleImageError = (e, fallbackName = "User") => {
  if (!e || !e.target) return;
  e.target.onerror = null; // Break potential error loop
  e.target.src = getDefaultAvatar(fallbackName);
};
