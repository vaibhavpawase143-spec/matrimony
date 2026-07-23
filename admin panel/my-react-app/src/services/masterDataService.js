import { apiClient } from "./api";

export const getReligions = async () => {
  const response = await apiClient("/master/religions");
  return response.data;
};

export const createReligion = async (data) => {
  const response = await apiClient("/master/religions", {
    method: "POST",
    body: JSON.stringify(data),
  });
  return response.data;
};

export const updateReligion = async (id, data) => {
  const response = await apiClient(`/master/religions/${id}`, {
    method: "PUT",
    body: JSON.stringify(data),
  });
  return response.data;
};

export const deactivateReligion = async (id) => {
  return apiClient(`/master/religions/${id}`, {
    method: "DELETE",
  });
};

export const restoreReligion = async (id) => {
  return apiClient(`/master/religions/${id}/restore`, {
    method: "PATCH",
  });
};