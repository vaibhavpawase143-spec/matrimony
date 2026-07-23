import { apiClient } from "./api";

const BASE_URL = "/admin/faqs";

const adminFaqService = {
  getAllFaqs() {
    return apiClient(BASE_URL);
  },

  getFaqById(id) {
    return apiClient(`${BASE_URL}/${id}`);
  },

  createFaq(data) {
    return apiClient(BASE_URL, {
      method: "POST",
      body: JSON.stringify(data),
    });
  },

  updateFaq(id, data) {
    return apiClient(`${BASE_URL}/${id}`, {
      method: "PUT",
      body: JSON.stringify(data),
    });
  },

  publishFaq(id) {
    return apiClient(`${BASE_URL}/${id}/publish`, {
      method: "PATCH",
    });
  },

  unpublishFaq(id) {
    return apiClient(`${BASE_URL}/${id}/unpublish`, {
      method: "PATCH",
    });
  },

  deleteFaq(id) {
    return apiClient(`${BASE_URL}/${id}`, {
      method: "DELETE",
    });
  },
};

export default adminFaqService;