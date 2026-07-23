import { apiClient } from "./api";

export const getDashboardStats = async () => {
  const response = await apiClient("/admin/dashboard");

  return response.data;
};