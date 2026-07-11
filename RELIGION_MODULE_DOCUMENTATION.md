# Religion Master Data Module - Production Release

## Overview
The Religion module has been upgraded with production-ready features including soft delete, proper security, and comprehensive API patterns following Spring Boot best practices.

---

## **API Endpoints**

### **1. PUBLIC ENDPOINTS (No Authentication Required)**

#### Get All Active Religions
```
GET /api/religions
```
**Description:** Retrieve all active religions (soft-deleted records excluded)

**Response:** 
```json
{
  "success": true,
  "message": "Religions fetched successfully",
  "data": [
    {
      "id": 1,
      "adminId": 1,
      "adminName": "Super Admin",
      "name": "Hindu",
      "isActive": true,
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    },
    {
      "id": 2,
      "adminId": 1,
      "adminName": "Super Admin",
      "name": "Muslim",
      "isActive": true,
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    }
  ]
}
```

**HTTP Status:** 200 OK

---

#### Get Religion by ID
```
GET /api/religions/{id}
```
**Path Parameters:**
- `id` (Long, required): Religion ID

**Response:**
```json
{
  "success": true,
  "message": "Religion fetched successfully",
  "data": {
    "id": 1,
    "adminId": 1,
    "adminName": "Super Admin",
    "name": "Hindu",
    "isActive": true,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
}
```

**Error Responses:**
- 404 NOT_FOUND: When religion does not exist or is deleted
```json
{
  "timestamp": "2024-01-16T12:00:00",
  "status": 404,
  "error": "NOT_FOUND",
  "errorCode": "ERR_404",
  "message": "Religion not found"
}
```

**HTTP Status:** 200 OK or 404 NOT_FOUND

---

### **2. ADMIN ENDPOINTS (Requires ROLE_ADMIN or ROLE_SUPER_ADMIN)**

#### Create Religion
```
POST /api/religions
Authorization: Bearer {accessToken}
```
**Request Body:**
```json
{
  "name": "Zoroastrianism",
  "isActive": true
}
```

**Validation Rules:**
- `name`: Required, max 120 characters, must not contain only whitespace
- `isActive`: Optional (defaults to true if not provided)
- Admin ID is obtained from authenticated context (not in request)

**Response (Created):**
```json
{
  "success": true,
  "message": "Religion created successfully",
  "data": {
    "id": 8,
    "adminId": 1,
    "adminName": "Super Admin",
    "name": "Zoroastrianism",
    "isActive": true,
    "createdAt": "2024-01-16T12:00:00",
    "updatedAt": "2024-01-16T12:00:00"
  }
}
```

**Error Responses:**
- 400 BAD_REQUEST: When duplicate religion exists
```json
{
  "timestamp": "2024-01-16T12:00:00",
  "status": 400,
  "error": "BAD_REQUEST",
  "errorCode": "ERR_400_BAD_REQUEST",
  "message": "Religion 'Zoroastrianism' already exists for this admin"
}
```

- 400 VALIDATION_ERROR: When validation fails
```json
{
  "timestamp": "2024-01-16T12:00:00",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "errorCode": "ERR_400_VALIDATION",
  "message": "name: Religion name is required"
}
```

**HTTP Status:** 201 CREATED

---

#### Update Religion
```
PUT /api/religions/{id}
Authorization: Bearer {accessToken}
```
**Path Parameters:**
- `id` (Long, required): Religion ID

**Request Body:**
```json
{
  "name": "Updated Religion Name",
  "isActive": false
}
```

**Response:**
```json
{
  "success": true,
  "message": "Religion updated successfully",
  "data": {
    "id": 1,
    "adminId": 1,
    "adminName": "Super Admin",
    "name": "Updated Religion Name",
    "isActive": false,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-16T12:00:00"
  }
}
```

**Error Responses:**
- 404 NOT_FOUND: Religion not found or already deleted
- 400 BAD_REQUEST: Duplicate name conflict

**HTTP Status:** 200 OK

---

#### Soft Delete Religion
```
DELETE /api/religions/{id}
Authorization: Bearer {accessToken}
```
**Path Parameters:**
- `id` (Long, required): Religion ID

