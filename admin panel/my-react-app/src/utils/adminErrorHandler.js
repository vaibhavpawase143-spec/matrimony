import { toast } from "sonner";

/**
 * Extracts a clean, human-readable error message formatted for Admin Portal users.
 */
export function getAdminErrorMessage(error, fallbackMsg = "An unexpected admin error occurred.") {
  if (!error) return fallbackMsg;
  if (typeof error === "string") return error;
  if (error.message) return error.message;
  if (error.error) return error.error;
  return fallbackMsg;
}

/**
 * Displays a styled toast error message specifically for Admin actions.
 */
export function showAdminErrorToast(error, fallbackMsg = "Operation failed.") {
  const msg = getAdminErrorMessage(error, fallbackMsg);
  const status = error?.status;

  toast.error(msg, {
    description: status ? `Status Code: ${status}` : undefined,
    duration: 4000,
  });
}
