# Audit System - Quick Reference

## 5-Minute Setup

### 1. Make Your Entity Auditable

```java
import com.example.model.base.Auditable;

@Entity
@Table(name = "my_table")
public class MyEntity extends Auditable {
    private String name;
    // audit fields inherited automatically
}
```

### 2. Update Your Service

```java
import com.example.service.base.BaseAuditService;

@Service
public class MyService extends BaseAuditService<MyEntity, Long> {
    
    public MyService(MyEntityRepository repo) {
        super(repo);
    }
    
    public MyEntity create(String name) {
        MyEntity entity = new MyEntity();
        entity.setName(name);
        return saveWithAudit(entity); // Automatically sets createdBy
    }
}
```

## Common Operations

### Create with Audit
```java
// createdAt and createdBy set automatically
MyEntity entity = myService.saveWithAudit(myEntity);
```

### Update with Audit
```java
// updatedAt and updatedBy set automatically
entity.setName("New Name");
repository.save(entity);
```

### Soft Delete
```java
// Marks as deleted with timestamp and user
myService.softDelete(id, "User requested removal");
```

### Restore Deleted
```java
// Clears deletion flags
myService.restore(id);
```

### Get Audit Trail
```java
// Returns formatted audit history
String trail = myService.getAuditTrail(id).orElse("Not found");
System.out.println(trail);
```

## Available Audit Fields

| Field | Type | Auto-Set | Immutable | Example |
|-------|------|----------|-----------|---------|
| createdAt | LocalDateTime | Yes | Yes | 2024-01-15 10:30:45 |
| createdBy | Long | Via Listener | Yes | 123 (User ID) |
| updatedAt | LocalDateTime | Yes | No | 2024-01-16 14:22:30 |
| updatedBy | Long | Via Listener | No | 456 (User ID) |
| isDeleted | Boolean | No | No | false |
| deletedAt | LocalDateTime | Yes (on delete) | N/A | 2024-01-17 09:15:00 |
| deletedBy | Long | Yes (on delete) | N/A | 789 (User ID) |
| deletionReason | String | Manual | N/A | "User requested deletion" |
| version | Long | Yes | No | 0, 1, 2... (optimistic lock) |

## Querying with Audit Fields

### Active Records Only (exclude deleted)
```java
List<MyEntity> active = repository.findByIsDeletedFalse();
```

### Changes by Specific User
```java
List<MyEntity> created = repository.findByCreatedBy(userId);
List<MyEntity> updated = repository.findByUpdatedBy(userId);
```

### Recently Modified
```java
List<MyEntity> recent = repository.findByUpdatedAtAfter(startDateTime);
```

### Find Deletions
```java
List<MyEntity> deleted = repository.findByIsDeletedTrue();
List<MyEntity> deletedByUser = repository.findByDeletedBy(userId);
```

## Setting Audit User

### In Controllers/Service (Automatic with Security)
```java
// Already handled by SecurityAuditInterceptor
// Just make sure @Secured or @PreAuthorize is on your controller
@RestController
public class MyController {
    @PostMapping("/entities")
    @PreAuthorize("isAuthenticated()")
    public MyEntity create(@RequestBody MyEntity entity) {
        return myService.saveWithAudit(entity); // User ID auto-captured
    }
}
```

### In Background Jobs/Batch Operations (Manual)
```java
import com.example.util.AuditContext;

@Scheduled(fixedRate = 60000)
public void batchProcess() {
    // Set audit user for batch
    AuditContext.setCurrentUserId(SYSTEM_USER_ID);
    
    try {
        // Your batch operations
        myService.create("Batch Name");
    } finally {
        AuditContext.clear(); // Always clear!
    }
}
```

## Soft Delete Strategy

Use soft deletes for compliance and audit trails:

```java
// Don't do this (hard delete - loses data)
repository.deleteById(id);

// Do this instead (soft delete - keeps audit trail)
myService.softDelete(id, "User requested deletion");
```

## Queries with Soft Delete Filter

### Method 1: Spring Data JPA
```java
@Repository
public interface MyRepository extends JpaRepository<MyEntity, Long> {
    List<MyEntity> findByIsDeletedFalse();
    List<MyEntity> findByIsDeletedFalseAndName(String name);
}
```

