# Shortlist System - Quick Start Guide

## Overview
This guide walks you through setting up and testing the Shortlist System in your matrimony platform.

---

## Quick Setup (5 minutes)

### ✅ Backend is Ready
The backend is already implemented and integrated:
- Shortlist entity, repository, service all created
- REST controllers with JWT authentication
- Database schema will auto-create on startup

### ✅ Frontend is Ready
The frontend is already implemented:
- API client (`shortlistAPI.js`)
- React hook (`useShortlist`)
- UI components (`ShortlistButton`, `MyShortlists`)
- Navigation integration complete

---

## File Locations Reference

### Backend Files to Verify
```
src/main/java/com/example/
├── model/Shortlist.java ✅
├── repository/ShortlistRepository.java ✅
├── service/ShortlistService.java ✅
├── serviceimpl/ShortlistServiceImpl.java ✅
├── controller/user/ShortlistController.java ✅
├── dto/request/ShortlistRequestDTO.java ✅
└── dto/response/ShortlistResponseDTO.java ✅
```

### Frontend Files to Verify
```
frontend/src/
├── services/shortlistAPI.js ✅
├── hooks/useShortlist.jsx ✅
├── components/ShortlistButton.jsx ✅
├── pages/MyShortlists.jsx ✅
├── pages/Home.jsx (updated) ✅
├── pages/ProfileDetails.jsx (updated) ✅
├── components/Navbar.jsx (updated) ✅
└── App.jsx (updated) ✅
```

---

## Running the Application

### 1. Start Backend
```bash
cd C:\Users\Vaibhav\Downloads\demo\demo

# Using Maven wrapper
.\mvnw.cmd spring-boot:run

# Application runs on http://localhost:9090
```

### 2. Start Frontend
```bash
cd C:\Users\Vaibhav\Downloads\demo\demo\frontend

# Install dependencies if needed
npm install

# Start development server
npm run dev

# Application runs on http://localhost:3000
```

---

## Testing the Feature

### 1. Login to Account
```
Navigate to: http://localhost:3000/login
Email: test@example.com
Password: password
```

### 2. Add to Shortlist
```
Option A - From Home Page:
1. Go to Home (http://localhost:3000/home)
2. Browse profile cards
3. Click the star button next to Like button
4. See "Added to shortlists" toast

Option B - From Profile View:
1. Go to any profile (click View Profile)
2. Click star button in action panel
3. Button fills in and shows "Shortlisted"
```

### 3. View Shortlists
```
1. Click "Shortlists" in navigation menu
2. See all shortlisted profiles (paginated)
3. Use pagination controls if more than 20 items
4. Click "View" to see profile details
```

### 4. Remove from Shortlist
```
1. From My Shortlists page or any profile card
2. Click the filled star button again
3. See "Removed from shortlists" toast
4. Profile disappears from list automatically
```

### 5. Check Status
```
1. View multiple profiles
2. Shortlisted profiles show filled stars
3. Non-shortlisted profiles show empty stars
4. Close and reopen to verify persistence
```

---

## API Testing (Postman/cURL)

### Get JWT Token
```bash
curl -X POST http://localhost:9090/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password"}'

# Copy the accessToken from response
export TOKEN="your-token-here"
```

### Test Add to Shortlist
```bash
curl -X POST http://localhost:9090/api/shortlists \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"profileId": 1}'

# Response: ShortlistResponseDTO with shortlist details
```

### Test Get My Shortlists
```bash
curl -X GET "http://localhost:9090/api/shortlists/me?page=0&size=20" \
  -H "Authorization: Bearer $TOKEN"

# Response: Paginated list of shortlisted profiles
```

### Test Check Status
```bash
curl -X GET http://localhost:9090/api/shortlists/check/1 \
  -H "Authorization: Bearer $TOKEN"

# Response: true or false
```

### Test Remove from Shortlist
```bash
curl -X DELETE http://localhost:9090/api/shortlists/1 \
  -H "Authorization: Bearer $TOKEN"

# Response: 204 No Content
```

---

## Troubleshooting

### Issue: "User not found" Error
**Solution:** 
- Ensure you're logged in (valid JWT token)
- Check localStorage for token
- Re-login if token expired

### Issue: "Profile already shortlisted!"
**Solution:**
- This is expected behavior
- You can't add the same profile twice
- Remove first, then add again if needed

### Issue: ShortlistButton doesn't show
**Solution:**
- Verify imports in component files
- Check browser console for errors
- Ensure frontend is properly built

