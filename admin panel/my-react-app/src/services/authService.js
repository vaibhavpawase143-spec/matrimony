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

  sessionStorage.setItem("adminToken", data.accessToken);
  sessionStorage.setItem("adminRefreshToken", data.refreshToken);
  sessionStorage.setItem("admin", JSON.stringify(data.admin));

  localStorage.removeItem("adminToken");
  localStorage.removeItem("adminRefreshToken");
  localStorage.removeItem("admin");

  return {
    token: data.accessToken,
    refreshToken: data.refreshToken,
    user: data.admin,
  };
}

export function logoutAdmin() {
  sessionStorage.removeItem("adminToken");
  sessionStorage.removeItem("adminRefreshToken");
  sessionStorage.removeItem("admin");

  localStorage.removeItem("adminToken");
  localStorage.removeItem("adminRefreshToken");
  localStorage.removeItem("admin");
}

export function getAdmin() {
  const admin = sessionStorage.getItem("admin") || localStorage.getItem("admin");
  return admin ? JSON.parse(admin) : null;
}

export function isAuthenticated() {
  return !!(sessionStorage.getItem("adminToken") || localStorage.getItem("adminToken"));
}