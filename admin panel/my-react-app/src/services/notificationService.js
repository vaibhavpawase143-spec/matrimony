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
// GET BROADCAST LIFECYCLE NOTIFICATIONS FOR ADMIN BELL
// =======================================================

export const getBroadcastLifecycleNotifications = async (page = 0, size = 10) => {
  const params = new URLSearchParams({ page, size });
  return await apiClient(
    `/admin/notifications/broadcast-lifecycle?${params.toString()}`
  );
};

export const getBroadcastLifecycleUnreadCount = async () => {
  return await apiClient("/admin/notifications/broadcast-lifecycle/unread-count");
};

export const markAllBroadcastLifecycleAsRead = async () => {
  return await apiClient("/admin/notifications/broadcast-lifecycle/read-all", {
    method: "PUT",
  });
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

// =======================================================
// GET ACTIVE BROADCAST JOB
// =======================================================

export const getActiveBroadcastJob = async () => {
  return await apiClient("/admin/notifications/broadcast/active");
};

export const getBroadcastJobById = async (jobId) => {
  return await apiClient(`/admin/notifications/broadcast/${jobId}`);
};

export const resumeBroadcastJob = async (jobId) => {
  return await apiClient(`/admin/notifications/broadcast/${jobId}/resume`, {
    method: "POST",
  });
};

export const cancelBroadcastJob = async (jobId) => {
  return await apiClient(`/admin/notifications/broadcast/${jobId}/cancel`, {
    method: "POST",
  });
};

// =======================================================
// GET BROADCAST HISTORY
// =======================================================

export const getBroadcastHistory = async (page = 0, size = 10) => {
  const params = new URLSearchParams({ page, size });
  return await apiClient(
    `/admin/notifications/broadcast/history?${params.toString()}`
  );
};

// =======================================================
// GET BROADCAST RECIPIENT DETAILS
// =======================================================

export const getBroadcastRecipients = async (
  broadcastJobId,
  search = "",
  appStatus = "",
  emailStatus = "",
  page = 0,
  size = 50
) => {
  const params = new URLSearchParams();
  if (search) params.append("search", search);
  if (appStatus) params.append("appStatus", appStatus);
  if (emailStatus) params.append("emailStatus", emailStatus);
  params.append("page", page);
  params.append("size", size);

  return await apiClient(
    `/admin/notifications/broadcast/${broadcastJobId}/recipients?${params.toString()}`
  );
};