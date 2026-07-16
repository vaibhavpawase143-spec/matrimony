package com.example.util;

/**
 * Thread-local context for tracking the current auditing user across the application.
 * Used to automatically populate createdBy, updatedBy, and deletedBy fields in entities.
 * 
 * This should be set in a filter or interceptor at the beginning of each request/operation.
 */
public class AuditContext {

    private static final ThreadLocal<Long> currentUserIdHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> currentUserNameHolder = new ThreadLocal<>();

    /**
     * Sets the current user ID.
     * Call this at the beginning of a transaction/request to track who is making changes.
     * 
     * @param userId User ID making the changes
     */
    public static void setCurrentUserId(Long userId) {
        currentUserIdHolder.set(userId);
    }

    /**
     * Gets the current user ID from context.
     * 
     * @return User ID or null if not set
     */
    public static Long getCurrentUserId() {
        return currentUserIdHolder.get();
    }

    /**
     * Sets the current user name (optional).
     * 
     * @param userName User name for additional logging
     */
    public static void setCurrentUserName(String userName) {
        currentUserNameHolder.set(userName);
    }

    /**
     * Gets the current user name from context.
     * 
     * @return User name or null if not set
     */
    public static String getCurrentUserName() {
        return currentUserNameHolder.get();
    }

    /**
     * Clears all audit context data.
     * Should be called at the end of processing to prevent memory leaks in ThreadPool environments.
     */
    public static void clear() {
        currentUserIdHolder.remove();
        currentUserNameHolder.remove();
    }

    /**
     * Checks if a user ID is set in the current context.
     * 
     * @return true if userId is present, false otherwise
     */
    public static boolean hasCurrentUserId() {
        return currentUserIdHolder.get() != null;
    }
}

