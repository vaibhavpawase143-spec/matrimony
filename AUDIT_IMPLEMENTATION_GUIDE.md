# Production-Level Audit Implementation Guide

## Overview

This document describes the comprehensive audit trail system implemented for production-level data tracking. The system provides automatic tracking of who created, updated, and deleted records, along with timestamps for each operation.

## Architecture

### Components

1. **Auditable Base Class** (`model/base/Auditable.java`)
   - MappedSuperclass providing all audit fields
   - Automatic timestamp management via @PrePersist/@PreUpdate
   - Soft delete functionality with audit trails

2. **Audit Fields**
   - `createdAt`: When the record was created (immutable after creation)
   - `createdBy`: User ID who created the record
   - `updatedAt`: Last update timestamp
   - `updatedBy`: User ID who last updated the record
   - `isDeleted`: Soft delete flag (default: false)
   - `deletedAt`: When the record was soft deleted
   - `deletedBy`: User ID who performed the deletion
   - `deletionReason`: Optional reason for deletion
   - `version`: Optimistic locking version for concurrent updates

3. **AuditContext** (`util/AuditContext.java`)
   - ThreadLocal storage for current user information
   - Set automatically by interceptor at request time
   - Cleared after request completion to prevent memory leaks

4. **AuditListener** (`util/AuditListener.java`)
   - JPA Entity Listener for automatic audit field population
   - Populates createdBy and updatedBy from AuditContext
   - Applied to all Auditable entities

5. **SecurityAuditInterceptor** (`config/SecurityAuditInterceptor.java`)
   - HTTP interceptor that extracts current user from Spring Security
   - Sets up AuditContext at request start
   - Clears AuditContext after request completion

6. **AuditWebConfig** (`config/AuditWebConfig.java`)
   - Spring Web MVC configuration
   - Registers the SecurityAuditInterceptor

7. **BaseAuditService** (`service/base/BaseAuditService.java`)
   - Base service class with common audit operations
   - Provides soft delete, restore, and audit trail methods

## Usage

### Making an Entity Auditable

To make any entity auditable, extend the `Auditable` base class:

```java
import com.example.model.base.Auditable;
import jakarta.persistence.*;

@Entity
@Table(name = "my_entities")
public class MyEntity extends Auditable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    
    // No need to add audit fields - inherited from Auditable
}
```

### Creating Records (with Audit Trail)

```java
@Service
public class MyEntityService extends BaseAuditService<MyEntity, Long> {
    
    private final MyEntityRepository repository;
    
    public MyEntityService(MyEntityRepository repository) {
        super(repository);
    }
    
    public MyEntity create(String name, String description) {
        MyEntity entity = new MyEntity();
        entity.setName(name);
        entity.setDescription(description);
        
        // saveWithAudit automatically sets createdBy from AuditContext
        return saveWithAudit(entity);
    }
}
```

### Soft Deleting Records

```java
// With deletion reason
Optional<MyEntity> deleted = myService.softDelete(entityId, "User requested deletion");

// Without deletion reason
Optional<MyEntity> deleted = myService.softDelete(entityId);
```

### Restoring Soft-Deleted Records

```java
Optional<MyEntity> restored = myService.restore(entityId);
```

### Getting Audit Trail

```java
Optional<String> auditTrail = myService.getAuditTrail(entityId);
// Output example:
// "Created: 2024-01-15 10:30:45 by User #123 | Updated: 2024-01-16 14:22:30 by User #456 | 
//  Deleted: 2024-01-17 09:15:00 by User #789 Reason: User requested deletion"
```

### Checking if Record is Deleted

```java
if (myService.isDeleted(entityId)) {
    // Record is soft deleted
}
```

## Setting the Audit User

### Automatic (Recommended)

The `SecurityAuditInterceptor` automatically sets the audit context from Spring Security:

```java
// The interceptor runs for every HTTP request
// Extracts current user from SecurityContextHolder
// Sets it in AuditContext automatically
```

### Manual (For Non-HTTP Operations)

For batch operations, background jobs, or non-HTTP contexts:

```java
import com.example.util.AuditContext;

// At the start of operation
AuditContext.setCurrentUserId(userId);
AuditContext.setCurrentUserName("username");

try {
    // Perform operations - audit fields will be populated automatically
    myService.create("Name", "Description");
} finally {
    AuditContext.clear(); // Always clear to prevent memory leaks
}
```

## Database Migration

### Running the Migration

The migration script is located at: `src/main/resources/database/audit-migration.sql`

Execute it on your database:

```bash
# MySQL
mysql -u root -p database_name < src/main/resources/database/audit-migration.sql

# PostgreSQL
psql -U postgres -d database_name -f src/main/resources/database/audit-migration.sql
```

### What the Migration Does

1. Adds all audit columns to existing tables
2. Creates indexes on frequently queried audit columns
3. Sets appropriate default values
4. Supports existing data (nullable columns)

### Supported Tables

- users
- profiles
- partner_preferences
- interests
- shortlists
- messages
- payments
- user_subscriptions
- user_photos
- support_tickets
- reports
- audit_log
- conversations
- swipes
- admin_audit_log
- profile_visitors

## Advanced Features

### Optimistic Locking

The `version` field enables optimistic locking for concurrent updates:

```java
// The @Version annotation automatically manages this
// Prevents lost updates in high-concurrency scenarios
@Version
@Column(name = "version")
private Long version;
```

### Soft Delete Queries

When querying, exclude soft-deleted records:

```java
@Repository
public interface MyEntityRepository extends JpaRepository<MyEntity, Long> {
    List<MyEntity> findByIsDeletedFalse();
    List<MyEntity> findByIsDeletedFalseAndName(String name);
    
    @Query("SELECT e FROM MyEntity e WHERE e.isDeleted = false")
    List<MyEntity> findAllActive();
}
```

### Audit Trail Queries

Find all changes made by a specific user:

```java
@Repository
public interface MyEntityRepository extends JpaRepository<MyEntity, Long> {
    List<MyEntity> findByCreatedBy(Long userId);
    List<MyEntity> findByUpdatedBy(Long userId);
    List<MyEntity> findByDeletedBy(Long userId);
}
```

## Security Considerations

1. **Never expose user IDs**: The createdBy/updatedBy/deletedBy fields contain user IDs for audit purposes only. Never expose them in APIs.

2. **Immutable Creation Fields**: createdAt and createdBy are immutable after creation - no updates allowed.

3. **Access Control**: Ensure only authorized users can view audit logs and deletion reasons.

4. **Concurrent Updates**: The version field prevents lost updates. Handle OptimisticLockingFailureException in application logic.

## Performance Optimization

### Indexes

The migration creates indexes on:
- created_by, updated_by, deleted_by
- is_deleted, created_at, updated_at

These improve query performance for audit-related queries:

```java
// Fast query with indexes
List<MyEntity> recentChanges = repository.findByUpdatedAtAfter(startDate);

// Fast query with soft delete filter
List<MyEntity> active = repository.findByIsDeletedFalse();
```

### Lazy Loading

Use FetchType.LAZY for relationships to avoid N+1 queries:

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "created_by")
private User createdByUser; // Loaded only when accessed
```

## Monitoring and Compliance

### Audit Trail Reporting

Generate audit trail reports:

```sql
SELECT 
    id,
    created_by,
    created_at,
    updated_by,
    updated_at,
    deleted_by,
    deleted_at,
    deletion_reason,
    version
FROM my_entities
WHERE deleted_at IS NOT NULL
ORDER BY deleted_at DESC;
```

### User Activity Tracking

Find all changes by a user in a time range:

```sql
SELECT * FROM profiles
WHERE (created_by = ? AND created_at BETWEEN ? AND ?)
   OR (updated_by = ? AND updated_at BETWEEN ? AND ?)
   OR (deleted_by = ? AND deleted_at BETWEEN ? AND ?)
ORDER BY updated_at DESC;
```

### Deletion Audit

Track all deletions with reasons:

```sql
SELECT 
    id,
    deleted_by,
    deleted_at,
    deletion_reason,
    (SELECT username FROM users WHERE id = deleted_by) as deleted_by_user
FROM my_entities
WHERE is_deleted = true
ORDER BY deleted_at DESC;
```

## Troubleshooting

### Audit Fields Not Populated

**Problem**: createdBy/updatedBy fields are null

**Solution**: 
1. Ensure AuditContext.setCurrentUserId() is called
2. Check SecurityAuditInterceptor is registered
3. Verify @EntityListeners(AuditListener.class) is on Auditable class

### Optimistic Lock Exceptions

**Problem**: `OptimisticLockingFailureException` when updating

**Solution**: Handle the exception and retry:

```java
try {
    entity.setName("New Name");
    repository.save(entity);
} catch (OptimisticLockingFailureException e) {
    // Reload and retry
    entity = repository.findById(id).orElseThrow();
    entity.setName("New Name");
    repository.save(entity);
}
```

### Performance Issues with Audit Queries

**Problem**: Slow queries on audit columns

**Solution**: 
1. Ensure indexes are created (run migration)
2. Use @Query with specific columns
3. Implement pagination for large result sets
4. Consider archiving old audit logs

## Future Enhancements

1. **Audit Log Archival**: Archive old audit records to separate table
2. **Encryption**: Encrypt deletion reasons for sensitive data
3. **Audit Events**: Publish events for external audit systems
4. **Retention Policy**: Automatic cleanup of old audit records
5. **Change History**: Track field-level changes in history table

## Testing

### Unit Tests

```java
@Test
public void testAuditFields() {
    // Set audit context
    AuditContext.setCurrentUserId(123L);
    
    MyEntity entity = myService.create("Test", "Description");
    
    assertEquals(123L, entity.getCreatedBy());
    assertEquals(123L, entity.getUpdatedBy());
    assertNotNull(entity.getCreatedAt());
    assertFalse(entity.isDeleted());
    
    AuditContext.clear();
}

@Test
public void testSoftDelete() {
    // Create and delete
    MyEntity entity = myService.create("Test", "Description");
    Long id = entity.getId();
    
    AuditContext.setCurrentUserId(456L);
    myService.softDelete(id, "Test deletion");
    
    MyEntity deleted = repository.findById(id).orElseThrow();
    assertTrue(deleted.isDeleted());
    assertEquals(456L, deleted.getDeletedBy());
    assertNotNull(deleted.getDeletedAt());
    
    AuditContext.clear();
}
```

## References

- JPA Lifecycle Callbacks: https://docs.jpa-spec.org/JPA_2.1/html/
- Spring Security: https://spring.io/projects/spring-security
- Optimistic Locking: https://en.wikipedia.org/wiki/Optimistic_concurrency_control
- Soft Deletes: https://en.wikipedia.org/wiki/Soft_delete

---

**Last Updated**: 2024-01-17
**Status**: Production Ready
**Version**: 1.0