**Description:** Performs a soft delete - record is marked as deleted but not removed from database.
The authenticated admin's ID is recorded as `deletedBy`.

**Response:**
```json
{
  "success": true,
  "message": "Religion deleted successfully",
  "data": "Religion ID: 1"
}
```

**Note:** Record will not appear in normal queries after soft delete.

**HTTP Status:** 200 OK

---

#### Restore Soft-Deleted Religion
```
POST /api/religions/{id}/restore
Authorization: Bearer {accessToken}
```
**Path Parameters:**
- `id` (Long, required): Religion ID (must be soft-deleted)

**Response:**
```json
{
  "success": true,
  "message": "Religion restored successfully",
  "data": {
    "id": 1,
    "adminId": 1,
    "adminName": "Super Admin",
    "name": "Hindu",
    "isActive": true,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-16T12:00:00"
  }
}
```

**HTTP Status:** 200 OK

---

#### Get Religions for Authenticated Admin
```
GET /api/religions/admin/list
Authorization: Bearer {accessToken}
```
**Description:** Get all non-deleted religions created/managed by the authenticated admin

**Response:**
```json
{
  "success": true,
  "message": "Religions fetched successfully",
  "data": [...]
}
```

**HTTP Status:** 200 OK

---

#### Get Active Religions for Authenticated Admin
```
GET /api/religions/admin/active
Authorization: Bearer {accessToken}
```

**HTTP Status:** 200 OK

---

#### Get Inactive Religions for Authenticated Admin
```
GET /api/religions/admin/inactive
Authorization: Bearer {accessToken}
```

**HTTP Status:** 200 OK

---

#### Get Deleted Religions for Authenticated Admin
```
GET /api/religions/admin/deleted
Authorization: Bearer {accessToken}
```
**Description:** Retrieve soft-deleted religions for the authenticated admin

**HTTP Status:** 200 OK

---

#### Search Religions
```
GET /api/religions/admin/search?keyword={keyword}
Authorization: Bearer {accessToken}
```
**Query Parameters:**
- `keyword` (String, optional): Search term (case-insensitive)

**Response:**
```json
{
  "success": true,
  "message": "Search completed successfully",
  "data": [
    {
      "id": 1,
      "name": "Hindu",
      ...
    }
  ]
}
```

**HTTP Status:** 200 OK

---

## **Security & Authorization**

| Endpoint | Public | ROLE_ADMIN | ROLE_SUPER_ADMIN | Auth Required |
|----------|--------|-----------|------------------|---------------|
| GET /api/religions | ✅ | ✅ | ✅ | No |
| GET /api/religions/{id} | ✅ | ✅ | ✅ | No |
| POST /api/religions | ❌ | ✅ | ✅ | Yes |
| PUT /api/religions/{id} | ❌ | ✅ | ✅ | Yes |
| DELETE /api/religions/{id} | ❌ | ✅ | ✅ | Yes |
| POST /api/religions/{id}/restore | ❌ | ✅ | ✅ | Yes |
| GET /api/religions/admin/list | ❌ | ✅ | ✅ | Yes |
| GET /api/religions/admin/active | ❌ | ✅ | ✅ | Yes |
| GET /api/religions/admin/inactive | ❌ | ✅ | ✅ | Yes |
| GET /api/religions/admin/deleted | ❌ | ✅ | ✅ | Yes |
| GET /api/religions/admin/search | ❌ | ✅ | ✅ | Yes |

---

## **HTTP Status Codes**

| Code | Scenario |
|------|----------|
| 200 | Successful GET, PUT, DELETE, RESTORE operations |
| 201 | Successful POST (resource created) |
| 400 | Validation failure, duplicate records, bad request |
| 401 | Missing or invalid JWT token |
| 403 | Insufficient permissions (wrong role) |
| 404 | Resource not found |
| 500 | Server error |

---

## **Soft Delete Information**

