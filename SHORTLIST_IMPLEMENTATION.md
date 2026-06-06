# Shortlist System Implementation Guide

## Overview
A complete shortlist feature for the matrimony platform allowing users to mark profiles they're interested in for later review.

---

## Backend Implementation

### 1. Database Model (`Shortlist.java`)
Located at: `src/main/java/com/example/model/Shortlist.java`

**Fields:**
- `id` - Primary key
- `user` - ManyToOne relationship (who created shortlist)
- `profile` - ManyToOne relationship (profile being shortlisted)
- `isActive` - Boolean (soft delete support, default: true)
- `createdAt` - LocalDateTime (auto-set on creation)
- `updatedAt` - LocalDateTime (auto-updated)

**Features:**
- Unique constraint on (user_id, profile_id) to prevent duplicates
- Indexes on user_id and profile_id for performance
- Lifecycle hooks for timestamp management

### 2. Repository (`ShortlistRepository.java`)
Location: `src/main/java/com/example/repository/ShortlistRepository.java`

**Key Methods:**
- `existsByUser_IdAndProfile_Id()` - Check for duplicates
- `findByUser_IdAndProfile_Id()` - Get specific shortlist record
- `findByUser_IdAndIsActiveTrue()` - Get all active shortlists for user (with pagination)
- `findByProfile_IdAndIsActiveTrue()` - Get who shortlisted a profile
- `findByIsActiveTrue()` - Get all active shortlists

### 3. Service Layer

**Interface (`ShortlistService.java`)**
Location: `src/main/java/com/example/service/ShortlistService.java`

**Implementation (`ShortlistServiceImpl.java`)**
Location: `src/main/java/com/example/serviceimpl/ShortlistServiceImpl.java`

**Key Methods:**
- `addToShortlist()` - Add to shortlist with duplicate check and reactivation
- `removeFromShortlist()` - Soft delete (set isActive=false)
- `getByUser()` - Get user's shortlists (list and paginated)
- `getByUserAndProfile()` - Check if specific profile is shortlisted
- `getByProfile()` - Get who shortlisted a profile
- `getAll()` - Get all active shortlists

**Business Rules:**
- Cannot shortlist yourself
- Duplicate shortlists return error message
- If already shortlisted and removed, can re-add (reactivate)
- Soft delete preferred over hard delete

### 4. DTOs

**Request DTO (`ShortlistRequestDTO.java`)**
```java
{
  "profileId": Long,  // or "userId": Long
  "userId": Long      // optional, used to get profile by userId
}
```

**Response DTO (`ShortlistResponseDTO.java`)**
```java
{
  "id": Long,
  "userId": Long,
  "userName": String,
  "profileId": Long,
  "isActive": Boolean,
  "createdAt": LocalDateTime,
  "updatedAt": LocalDateTime
}
```

### 5. REST Controller APIs
Location: `src/main/java/com/example/controller/user/ShortlistController.java`

#### **Authenticated Endpoints (JWT Required)**

**POST /api/shortlists**
- Add profile to shortlist
- Request: `{ "profileId": 123 }`
- Response: ShortlistResponseDTO
- Features: Prevents self-shortlisting, checks for duplicates, reactivates if removed

**DELETE /api/shortlists/{shortlistedUserId}**
- Remove profile from shortlist
- Response: 204 No Content
- Features: Soft delete (sets isActive=false)

**GET /api/shortlists/me?page=0&size=20**
- Get current user's shortlisted profiles with pagination
- Response:
```json
{
  "content": [ShortlistResponseDTO],
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5
}
```

**GET /api/shortlists/check/{shortlistedUserId}**
- Check if user is shortlisted
- Response: `true` or `false`

#### **Legacy Endpoints (Still Supported)**

- `POST /api/shortlists/user/{userId}/profile/{profileId}`
- `DELETE /api/shortlists/user/{userId}/profile/{profileId}`
- `GET /api/shortlists/user/{userId}`
- `GET /api/shortlists/user/{userId}/profile/{profileId}/check`
- `GET /api/shortlists/profile/{profileId}`
- `GET /api/shortlists`

---

## Frontend Implementation

### 1. API Service (`shortlistAPI.js`)
Location: `frontend/src/services/shortlistAPI.js`

```javascript
// Add to shortlist
await shortlistAPI.add(profileId)

// Remove from shortlist
await shortlistAPI.remove(shortlistedUserId)

// Get my shortlists with pagination
await shortlistAPI.getMyShortlists(page, size)

// Check if shortlisted
await shortlistAPI.checkStatus(shortlistedUserId)
```

### 2. Custom Hook (`useShortlist.jsx`)
Location: `frontend/src/hooks/useShortlist.jsx`

**Features:**
- Shared cache across components using window object
- Automatic synchronization using custom events
- Lazy loading of shortlists
- Profile ID tracking with Set for O(1) lookups

**Usage:**
```javascript
const { items, loading, error, isShortlisted, add, remove, refresh, count } = useShortlist();

// Check if profile is shortlisted
if (isShortlisted(profileId)) { ... }

// Add to shortlist
await add(profileId);

// Remove from shortlist
await remove(profileId);
```

### 3. ShortlistButton Component
Location: `frontend/src/components/ShortlistButton.jsx`

**Props:**
- `profileId` (required) - Profile to shortlist
- `size` (optional, default: 'md') - 'sm', 'md', 'lg'
- `showLabel` (optional, default: true) - Show/hide text label

**Features:**
- Star icon (filled when shortlisted)
- Loading states
- Toast notifications
- Responsive sizing

**Usage:**
```jsx
<ShortlistButton profileId={123} size="md" showLabel={true} />
```

### 4. My Shortlists Page (`MyShortlists.jsx`)
Location: `frontend/src/pages/MyShortlists.jsx`

