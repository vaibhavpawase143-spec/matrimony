import { BACKEND_BASE_URL } from "../utils/imageUtils";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "/api";
export const IMAGE_BASE_URL = BACKEND_BASE_URL;

/**
 * Custom Error Class for Admin Portal API calls
 */
export class AdminApiError extends Error {
  constructor(message, status = 500, serverMessage = null) {
    super(message);
    this.name = "AdminApiError";
    this.status = status;
    this.serverMessage = serverMessage;
  }
}

export const apiClient = async (endpoint, options = {}) => {
  const adminToken = sessionStorage.getItem("adminToken") || localStorage.getItem("adminToken");
  const isFormData = options.body instanceof FormData;

  const defaultOptions = {
    ...options,
    headers: {
      ...(isFormData ? {} : { "Content-Type": "application/json" }),
      ...(adminToken && {
        Authorization: `Bearer ${adminToken}`,
      }),
      ...options.headers,
    },
  };

  let response;
  try {
    response = await fetch(`${API_BASE_URL}${endpoint}`, defaultOptions);
  } catch (netErr) {
    console.error("[Admin API Network Error]:", netErr);
    throw new AdminApiError(
      "Backend Service Unavailable: Unable to connect to server backend API. Please ensure the backend server is running.",
      0,
      netErr?.message
    );
  }

  if (!response.ok) {
    let errorData = {};

    try {
      errorData = await response.json();
    } catch {
      // Non-JSON response body
    }

    const serverMsg = errorData.message || errorData.error || errorData.details;

    let adminMessage = "";
    switch (response.status) {
      case 401:
        adminMessage = serverMsg || "Admin Session Expired: Security token is invalid or expired. Please log in again.";
        sessionStorage.removeItem("adminToken");
        sessionStorage.removeItem("adminRefreshToken");
        sessionStorage.removeItem("admin");
        localStorage.removeItem("adminToken");
        localStorage.removeItem("adminRefreshToken");
        localStorage.removeItem("admin");
        break;
      case 403:
        adminMessage = serverMsg || "Access Denied: You do not have privilege to perform this admin action.";
        break;
      case 404:
        adminMessage = serverMsg || "Resource Not Found: Requested item or endpoint could not be found.";
        break;
      case 400:
      case 422:
        adminMessage = serverMsg || "Invalid Request: Submitted parameters failed validation.";
        break;
      case 409:
        adminMessage = serverMsg || "Data Conflict: Record has already been modified or exists.";
        break;
      case 429:
        adminMessage = serverMsg || "Rate Limit Exceeded: Too many requests sent. Please try again shortly.";
        break;
      case 500:
      case 502:
      case 503:
      case 504:
        adminMessage = serverMsg || `Server Error (${response.status}): An internal server error occurred in backend.`;
        break;
      default:
        adminMessage = serverMsg || `Admin Request Failed (${response.status}).`;
    }

    throw new AdminApiError(adminMessage, response.status, serverMsg);
  }

  if (response.status === 204) {
    return null;
  }

  const text = await response.text();
  if (!text) {
    return null;
  }

  try {
    return JSON.parse(text);
  } catch {
    return text;
  }
};