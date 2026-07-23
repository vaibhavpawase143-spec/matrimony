import { apiClient } from "./api";

export async function loginAdmin({ email, password }) {
  const response = await apiClient("/admins/login", {
    method: "POST",
    body: JSON.stringify({
      email,
      password,
    }),
  });

  const result = response.data || response;

  if (!result.accessToken) {
    throw new Error("Access token not received.");
  }

  // Store authentication
  localStorage.setItem("adminToken", result.accessToken);
  localStorage.setItem("adminRefreshToken", result.refreshToken);

  if (result.admin) {
    localStorage.setItem(
      "admin",
      JSON.stringify(result.admin)
    );
  }

  return result;
}

export function logoutAdmin() {
  localStorage.removeItem("adminToken");
  localStorage.removeItem("adminRefreshToken");
  localStorage.removeItem("admin");
}

export function getAdmin() {
  const admin = localStorage.getItem("admin");
  return admin ? JSON.parse(admin) : null;
}

export function isAuthenticated() {
  return !!localStorage.getItem("adminToken");
}