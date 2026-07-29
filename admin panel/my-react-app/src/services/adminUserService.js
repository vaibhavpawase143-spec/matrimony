import { apiClient } from "./api";

/* ==========================================================
   USER LIST
========================================================== */

export const getUsers = async (
  page = 0,
  size = 10,
  search = "",
  filters = {}
) => {

  let endpoint = `/admin/users?page=${page}&size=${size}`;

  if (search.trim()) {
    endpoint += `&search=${encodeURIComponent(search)}`;
  }

  Object.entries(filters).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== "") {
      endpoint += `&${key}=${encodeURIComponent(value)}`;
    }
  });

  return await apiClient(endpoint);
};

/* ==========================================================
   USER DETAILS
========================================================== */

export const getUserById = async (id) => {
  return await apiClient(`/admin/users/${id}`);
};

/* ==========================================================
   USER ACTIONS
========================================================== */

export const activateUser = async (id) => {
  return await apiClient(`/admin/users/${id}/activate`, {
    method: "PUT",
  });
};

export const deactivateUser = async (id) => {
  return await apiClient(`/admin/users/${id}/deactivate`, {
    method: "PUT",
  });
};

export const blockUser = async (id) => {
  return await apiClient(`/admin/users/${id}/block`, {
    method: "PUT",
  });
};

export const unblockUser = async (id) => {
  return await apiClient(`/admin/users/${id}/unblock`, {
    method: "PUT",
  });
};

export const verifyEmail = async (id) => {
  return await apiClient(`/admin/users/${id}/verify-email`, {
    method: "PUT",
  });
};

export const verifyPhone = async (id) => {
  return await apiClient(`/admin/users/${id}/verify-phone`, {
    method: "PUT",
  });
};

export const restoreUser = async (id) => {
  return await apiClient(`/admin/users/${id}/restore`, {
    method: "PUT",
  });
};

export const softDeleteUser = async (id) => {
  return await apiClient(`/admin/users/${id}/soft-delete`, {
    method: "PUT",
  });
};

export const deleteUser = async (id) => {
  return await apiClient(`/admin/users/${id}`, {
    method: "DELETE",
  });
};

/* ==========================================================
   BULK ACTIONS
========================================================== */

export const bulkActivateUsers = async (userIds) => {
  return await apiClient("/admin/users/bulk/activate", {
    method: "PUT",
    body: JSON.stringify({ userIds }),
  });
};

export const bulkBlockUsers = async (userIds) => {
  return await apiClient("/admin/users/bulk/block", {
    method: "PUT",
    body: JSON.stringify({ userIds }),
  });
};

export const bulkUnblockUsers = async (userIds) => {
  return await apiClient("/admin/users/bulk/unblock", {
    method: "PUT",
    body: JSON.stringify({ userIds }),
  });
};

export const bulkSoftDeleteUsers = async (userIds) => {
  return await apiClient("/admin/users/bulk/soft-delete", {
    method: "PUT",
    body: JSON.stringify({ userIds }),
  });
};

/* ==========================================================
   EXPORT
========================================================== */

export const exportUsersCsv = async () => {
  return await apiClient("/admin/users/export/csv", {
    method: "GET",
  });
};

export const exportUsersExcel = async () => {
  return await apiClient("/admin/users/export/excel", {
    method: "GET",
  });
};