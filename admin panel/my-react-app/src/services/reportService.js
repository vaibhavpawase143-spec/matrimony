import { apiClient } from "./api";

/* ==========================================================
   GET ALL REPORTS
========================================================== */

export const getReports = async (
  page = 0,
  size = 10,
  search = "",
  status = ""
) => {
  const params = new URLSearchParams();

  params.append("page", page);
  params.append("size", size);
  params.append("sortBy", "createdAt");
  params.append("direction", "DESC");

  if (search.trim()) {
    params.append("search", search.trim());
  }

  if (status) {
    params.append("status", status);
  }

  const response = await apiClient(
    `/admin/reports?${params.toString()}`
  );

  return response.data;
};

/* ==========================================================
   GET REPORT BY ID
========================================================== */

export const getReportById = async (id) => {
  const response = await apiClient(
    `/admin/reports/${id}`
  );

  return response.data;
};

/* ==========================================================
   MARK REPORT UNDER REVIEW
========================================================== */

export const reviewReport = async (id) => {
  const response = await apiClient(
    `/admin/reports/${id}/review`,
    {
      method: "PUT",
    }
  );

  return response.data;
};

/* ==========================================================
   APPROVE REPORT
========================================================== */

export const approveReport = async (id) => {
  const response = await apiClient(
    `/admin/reports/${id}/approve`,
    {
      method: "PUT",
    }
  );

  return response.data;
};

/* ==========================================================
   REJECT REPORT
========================================================== */

export const rejectReport = async (id) => {
  const response = await apiClient(
    `/admin/reports/${id}/reject`,
    {
      method: "PUT",
    }
  );

  return response.data;
};

/* ==========================================================
   REPORT STATISTICS
========================================================== */

export const getReportStatistics = async () => {
  const response = await apiClient(
    "/admin/reports/statistics"
  );

  return response.data;
};