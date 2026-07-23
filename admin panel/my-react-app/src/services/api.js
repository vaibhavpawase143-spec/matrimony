const API_BASE_URL = "/api";

export const apiClient = async (endpoint, options = {}) => {
  const adminToken = localStorage.getItem("adminToken");

  const defaultOptions = {
    headers: {
      "Content-Type": "application/json",
      ...(adminToken && {
        Authorization: `Bearer ${adminToken}`,
      }),
      ...options.headers,
    },
    ...options,
  };

  const response = await fetch(`${API_BASE_URL}${endpoint}`, defaultOptions);

  if (!response.ok) {
    let error = {};

    try {
      error = await response.json();
    } catch {
      // Ignore JSON parse errors for empty/non-JSON responses
    }

    throw new Error(
      error.message ||
      error.error ||
      `Request failed (${response.status})`
    );
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