**Features:**
- Display all shortlisted profiles
- Pagination support
- Profile card with image, name, location
- View Profile button
- Loading/error states
- Empty state message
- Refreshes on mount

### 5. Integration Points

**Home Page (`Home.jsx`)**
- ShortlistButton added next to LikeBookmarkButtons on profile cards
- Uses compact size (`sm`)
- Hides label for space optimization

**Profile Details Page (`ProfileDetails.jsx`)**
- ShortlistButton in action panel
- Standard size with label
- Allows shortlisting from profile view

**Navbar (`Navbar.jsx`)**
- New "Shortlists" link in navigation
- Available only to authenticated users
- Routes to `/shortlists`

**App Routes (`App.jsx`)**
- New route: `/shortlists` → `MyShortlists` component
- Protected with AuthenticatedLayout

---

## Security Implementation

### JWT Authentication
- Uses `SecurityUtils.getCurrentUsername()` to get logged-in user email
- All authenticated endpoints require valid JWT token
- User cannot access other users' shortlists

### Authorization
- Users can only manage their own shortlists
- Backend validates user ownership before operations
- Self-shortlisting prevented at service layer

### Validation
- Profile existence checks
- User existence checks
- Unique constraint on (user_id, profile_id)
- Duplicate add returns meaningful error

---

## Error Handling

### Exception Handling (`GlobalExceptionHandler.java`)
All errors handled globally with:
- Proper HTTP status codes
- Error messages
- Error codes for client-side handling
- Timestamps

### Common Scenarios

| Scenario | Error | Status |
|----------|-------|--------|
| User not found | "User not found" | 400 |
| Profile not found | "Profile not found" | 400 |
| Self-shortlist | "You cannot shortlist yourself" | 400 |
| Duplicate shortlist | "Profile already shortlisted!" | 400 |
| Already removed | "Shortlist not found" | 400 |
| No token | Unauthorized | 401 |

---

## Pagination Support

**Request:**
```
GET /api/shortlists/me?page=0&size=20
```

**Response:**
```json
{
  "content": [...],
  "page": 0,
  "size": 20,
  "totalElements": 150,
  "totalPages": 8
}
```

**Frontend Usage:**
```javascript
// First page (default)
const data = await shortlistAPI.getMyShortlists(0, 20);

// Second page
const data = await shortlistAPI.getMyShortlists(1, 20);
```

---

## Production Considerations

### Performance
- Database indexes on user_id, profile_id
- Unique constraint prevents duplicate DB inserts
- Efficient query using JOIN FETCH to avoid N+1 problem
- Pagination prevents loading all shortlists at once

### Scalability
- Soft delete allows for data recovery
- Pagination supports unlimited shortlist sizes
- Efficient search with cache in frontend

### Monitoring
- All exceptions logged with Slf4j
- Request/response tracking in controller
- Meaningful error messages for debugging

---

## Testing Checklist

### Backend Tests
- [ ] Add shortlist - success
- [ ] Add shortlist - duplicate (should fail)
- [ ] Add shortlist - self (should fail)
- [ ] Remove shortlist - success
- [ ] Get user shortlists - pagination works
- [ ] Check shortlist status - accurate
- [ ] Security - unauthenticated request rejected
- [ ] Security - user cannot access other's shortlists

### Frontend Tests
- [ ] ShortlistButton renders correctly
- [ ] Click adds to shortlist, shows success toast
- [ ] Click again removes, shows success toast
- [ ] MyShortlists page loads data
- [ ] Pagination works
- [ ] Profile card integration works
- [ ] Cache synchronization works across components

---

## API Examples

### cURL Examples

**Add Shortlist**
```bash
curl -X POST http://localhost:9090/api/shortlists \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{"profileId": 123}'
```

**Remove Shortlist**
```bash
curl -X DELETE http://localhost:9090/api/shortlists/456 \
  -H "Authorization: Bearer {token}"
```

**Get My Shortlists**
```bash
curl -X GET "http://localhost:9090/api/shortlists/me?page=0&size=20" \
  -H "Authorization: Bearer {token}"
```

**Check Shortlist**
```bash
curl -X GET http://localhost:9090/api/shortlists/check/456 \
  -H "Authorization: Bearer {token}"
```

---

## File Structure Summary

```
Backend:
├── model/Shortlist.java
├── repository/ShortlistRepository.java
├── service/ShortlistService.java
├── serviceimpl/ShortlistServiceImpl.java
├── controller/user/ShortlistController.java
├── dto/request/ShortlistRequestDTO.java
├── dto/response/ShortlistResponseDTO.java
└── exception/GlobalExceptionHandler.java (already configured)

Frontend:
├── services/shortlistAPI.js
├── hooks/useShortlist.jsx
├── components/ShortlistButton.jsx
├── pages/MyShortlists.jsx
├── App.jsx (routes added)
├── Navbar.jsx (navigation added)
└── pages/Home.jsx & ProfileDetails.jsx (integration)
```

---

## Next Steps (Optional Enhancements)

1. **Email Notifications** - Notify when someone shortlists you
2. **Shortlist Collections** - Allow organizing shortlists into categories
3. **Mutual Shortlist** - Show if both users have shortlisted each other
4. **Analytics** - Track most shortlisted profiles
5. **Export** - Download shortlist as PDF/CSV
6. **Bulk Operations** - Add/remove multiple shortlists at once
7. **Filters** - Sort/filter shortlists by date, status, etc.

---

## Questions or Issues?

Refer to the backend logs for detailed error information:
- Exception stack traces logged with Slf4j
- Check GlobalExceptionHandler for error mappings
- Verify JWT token is valid and not expired
- Ensure user profile exists before shortlisting