### Method 2: @Query
```java
@Repository
public interface MyRepository extends JpaRepository<MyEntity, Long> {
    @Query("SELECT m FROM MyEntity m WHERE m.isDeleted = false")
    List<MyEntity> findAllActive();
}
```

### Method 3: Specification
```java
Specification<MyEntity> spec = (root, query, cb) -> 
    cb.equal(root.get("isDeleted"), false);
List<MyEntity> active = repository.findAll(spec);
```

## Index on Audit Columns

Indexes are auto-created by migration for:
- created_by
- updated_by  
- deleted_by
- is_deleted
- created_at
- updated_at

No additional configuration needed.

## Error Handling

### Optimistic Lock (Concurrent Update)
```java
try {
    myEntity.setName("New");
    repository.save(myEntity);
} catch (OptimisticLockingFailureException ex) {
    // Reload fresh copy and retry
    myEntity = repository.findById(id).orElseThrow();
    myEntity.setName("New");
    repository.save(myEntity);
}
```

### Audit Context Not Set
```java
// Audit fields still set automatically with defaults
// Warning logged if no user context available
myService.saveWithAudit(entity); // Still works, createdBy may be null
```

## Reporting Queries

### User Activity Report
```sql
-- All changes by user in date range
SELECT id, created_at, updated_at, deleted_at, deletion_reason
FROM my_entities
WHERE created_by = ? OR updated_by = ? OR deleted_by = ?
ORDER BY updated_at DESC;
```

### Deletion Audit Report
```sql
-- All soft-deleted records with deletion info
SELECT id, deleted_by, deleted_at, deletion_reason
FROM my_entities
WHERE is_deleted = true
ORDER BY deleted_at DESC LIMIT 100;
```

### Change Frequency
```sql
-- Most frequently updated records
SELECT id, COUNT(*) as update_count, MAX(updated_at) as last_update
FROM my_entities
WHERE is_deleted = false
GROUP BY id
ORDER BY update_count DESC LIMIT 10;
```

## Gotchas & Tips

✅ **DO:**
- Call `saveWithAudit()` to populate createdBy
- Use soft deletes instead of hard deletes
- Always filter by `isDeleted = false` in queries
- Clear AuditContext in try-finally blocks
- Create indexes on audit columns

❌ **DON'T:**
- Hard delete via `repository.deleteById()`
- Directly set createdBy/updatedBy fields
- Forget to clear AuditContext in batch jobs
- Expose createdBy/updatedBy in public APIs
- Rely on version for anything but optimistic locking

## Migration Command

```bash
# Apply audit columns to database
mysql -u root -p database_name < src/main/resources/database/audit-migration.sql
```

## Testing Example

```java
@Test
public void testAuditTrail() {
    // Setup
    AuditContext.setCurrentUserId(100L);
    
    // Create
    MyEntity entity = myService.create("Test");
    Long id = entity.getId();
    assertEquals(100L, entity.getCreatedBy());
    
    // Update
    AuditContext.setCurrentUserId(200L);
    entity.setName("Updated");
    repository.save(entity);
    entity = repository.findById(id).orElseThrow();
    assertEquals(200L, entity.getUpdatedBy());
    
    // Delete
    AuditContext.setCurrentUserId(300L);
    myService.softDelete(id, "Test done");
    entity = repository.findById(id).orElseThrow();
    assertEquals(300L, entity.getDeletedBy());
    assertTrue(entity.isDeleted());
    
    // Cleanup
    AuditContext.clear();
}
```

## Where to Find Things

| Component | Location |
|-----------|----------|
| Auditable Base Class | `model/base/Auditable.java` |
| Audit Context | `util/AuditContext.java` |
| Audit Listener | `util/AuditListener.java` |
| Base Service | `service/base/BaseAuditService.java` |
| Interceptor | `config/SecurityAuditInterceptor.java` |
| Web Config | `config/AuditWebConfig.java` |
| Database Migration | `resources/database/audit-migration.sql` |
| Full Guide | `AUDIT_IMPLEMENTATION_GUIDE.md` |

## Support

For issues:
1. Check AuditContext is set: `AuditContext.getCurrentUserId()`
2. Verify entity extends Auditable
3. Ensure migration is applied
4. Check logs for warnings from AuditListener

---

**Last Updated**: 2024-01-17  
**Quick Reference Version**: 1.0

