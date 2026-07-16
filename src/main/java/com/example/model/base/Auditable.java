package com.example.model.base;

import com.example.util.AuditListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Base class for all entities requiring audit trail tracking.
 * Provides production-level audit fields for comprehensive tracking of data changes.
 * 
 * AUDIT FIELDS:
 * - createdAt: Timestamp when entity was created
 * - createdBy: User ID who created the entity
 * - updatedAt: Timestamp when entity was last updated
 * - updatedBy: User ID who last updated the entity
 * - isDeleted: Soft delete flag
 * - deletedAt: Timestamp when entity was soft deleted
 * - deletedBy: User ID who deleted the entity
 * - version: Optimistic locking version for concurrent updates
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditListener.class)
public abstract class Auditable {

    /**
     * Timestamp when the entity was created.
     * Set automatically in onCreate() via @PrePersist.
     * Immutable once created.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * User ID of the person who created this entity.
     * Immutable once created.
     */
    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    /**
     * Timestamp when the entity was last updated.
     * Updated automatically in onUpdate() via @PreUpdate.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * User ID of the person who last updated this entity.
     * Should be set before calling save/update operations.
     */
    @Column(name = "updated_by")
    private Long updatedBy;

    /**
     * Soft delete flag.
     * When true, entity should not be included in normal queries.
     * Default: false
     */
    @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
    private Boolean isDeleted = false;
    /**
     * Timestamp when the entity was soft deleted.
     * Null if entity is not deleted.
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    /**
     * User ID of the person who deleted this entity.
     * Null if entity is not deleted.
     */
    @Column(name = "deleted_by")
    private Long deletedBy;

    /**
     * Reason for deletion (optional, for audit trail).
     * Useful for compliance and tracking deletions.
     */
    @Column(name = "deletion_reason", length = 500)
    private String deletionReason;

    /**
     * Version field for optimistic locking.
     * Prevents concurrent update conflicts.
     */
    @Version
    @Column(name = "version")
    private Long version = 0L;

    /**
     * Called when a new entity is persisted.
     * Sets createdAt and updatedAt to current time.
     * Should not update createdBy/updatedBy here as they need to be set via service layer.
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.isDeleted == null) {
            this.isDeleted = false;
        }
        if (this.version == null) {
            this.version = 0L;
        }
    }

    /**
     * Called when an entity is updated.
     * Updates the updatedAt timestamp.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Marks this entity as deleted (soft delete).
     * Sets isDeleted flag, deletedAt timestamp, and deletedBy user.
     * 
     * @param userId User ID performing the deletion
     * @param reason Optional reason for deletion
     */
    public void markDeleted(Long userId, String reason) {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = userId;
        this.deletionReason = reason;
        this.updatedBy = userId;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Restores a soft-deleted entity.
     * Clears deletion flags and timestamps.
     * 
     * @param userId User ID performing the restoration
     */
    public void restore(Long userId) {
        this.isDeleted = false;
        this.deletedAt = null;
        this.deletedBy = null;
        this.deletionReason = null;
        this.updatedBy = userId;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Checks if this entity is soft deleted.
     * 
     * @return true if entity is deleted, false otherwise
     */
    public boolean isDeleted() {
        return Boolean.TRUE.equals(this.isDeleted);
    }

    /**
     * Sets the audit fields for tracking who made changes.
     * Should be called from service layer before persisting changes.
     * 
     * @param userId User ID making the change
     */
    public void setAuditUser(Long userId) {
        if (userId == null) {
            return;
        }
        if (this.createdBy == null) {
            this.createdBy = userId;
        }
        this.updatedBy = userId;
    }

    /**
     * Gets full audit information as a formatted string.
     * Useful for logging and debugging.
     * 
     * @return Formatted audit trail string
     */
    public String getAuditTrail() {
        StringBuilder trail = new StringBuilder();
        trail.append("Created: ").append(createdAt).append(" by User #").append(createdBy);
        if (updatedAt != null && !updatedAt.equals(createdAt)) {
            trail.append(" | Updated: ").append(updatedAt).append(" by User #").append(updatedBy);
        }
        if (isDeleted) {
            trail.append(" | Deleted: ").append(deletedAt).append(" by User #").append(deletedBy);
            if (deletionReason != null) {
                trail.append(" Reason: ").append(deletionReason);
            }
        }
        return trail.toString();
    }
}

