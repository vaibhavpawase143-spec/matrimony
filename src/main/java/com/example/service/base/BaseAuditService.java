package com.example.service.base;

import com.example.model.base.Auditable;
import com.example.util.AuditContext;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Base service class providing common audit operations for all entities extending Auditable.
 * Provides methods for:
 * - Soft deletion with audit trail
 * - Entity restoration
 * - Safe updates with audit tracking
 * 
 * @param <E> Entity type extending Auditable
 * @param <ID> Primary key type
 */
public abstract class BaseAuditService<E extends Auditable, ID> {

    protected final JpaRepository<E, ID> repository;

    public BaseAuditService(JpaRepository<E, ID> repository) {
        this.repository = repository;
    }

    /**
     * Saves an entity with automatic audit field population.
     * Ensures createdBy field is set from AuditContext if this is a new entity.
     * 
     * @param entity Entity to save
     * @return Saved entity with audit fields populated
     */
    public E saveWithAudit(E entity) {
        Long currentUserId = AuditContext.getCurrentUserId();
        entity.setAuditUser(currentUserId);
        return repository.save(entity);
    }

    /**
     * Soft deletes an entity by marking it as deleted.
     * Sets deletedAt, deletedBy, and optional deletion reason from AuditContext.
     * 
     * @param id Entity ID to soft delete
     * @param reason Optional deletion reason for audit trail
     * @return Optional containing the soft-deleted entity if found
     */
    public Optional<E> softDelete(ID id, String reason) {
        Optional<E> entity = repository.findById(id);
        if (entity.isPresent()) {
            Long currentUserId = AuditContext.getCurrentUserId();
            entity.get().markDeleted(currentUserId, reason);
            repository.save(entity.get());
        }
        return entity;
    }

    /**
     * Soft deletes an entity without providing a deletion reason.
     * 
     * @param id Entity ID to soft delete
     * @return Optional containing the soft-deleted entity if found
     */
    public Optional<E> softDelete(ID id) {
        return softDelete(id, null);
    }

    /**
     * Restores a previously soft-deleted entity.
     * Clears deletion flags and timestamps, sets updatedBy from AuditContext.
     * 
     * @param id Entity ID to restore
     * @return Optional containing the restored entity if found
     */
    public Optional<E> restore(ID id) {
        Optional<E> entity = repository.findById(id);
        if (entity.isPresent()) {
            Long currentUserId = AuditContext.getCurrentUserId();
            entity.get().restore(currentUserId);
            repository.save(entity.get());
        }
        return entity;
    }

    /**
     * Gets the audit trail information for an entity.
     * Returns formatted string showing creation, updates, and deletion info.
     * 
     * @param id Entity ID
     * @return Optional containing formatted audit trail string
     */
    public Optional<String> getAuditTrail(ID id) {
        return repository.findById(id).map(Auditable::getAuditTrail);
    }

    /**
     * Checks if an entity is soft deleted.
     * 
     * @param id Entity ID
     * @return true if entity exists and is marked deleted, false otherwise
     */
    public boolean isDeleted(ID id) {
        Optional<E> entity = repository.findById(id);
        return entity.map(Auditable::isDeleted).orElse(false);
    }

    /**
     * Base repository access for custom queries.
     * Subclasses can override this or use repository directly.
     * 
     * @return The underlying repository
     */
    protected JpaRepository<E, ID> getRepository() {
        return repository;
    }
}