### Issue: Backend returns 401 Unauthorized
**Solution:**
- Token is missing or expired
- Get new token via login endpoint
- Add Bearer prefix: `Authorization: Bearer {token}`

### Issue: Database table not created
**Solution:**
- Ensure database is running
- Check Spring Boot logs for migration errors
- Verify application.properties has correct DB config
- Tables will auto-create on first run

---

## Performance Considerations

### Database Optimization
- Shortlist table has indexes on user_id and profile_id
- Unique constraint prevents duplicate inserts
- Queries use JOIN FETCH to avoid N+1 problems

### Frontend Optimization
- Hook caches data in window object
- Events synchronize state across components
- Pagination prevents loading all shortlists at once
- Profile details fetched lazily on page

### Recommended Indexes
```sql
-- Already created by Hibernate/Flyway
CREATE INDEX idx_shortlist_user ON shortlists(user_id);
CREATE INDEX idx_shortlist_profile ON shortlists(profile_id);
UNIQUE KEY unique_user_profile (user_id, profile_id);
```

---

## Common Issues & Solutions

| Issue | Cause | Solution |
|-------|-------|----------|
| Button doesn't respond | Token expired | Re-login |
| API returns 404 | Wrong endpoint | Check controller path /api/shortlists |
| Duplicate error | Already shortlisted | Refresh page or remove first |
| Page won't load | API error | Check browser network tab |
| Data not syncing | Cache issue | Clear localStorage and reload |

---

## Next Steps

### For Development
1. Read `SHORTLIST_IMPLEMENTATION.md` for detailed documentation
2. Review `ShortlistController.java` for API design
3. Check `useShortlist.jsx` for state management pattern

### For Production
1. Run security tests (SQL injection, authorization)
2. Load test pagination with 10000+ shortlists
3. Set up monitoring for shortlist endpoints
4. Configure rate limiting if needed

### For Enhancement
1. Add email notifications when shortlisted
2. Implement shortlist collections/categories
3. Add mutual shortlist indicator
4. Create shortlist analytics dashboard

---

## Key Endpoints Reference

### Pre-Production Ready Endpoints
```
POST   /api/shortlists              (Add - JWT required)
DELETE /api/shortlists/{userId}     (Remove - JWT required)
GET    /api/shortlists/me           (Get with pagination - JWT required)
GET    /api/shortlists/check/{userId} (Check - JWT required)
```

### Still Supported (Legacy)
```
POST   /api/shortlists/user/{userId}/profile/{profileId}
DELETE /api/shortlists/user/{userId}/profile/{profileId}
GET    /api/shortlists/user/{userId}
```

---

## Security Checklist

- [x] JWT authentication on all endpoints
- [x] User ownership validation
- [x] Self-shortlist prevention
- [x] Input validation
- [x] Error handling without exposing internals
- [x] SQL injection prevention (Hibernate)
- [x] CORS configured properly

---

## Monitoring & Logging

### Backend Logs
```
Check application logs for:
- Authentication failures
- Duplicate shortlist attempts
- Self-shortlist attempts
- Database errors
```

### Frontend Logs
```
Check browser console for:
- API errors (network tab)
- React warnings
- Cache issues
- Hook lifecycle logs
```

---

## Support

For issues or questions:

1. **Check Logs**
   - Backend: Spring Boot logs
   - Frontend: Browser console

2. **Review Documentation**
   - `SHORTLIST_IMPLEMENTATION.md` - Full guide
   - `SHORTLIST_CHECKLIST.md` - Implementation checklist
   - Code comments in components

3. **Test With cURL**
   - Verify API works independently
   - Check token validity
   - Test error scenarios

---

## Summary

🎉 **The Shortlist System is fully implemented and ready to use!**

**What's included:**
- ✅ Backend REST APIs with JWT auth
- ✅ Database schema with soft delete
- ✅ React components and hooks
- ✅ Integration in Home, Profile, and Navigation
- ✅ Pagination support
- ✅ Error handling and validation
- ✅ Security best practices

**To get started:**
1. Start backend: `mvnw spring-boot:run` (runs on http://localhost:9090)
2. Start frontend: `npm run dev` (runs on http://localhost:3000)
3. Open http://localhost:3000 in browser
4. Login and navigate to Home
5. Click star button to test shortlist
6. Visit Shortlists page to see all

**Enjoy! 🌟**

