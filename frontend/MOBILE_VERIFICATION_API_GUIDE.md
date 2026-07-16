# 📱 Mobile Verification OTP API Guide

## **Registration with Phone Verification**

### **1. Register User**
```
POST /api/users/register
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phone": "+919876543210",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "User registered successfully. Please verify your email and phone number.",
  "data": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "phone": "+919876543210",
    "emailVerified": false,
    "phoneVerified": false
  }
}
```

✅ **What happens:** 
- User created
- Email verification token sent
- OTP sent to phone number (check console or SMS)

---

### **2. Send OTP to Phone**
```
POST /api/users/send-otp?phone=+919876543210
```

**Response:**
```json
{
  "success": true,
  "message": "OTP sent successfully to +919876543210. Valid for 10 minutes.",
  "data": null
}
```

---

### **3. Verify OTP**
```
POST /api/users/verify-otp?phone=+919876543210&otp=123456
```

**Response:**
```json
{
  "success": true,
  "message": "Phone verified successfully",
  "data": null
}
```

✅ **What happens:**
- OTP validated
- Phone marked as verified in database
- User can now login

---

### **4. Resend OTP**
```
POST /api/users/resend-otp?phone=+919876543210
```

**Response:**
```json
{
  "success": true,
  "message": "OTP resent successfully to +919876543210",
  "data": null
}
```

---

### **5. Verify Email** (Existing endpoint)
```
GET /api/users/verify?token=VERIFICATION_TOKEN_HERE
```

---

### **6. Login (After Verification)**
```
POST /api/users/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

❌ **Will fail if:**
- Email not verified → "Please verify your email first"
- Phone not verified → "Please verify your phone number first"
- Wrong password

---

## **Testing with Postman/Insomnia**

### **Example Flow:**

1. **Import this collection:**
```json
{
  "info": {
    "name": "Mobile Verification OTP",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Register",
      "request": {
        "method": "POST",
        "url": "http://localhost:8080/api/users/register",
        "body": {
          "mode": "raw",
          "raw": "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john@example.com\",\"phone\":\"+919876543210\",\"password\":\"password123\"}"
        }
      }
    },
    {
      "name": "Verify OTP",
      "request": {
        "method": "POST",
        "url": "http://localhost:8080/api/users/verify-otp?phone=+919876543210&otp=123456"
      }
    },
    {
      "name": "Login",
      "request": {
        "method": "POST",
        "url": "http://localhost:8080/api/users/login",
        "body": {
          "mode": "raw",
          "raw": "{\"email\":\"john@example.com\",\"password\":\"password123\"}"
        }
      }
    }
  ]
}
```

---

## **Features Implemented**

✅ **OTP Generation** - 6-digit random OTP
✅ **OTP Expiry** - 10 minutes validity
✅ **Attempt Limiting** - Max 5 failed attempts
✅ **Phone Verification** - Updates user `phoneVerified` status
✅ **Resend OTP** - Users can request new OTP
✅ **Login Validation** - Both email and phone must be verified
✅ **SMS Mock** - Console output for testing (no SMS costs)

---

## **Production: Enable Real SMS (Twilio)**

### **Option A: Using Twilio**

1. **Install Twilio dependency** (add to pom.xml):
```xml
<dependency>
    <groupId>com.twilio.sdk</groupId>
    <artifactId>twilio</artifactId>
    <version>9.2.0</version>
</dependency>
```

2. **Update application.properties:**
```properties
twilio.account-sid=YOUR_TWILIO_ACCOUNT_SID
twilio.auth-token=YOUR_TWILIO_AUTH_TOKEN
twilio.phone-number=+1234567890
app.sms.enabled=true
```

3. **Update SMSServiceImpl.java:**
```java
@Service
@RequiredArgsConstructor
public class SMSServiceImpl implements SMSService {

    @Value("${twilio.account-sid}")
    private String accountSid;
    
    @Value("${twilio.auth-token}")
    private String authToken;
    
    @Value("${twilio.phone-number}")
    private String fromPhoneNumber;

    @Override
    public void sendOTP(String phone, String otp) {
        Twilio.init(accountSid, authToken);
        Message message = Message.creator(
            new PhoneNumber(fromPhoneNumber),
            new PhoneNumber(phone),
            "Your OTP: " + otp + ". Valid for 10 minutes. Do not share with anyone."
        ).create();
        System.out.println("✅ OTP SENT: " + message.getSid());
    }
}
```

### **Option B: Using AWS SNS**

```xml
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>sns</artifactId>
    <version>2.20.0</version>
</dependency>
```

### **Option C: Using Firebase Cloud Messaging (FCM)**

```xml
<dependency>
    <groupId>com.google.firebase</groupId>
    <artifactId>firebase-admin</artifactId>
    <version>9.2.0</version>
</dependency>
```

---

## **Security Considerations**

✅ OTP stored in database (hashed recommended)
✅ 10-minute expiry time
✅ Max 5 failed attempts
✅ Phone number unique constraint
✅ No OTP in logs/responses

⚠️ **Future improvements:**
- Hash OTP before storing
- Rate limiting per IP
- Blacklist repeated failures
- Multi-factor authentication support

---

## **Database Schema**

```sql
CREATE TABLE phone_verification_otps (
    id BIGSERIAL PRIMARY KEY,
    phone VARCHAR(15) NOT NULL,
    otp VARCHAR(6) NOT NULL,
    verified BOOLEAN DEFAULT false,
    expiry_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    attempt_count INTEGER DEFAULT 0
);

-- User table already has:
-- phone_verified BOOLEAN DEFAULT false
-- phone_verified_at TIMESTAMP
```

---

## **Common Issues & Solutions**

### **"No OTP found for this phone"**
- OTP expired (older than 10 minutes)
- Phone never requested OTP
- Solution: Call `/send-otp` endpoint first

### **"Invalid OTP"**
- Wrong digits entered
- OTP already verified
- Solution: Request fresh OTP if needed

### **"Too many incorrect attempts"**
- 5+ failed OTP attempts
- Solution: Call `/resend-otp` to get new OTP

### **"Please verify your phone number first"**
- User trying to login before phone verification
- Solution: Complete verification first

---

## **Next Steps**

1. ✅ Run application from IDE
2. 🧪 Test with Postman using above endpoints
3. 📱 Choose SMS provider (Twilio recommended)
4. 🔐 Enable real SMS in production
5. 📊 Monitor OTP metrics

Enjoy! 🎉

