import { apiClient } from "./api";

// ==========================================
// GET ALL ADMINS
// ==========================================
export const getAllAdmins = async (
  page = 0,
  size = 10,
  search = ""
) => {
  return await apiClient(
    `/admins/manage?page=${page}&size=${size}&search=${encodeURIComponent(search)}`,
    {
      method: "GET",
    }
  );
};
export const updateAdmin = async (id, payload) => {
  return await apiClient(`/admins/${id}/manage`, {
    method: "PUT",
    body: JSON.stringify(payload),
  });
};
// ==========================================
// UPLOAD ADMIN PHOTO
// ==========================================
export const uploadAdminPhoto = async (id, file) => {
  const formData = new FormData();

  formData.append("file", file);

  return await apiClient(`/admins/${id}/upload-photo`, {
    method: "POST",
    body: formData,
    headers: {}, // Don't set Content-Type manually
  });
};
// ==========================================
// GET ADMIN BY ID
// ==========================================
export const getAdminById = async (id) => {
  return await apiClient(`/admins/${id}/manage`, {
    method: "GET",
  });
};
// ==========================================
// GET ALL ROLES
// ==========================================

export const getRoles = async () => {
  return await apiClient("/master/roles", {
    method: "GET",
  });
};
// ==========================================
// ACTIVATE ADMIN
// ==========================================
export const activateAdmin = async (id) => {
  return await apiClient(`/admins/${id}/activate`, {
    method: "PUT",
  });
};

// ==========================================
// DEACTIVATE ADMIN
// ==========================================
export const deactivateAdmin = async (id) => {
  return await apiClient(`/admins/${id}/deactivate`, {
    method: "PUT",
  });
};
export const resetAdminPassword = async (id, data) => {
  return await apiClient(`/admins/${id}/reset-password`, {
    method: "PUT",
    body: JSON.stringify(data),
  });
};
export async function deleteAdmin(id) {
  return apiClient(`/admins/${id}`, {
    method: "DELETE",
  });
}