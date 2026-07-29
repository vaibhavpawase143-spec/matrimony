import { apiClient } from "./api";

// ======================================================
// GET ALL SUPPORT TICKETS
// ======================================================

export const getSupportTickets = async () => {
    const response = await apiClient("/admin/support");
    return response.data;
};

// ======================================================
// GET SINGLE SUPPORT TICKET
// ======================================================

export const getSupportTicket = async (ticketNumber) => {
    const response = await apiClient(
        `/admin/support/${ticketNumber}`
    );

    return response.data;
};

// ======================================================
// UPDATE STATUS
// ======================================================

export const updateSupportStatus = async (
    ticketNumber,
    status
) => {

    const response = await apiClient(
        `/admin/support/${ticketNumber}/status`,
        {
            method: "PUT",
            body: JSON.stringify({
                status,
            }),
        }
    );

    return response.data;
};

// ======================================================
// REPLY TICKET
// ======================================================

export const replySupportTicket = async (
    ticketNumber,
    adminReply
) => {

    const response = await apiClient(
        `/admin/support/${ticketNumber}/reply`,
        {
            method: "PUT",
            body: JSON.stringify({
                reply: adminReply,
            }),
        }
    );

    return response.data;
};