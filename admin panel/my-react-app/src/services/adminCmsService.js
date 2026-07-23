import { apiClient } from "./api";

const BASE_URL = "/admin/cms";

const adminCmsService = {
  /**
   * Get all CMS pages
   */
  getAllPages() {
    return apiClient(BASE_URL);
  },

  /**
   * Get CMS page by ID
   */
  getPageById(id) {
    return apiClient(`${BASE_URL}/${id}`);
  },

  /**
   * Create CMS page
   */
  createPage(data) {
    return apiClient(BASE_URL, {
      method: "POST",
      body: JSON.stringify(data),
    });
  },

  /**
   * Update CMS page
   */
  updatePage(id, data) {
    return apiClient(`${BASE_URL}/${id}`, {
      method: "PUT",
      body: JSON.stringify(data),
    });
  },

  /**
   * Publish CMS page
   */
  publishPage(id) {
    return apiClient(`${BASE_URL}/${id}/publish`, {
      method: "PATCH",
    });
  },

  /**
   * Unpublish CMS page
   */
  unpublishPage(id) {
    return apiClient(`${BASE_URL}/${id}/unpublish`, {
      method: "PATCH",
    });
  },

  /**
   * Soft Delete CMS page
   */
  deletePage(id) {
    return apiClient(`${BASE_URL}/${id}`, {
      method: "DELETE",
    });
  },
};

export default adminCmsService;