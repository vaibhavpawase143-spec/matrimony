import { apiClient } from "./api";

const BASE_URL = "/admin/success-stories";

const adminSuccessStoryService = {
  getAllStories(params = {}) {
    const query = new URLSearchParams();
    if (params.page !== undefined) query.append("page", params.page);
    if (params.size !== undefined) query.append("size", params.size);
    if (params.search) query.append("search", params.search);
    if (params.published !== undefined && params.published !== null && params.published !== "") {
      query.append("published", params.published);
    }
    if (params.sortBy) query.append("sortBy", params.sortBy);
    if (params.sortDir) query.append("sortDir", params.sortDir);

    const queryString = query.toString();
    return apiClient(queryString ? `${BASE_URL}?${queryString}` : BASE_URL);
  },

  getStoryById(id) {
    return apiClient(`${BASE_URL}/${id}`);
  },

  createStory(data) {
    return apiClient(BASE_URL, {
      method: "POST",
      body: JSON.stringify(data),
    });
  },

  updateStory(id, data) {
    return apiClient(`${BASE_URL}/${id}`, {
      method: "PUT",
      body: JSON.stringify(data),
    });
  },

  publishStory(id) {
    return apiClient(`${BASE_URL}/${id}/publish`, {
      method: "PATCH",
    });
  },

  unpublishStory(id) {
    return apiClient(`${BASE_URL}/${id}/unpublish`, {
      method: "PATCH",
    });
  },

  deleteStory(id) {
    return apiClient(`${BASE_URL}/${id}`, {
      method: "DELETE",
    });
  },
};

export default adminSuccessStoryService;
