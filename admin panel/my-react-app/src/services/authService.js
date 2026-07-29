import { apiClient } from "./api";
export async function loginAdmin({
  email,
  password,
  recaptchaToken,
}) {
  const response = await apiClient("/admins/login", {
    method: "POST",
    body: JSON.stringify({
      email,
      password,
      recaptchaToken,
    }),
  });

const result = response;

if (!result.success) {
  throw new Error(result.message || "Login failed.");
}

const data = result.data;

  localStorage.setItem("adminToken", data.accessToken);
  localStorage.setItem("adminRefreshToken", data.refreshToken);
  localStorage.setItem("admin", JSON.stringify(data.admin));

  return {
    token: data.accessToken,
    refreshToken: data.refreshToken,
    user: data.admin,
  };
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