When a religion is deleted:
1. `deleted_at` field is set to current timestamp
2. `deleted_by` field is set to the authenticated admin's ID
3. Record is NOT removed from database
4. Record is automatically excluded from normal queries
5. Deleted records can be:
   - Viewed via `/admin/deleted` endpoint
   - Restored via `/restore` endpoint
   - Permanently deleted if needed

---

## **Validation Scenarios**

### Valid Request
```json
{
  "name": "Atheism",
  "isActive": true
}
```

### Invalid Request - Missing Required Field
```json
{
  "isActive": true
}
```
**Error:** `Religion name is required`

### Invalid Request - Name Too Long
```json
{
  "name": "A very long religion name that exceeds the maximum allowed length of 120 characters which will cause a validation error",
  "isActive": true
}
```
**Error:** `Name must be less than 120 characters`

### Invalid Request - Duplicate Name
When creating a second "Hindu" for the same admin:
```json
{
  "name": "Hindu",
  "isActive": true
}
```
**Error:** `Religion 'Hindu' already exists for this admin`

---

## **Database Schema**

```sql
CREATE TABLE religions (
    id BIGSERIAL PRIMARY KEY,
    admin_id BIGINT,
    name VARCHAR(120) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    deleted_by BIGINT NULL,
    CONSTRAINT uq_religions_admin_name UNIQUE (admin_id, name),
    CONSTRAINT fk_religions_admin FOREIGN KEY (admin_id) REFERENCES admins(id) ON DELETE SET NULL,
    CONSTRAINT fk_religions_deleted_by FOREIGN KEY (deleted_by) REFERENCES admins(id) ON DELETE SET NULL
);

CREATE INDEX idx_religion_name ON religions(name);
CREATE INDEX idx_religion_is_active ON religions(is_active);
CREATE INDEX idx_religion_deleted_at ON religions(deleted_at);
CREATE INDEX idx_religion_deleted_by ON religions(deleted_by);
CREATE INDEX idx_religions_admin_id ON religions(admin_id);
```

---

## **Backward Compatibility**

The following deprecated endpoints are maintained for backward compatibility but should be migrated to the new versions:

| Deprecated Endpoint | New Endpoint |
|-------------------|--------------|
| GET /api/religions/admin/{adminId} | GET /api/religions/admin/list |
| GET /api/religions/admin/{adminId}/active | GET /api/religions/admin/active |
| GET /api/religions/admin/{adminId}/inactive | GET /api/religions/admin/inactive |
| GET /api/religions/admin/{adminId}/search | GET /api/religions/admin/search |

---

## **Error Response Format**

All errors follow this standard format:

```json
{
  "timestamp": "2024-01-16T12:00:00",
  "status": 400,
  "error": "ERROR_TYPE",
  "errorCode": "ERR_CODE",
  "message": "Descriptive error message"
}
```

---

## **Production Deployment Checklist**

- ✅ Entity includes soft delete fields (deleted_at, deleted_by)
- ✅ Repository has soft delete queries
- ✅ Service uses soft delete logic
- ✅ Controller has @PreAuthorize decorators
- ✅ All endpoints wrapped in ApiResponse
- ✅ Proper logging implemented
- ✅ Exception handling follows GlobalExceptionHandler pattern
- ✅ Flyway migration created (V56__add_soft_delete_to_religions.sql)
- ✅ Input validation on all requests
- ✅ Unique constraints enforced
- ✅ Foreign key relationships established
- ✅ Indexes created for performance
- ✅ Admin ID obtained from SecurityContext (not from request)
- ✅ Backward compatibility maintained

---

## **Performance Considerations**

1. **Indexes:** All queries benefit from indexes on frequently searched columns
2. **Soft Delete:** Uses `deletedAt IS NULL` filter - indexed for performance
3. **Lazy Loading:** Admin relationship uses LAZY loading to prevent N+1 queries
4. **Query Efficiency:** Service layer pre-filters deleted records
5. **Database Constraints:** Unique constraints prevent duplicate entries

---

## **Next Steps**

This Religion module serves as a template for implementing other master data modules:
- Caste
- SubCaste
- Country
- State
- City (District)
- Height
- Weight
- Occupation
- EducationLevel
- etc.

Same patterns should be applied to each module for consistency.

