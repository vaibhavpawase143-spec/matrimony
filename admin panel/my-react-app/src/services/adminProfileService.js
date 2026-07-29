import { apiClient } from "./api";

// ==========================================
// GET ADMIN PROFILE
// ==========================================
export const getAdminProfile = async () => {
  const response = await apiClient("/admin/profile", {
    method: "GET",
  });

  return response.data;
};

// ==========================================
// UPDATE ADMIN PROFILE
// ==========================================
export const updateAdminProfile = async (profileData) => {
  const response = await apiClient("/admin/profile", {
    method: "PUT",
    body: JSON.stringify(profileData),
  });

  return response.data;
};

// ==========================================
// CHANGE PASSWORD
// ==========================================
export const changePassword = async (passwordData) => {
  const response = await apiClient("/admin/profile/change-password", {
    method: "PUT",
    body: JSON.stringify(passwordData),
  });

  return response.data;
};