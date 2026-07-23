import { apiClient } from "./api";

// ================= GET ALL PAYMENTS =================

export const getPayments = async (params = {}) => {
  const query = new URLSearchParams(params).toString();

  const response = await apiClient(
    `/admin/payments${query ? `?${query}` : ""}`
  );

  return response.data;
};

// ================= GET PAYMENT BY ID =================

export const getPaymentById = async (id) => {
  const response = await apiClient(`/admin/payments/${id}`);

  return response.data;
};

// ================= PAYMENT STATISTICS =================

export const getPaymentStatistics = async () => {
  const response = await apiClient("/admin/payments/statistics");

  return response.data;
};

// ================= MARK SUCCESS =================

export const markPaymentSuccess = async (id) => {
  const response = await apiClient(
    `/admin/payments/${id}/mark-success`,
    {
      method: "PUT",
    }
  );

  return response.data;
};

// ================= MARK FAILED =================

export const markPaymentFailed = async (id, reason) => {
  const response = await apiClient(
    `/admin/payments/${id}/mark-failed?reason=${encodeURIComponent(reason)}`,
    {
      method: "PUT",
    }
  );

  return response.data;
};

// ================= REFUND PAYMENT =================

export const refundPayment = async (id, refundAmount, refundReason) => {
  const response = await apiClient(
    `/admin/payments/${id}/refund?refundAmount=${refundAmount}&refundReason=${encodeURIComponent(refundReason)}`,
    {
      method: "PUT",
    }
  );

  return response.data;
};