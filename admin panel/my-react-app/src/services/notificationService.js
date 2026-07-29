import { apiClient } from "./api";

// =======================================================
// SEND NOTIFICATION TO SELECTED USERS
// =======================================================

export const sendNotification = async (request) => {
  return await apiClient("/admin/notifications/send", {
    method: "POST",
    body: JSON.stringify(request),
  });
};

// =======================================================
// BROADCAST NOTIFICATION TO ALL USERS
// =======================================================

export const broadcastNotification = async (request) => {
  return await apiClient("/admin/notifications/broadcast", {
    method: "POST",
    body: JSON.stringify(request),
  });
};

// =======================================================
// GET NOTIFICATION HISTORY
// =======================================================

export const getNotificationHistory = async (
  keyword = "",
  page = 0,
  size = 10
) => {
  const params = new URLSearchParams({
    keyword,
    page,
    size,
  });

  return await apiClient(
    `/admin/notifications/history?${params.toString()}`
  );
};
// =======================================================
// GET UNREAD COUNT
// =======================================================

export const getUnreadCount = async () => {
  return await apiClient("/admin/notifications/unread-count");
};

// =======================================================
// MARK NOTIFICATION AS READ
// =======================================================

export const markAsRead = async (id) => {
  return await apiClient(`/admin/notifications/${id}/read`, {
    method: "PUT",
  });
};

// =======================================================
// MARK ALL NOTIFICATIONS AS READ
// =======================================================

export const markAllAsRead = async () => {
  return await apiClient("/admin/notifications/read-all", {
    method: "PUT",
  });
};

// =======================================================
// DELETE NOTIFICATION
// =======================================================

export const deleteNotification = async (id) => {
  return await apiClient(`/admin/notifications/${id}`, {
    method: "DELETE",
  });
};