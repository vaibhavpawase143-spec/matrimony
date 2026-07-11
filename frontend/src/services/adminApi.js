import { apiClient } from "./api";

export const adminApi = {

  getUsers: () =>
    apiClient("/admin/users"),

  searchUsers: (keyword) =>
    apiClient(`/admin/users/search?keyword=${keyword}`),

  activateUser: (id) =>
    apiClient(`/admin/users/${id}/activate`, {
      method: "PUT",
    }),

  deactivateUser: (id) =>
    apiClient(`/admin/users/${id}/deactivate`, {
      method: "PUT",
    }),

  verifyEmail: (id) =>
    apiClient(`/admin/users/${id}/verify-email`, {
      method: "PUT",
    }),

  verifyPhone: (id) =>
    apiClient(`/admin/users/${id}/verify-phone`, {
      method: "PUT",
    }),

  blockUser: (id) =>
    apiClient(`/admin/users/${id}/block`, {
      method: "PUT",
    }),

  unblockUser: (id) =>
    apiClient(`/admin/users/${id}/unblock`, {
      method: "PUT",
    }),

  softDelete: (id) =>
    apiClient(`/admin/users/${id}/soft-delete`, {
      method: "PUT",
    }),

  hardDelete: (id) =>
    apiClient(`/admin/users/${id}`, {
      method: "DELETE",
    }),

  exportUsers: () =>
    apiClient("/admin/users/export"),

  statistics: () =>
    apiClient("/admin/users/stats/count"),
};