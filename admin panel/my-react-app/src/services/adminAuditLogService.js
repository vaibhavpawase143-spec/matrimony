import { apiClient } from "./api";

const BASE_URL = "/admin/audit-logs";

const buildQuery = (params = {}) => {
    const searchParams = new URLSearchParams();

    Object.entries(params).forEach(([key, value]) => {
        if (
            value !== undefined &&
            value !== null &&
            value !== ""
        ) {
            searchParams.append(key, value);
        }
    });

    return searchParams.toString()
        ? `?${searchParams.toString()}`
        : "";
};

const adminAuditLogService = {

    getAuditLogs(params = {}) {
        return apiClient(
            `${BASE_URL}${buildQuery(params)}`
        );
    }

};

export default adminAuditLogService;