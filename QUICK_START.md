# 🚀 Quick Start - Mobile OTP Verification

## **Step 1: Run the Application**

### **Method A: From IDE (Easiest)**
1. Open `src/main/java/com/example/DemoApplication.java`
2. Click the **green ▶️ play button** next to `public static void main`
3. Wait for "Started DemoApplication in X seconds"

### **Method B: From Terminal**
```powershell
# Set Java home (one-time)
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot"

# Navigate to project
cd "C:\Users\Vaibhav\Downloads\demo\demo"

# Run with Maven wrapper
.\mvnw.cmd spring-boot:run
```

---

## **Step 2: Test Mobile Verification**

**Using Postman/Thunder Client:**

### **Register User**
```
POST http://localhost:9090/api/users/register
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phone": "+919876543210",
  "password": "password123"
}
```

**Check Console Output:**
```
📱 [MOCK SMS - OTP DISABLED] Phone: +919876543210
📱 [MOCK SMS - OTP CODE] Your OTP: 123456 (Valid for 10 minutes)
```

### **Verify OTP**
```
POST http://localhost:9090/api/users/verify-otp?phone=+919876543210&otp=123456
```

### **Verify Email** (Get token from email)
```
GET http://localhost:9090/api/users/verify?token=YOUR_TOKEN_HERE
```

### **Login**
```
POST http://localhost:9090/api/users/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

## **Step 3: Enable Real SMS (Optional)**

### **Default Behavior (Mock SMS)**
- OTP printed to console
- No SMS charges
- Perfect for development

### **To Enable Real SMS:**

**Option 1: Twilio (Recommended)**
1. Sign up at https://www.twilio.com
2. Get Account SID and Auth Token
3. Add to `application.properties`:
```properties
app.sms.enabled=true
twilio.account-sid=ACxxxxxxxxxx
twilio.auth-token=your_auth_token
twilio.phone-number=+1234567890
```
4. Add dependency to `pom.xml`:
```xml
<dependency>
    <groupId>com.twilio.sdk</groupId>
    <artifactId>twilio</artifactId>
    <version>9.2.0</version>
</dependency>
```

**Option 2: AWS SNS**
- Already integrated, just add AWS credentials

**Option 3: Firebase Cloud Messaging**
- Add FCM credentials in `application.properties`

---

## **API Endpoints Summary**

| Endpoint | Method | Purpose |
|----------|--------|---------|
| `/api/users/register` | POST | Register new user + send email & OTP |
| `/api/users/send-otp` | POST | Send OTP to phone |
| `/api/users/verify-otp` | POST | Verify OTP |
| `/api/users/resend-otp` | POST | Resend new OTP |
| `/api/users/verify` | GET | Verify email |
| `/api/users/login` | POST | Login (requires phone + email verified) |

---

## **Database**

The migration will auto-create the table when app starts:
- File: `src/main/resources/migration/V31__create_phone_verification_otps.sql`
- Table: `phone_verification_otps`
- Auto-runs via Flyway

---

## **Troubleshooting**

| Issue | Solution |
|-------|----------|
| "No OTP found" | Call `/send-otp` first |
| "OTP expired" | 10 min limit, call `/resend-otp` |
| "Too many attempts" | Call `/resend-otp` |
| "Phone not verified" | Complete `/verify-otp` first |
| "Email not verified" | Check email, click link from token |
| Port 9090 in use | Change in `application.properties`: `server.port=9091` |

---

## **Project Structure**

```
demo/
├── src/main/java/com/example/
│   ├── model/
│   │   ├── User.java (✅ Updated - added phoneVerified field)
│   │   └── PhoneVerificationOTP.java (✅ NEW)
│   ├── repository/
│   │   └── PhoneVerificationOTPRepository.java (✅ NEW)
│   ├── service/
│   │   ├── UserService.java (✅ Updated - added phone methods)
│   │   └── SMSService.java (✅ NEW)
│   ├── serviceimpl/
│   │   ├── UserServiceImpl.java (✅ Updated - added phone logic)
│   │   └── SMSServiceImpl.java (✅ NEW)
│   └── controller/
│       └── UserController.java (✅ Updated - added OTP endpoints)
├── src/main/resources/migration/
│   └── V31__create_phone_verification_otps.sql (✅ NEW)
└── MOBILE_VERIFICATION_API_GUIDE.md (✅ NEW - Full docs)
```

---

## **Features Implemented**

✅ Phone OTP generation (6 digits)
✅ 10-minute OTP expiry
✅ Max 5 failed attempts
✅ Resend OTP functionality
✅ Phone verification status in User model
✅ Login requires both email + phone verified
✅ Mock SMS for development
✅ Production SMS ready (Twilio/AWS/FCM)
✅ Anti-spam attempt limiting
✅ Automatic database migration

---

## **Example Workflow**

```
1. User calls /register
   ↓
2. Email & OTP sent
   ↓
3. User clicks email link (/verify?token=...)
   ↓
4. User enters OTP (/verify-otp?phone=...&otp=...)
   ↓
5. User calls /login
   ↓
6. JWT token returned ✅
```

---

**That's it! Your mobile verification OTP system is ready.** 🎉

For questions, check `MOBILE_VERIFICATION_API_GUIDE.md`

