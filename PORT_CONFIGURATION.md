# 🔧 Port Configuration Guide

## Your Setup

Your application is configured to run on the following ports:

### Frontend
- **Port:** 3000
- **URL:** http://localhost:3000
- **Configuration:** Already set in `vite.config.js`

### Backend
- **Port:** 9090
- **URL:** http://localhost:9090
- **Configuration:** Already set in `application.properties` or `application.yml`

### API Communication
- Frontend calls `/api/*` endpoints
- Vite proxy in `vite.config.js` automatically forwards requests to `http://localhost:9090`
- **No changes needed** - it's already configured!

---

## Running the Application

### Step 1: Start Backend (Port 9090)
```bash
cd C:\Users\Vaibhav\Downloads\demo\demo
.\mvnw.cmd spring-boot:run

# Backend will be available at: http://localhost:9090
```

### Step 2: Start Frontend (Port 3000)
```bash
cd C:\Users\Vaibhav\Downloads\demo\demo\frontend
npm install  # if needed
npm run dev

# Frontend will be available at: http://localhost:3000
```

### Step 3: Open in Browser
Navigate to: **http://localhost:3000**

---

## Proxy Configuration Details

The frontend automatically proxies API requests through `vite.config.js`:

```javascript
proxy: {
  "/api": {
    target: "http://localhost:9090",  // Backend URL
    changeOrigin: true,
    secure: false
  }
}
```

This means:
- Frontend request: `http://localhost:3000/api/shortlists`
- Proxied to: `http://localhost:9090/api/shortlists`
- **Completely transparent to your code!**

---

## Testing Endpoints

### Using Browser
```
Login: http://localhost:3000/login
Home: http://localhost:3000/home
Shortlists: http://localhost:3000/shortlists
```

### Using cURL (Direct Backend)
```bash
# Make sure to get token first
curl -X GET "http://localhost:9090/api/shortlists/me?page=0&size=20" \
  -H "Authorization: Bearer {your-token-here}"
```

### Using Postman/Insomnia
```
Base URL: http://localhost:9090
Add header: Authorization: Bearer {token}
Endpoints will work as documented
```

---

## Verifying the Connection

### 1. Check Backend is Running
```bash
curl http://localhost:9090/api/auth/login
# Should return an error about missing credentials (that's OK)
# Not a 404 or "connection refused"
```

### 2. Check Frontend is Running
```bash
curl http://localhost:3000
# Should return HTML content
```

### 3. Test Shortlist API
```bash
# 1. Login to get token
curl -X POST http://localhost:9090/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password"}'

# 2. Use token to test shortlist endpoint
curl -X GET "http://localhost:9090/api/shortlists/me" \
  -H "Authorization: Bearer {token-from-above}"
```

---

## Common Port Issues & Solutions

| Issue | Solution |
|-------|----------|
| Frontend won't start on 3000 | Port already in use. Kill process or change port in vite.config.js |
| Backend won't start on 9090 | Check application.properties for `server.port=9090` |
| API calls return 404 | Verify backend is running and proxy is working |
| CORS errors | Already configured in vite.config.js |
| "Connection refused" | One of the services isn't running |

---

## Troubleshooting Checklist

✅ **Backend Running?**
```bash
netstat -ano | findstr :9090
# Should show Java process listening on port 9090
```

✅ **Frontend Running?**
```bash
netstat -ano | findstr :3000
# Should show Node.js process listening on port 3000
```

✅ **Can Backend Access?**
```bash
curl http://localhost:9090
# Should get response
```

✅ **Can Frontend Access Backend Through Proxy?**
- Open frontend: http://localhost:3000
- Open browser DevTools → Network tab
- Try an API call (e.g., login)
- Check if request goes to `/api/...`

✅ **Is Token Valid?**
- Check localStorage in browser DevTools
- Verify token is being sent with requests
- Check token expiration

---

## Documentation Updated

All documentation files have been updated to reflect your port configuration:
- ✅ SHORTLIST_QUICK_START.md
- ✅ SHORTLIST_IMPLEMENTATION.md
- ✅ SHORTLIST_SYSTEM_COMPLETE.md

All cURL examples now use `http://localhost:9090`
All browser URLs now use `http://localhost:3000`

---

## You're All Set! 🚀

Your application is correctly configured for:
- **Frontend:** Port 3000
- **Backend:** Port 9090
- **Proxy:** Automatic forwarding from `/api` to backend

Just run:
1. Backend: `mvnw spring-boot:run`
2. Frontend: `npm run dev`
3. Open: http://localhost:3000

Enjoy! 🌟

