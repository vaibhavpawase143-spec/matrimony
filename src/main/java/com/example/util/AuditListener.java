package com.example.util;

import com.example.model.base.Auditable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JPA Entity Listener for automatic audit field management.
 * Automatically populates createdBy and updatedBy fields from AuditContext.
 * 
 * Apply this listener to Auditable entities or use @EntityListeners annotation.
 */
public class AuditListener {

    private static final Logger logger = LoggerFactory.getLogger(AuditListener.class);

    /**
     * Called before a new Auditable entity is persisted.
     * Sets createdBy from current audit context if available.
     * 
     * @param entity The entity being persisted
     */
    @PrePersist
    public void onPrePersist(Auditable entity) {
        Long currentUserId = AuditContext.getCurrentUserId();
        if (currentUserId != null) {
            entity.setAuditUser(currentUserId);
        } else {
            logger.debug("Warning: Creating {} without audit user context", entity.getClass().getSimpleName());
        }
    }

    /**
     * Called before an existing Auditable entity is updated.
     * Sets updatedBy from current audit context if available.
     * 
     * @param entity The entity being updated
     */
    @PreUpdate
    public void onPreUpdate(Auditable entity) {
        Long currentUserId = AuditContext.getCurrentUserId();
        if (currentUserId != null) {
            entity.setUpdatedBy(currentUserId);
        } else {
            logger.debug("Warning: Updating {} without audit user context", entity.getClass().getSimpleName());
        }
    }
}

