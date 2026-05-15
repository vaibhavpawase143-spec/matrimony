# 📱 Testing Mobile OTP Verification with Your Phone Number

## **Your Phone Number: 7666084107**

**Full Format:** `+917666084107`

---

## **🧪 Complete Testing Flow in Postman**

### **Step 1️⃣: Register Your Account**

**Request:**
```http
POST http://localhost:9090/api/users/register
Content-Type: application/json
```

**Body:**
```json
{
  "firstName": "Vaibhav",
  "lastName": "Pawase",
  "email": "vaibhav@example.com",
  "phone": "+917666084107",
  "password": "password123"
}
```

**Expected Response:**
```json
{
  "success": true,
  "message": "User registered successfully. Please verify your email and phone number.",
  "data": {
    "id": 1,
    "firstName": "Vaibhav",
    "lastName": "Pawase",
    "email": "vaibhav@example.com",
    "phone": "+917666084107",
    "emailVerified": false,
    "phoneVerified": false
  }
}
```

✅ **What happens:**
- User created in database
- Email verification link sent to `vaibhav@example.com`
- **OTP sent to `+917666084107`** (check console!)

---

## **⏳ Check Console for OTP**

When you register, check the **IDE Console** output:

```
📱 [MOCK SMS - OTP CODE] Your OTP: XXXXXX (Valid for 10 minutes)
```

**Example Output:**
```
📱 [MOCK SMS - OTP DISABLED] Phone: +917666084107
📱 [MOCK SMS - OTP CODE] Your OTP: 456789 (Valid for 10 minutes)
```

💡 **Copy this 6-digit OTP number** (e.g., `456789`)

---

### **Step 2️⃣: Verify OTP with Your Phone Number**

**Request:**
```http
POST http://localhost:9090/api/users/verify-otp?phone=%2B917666084107&otp=456789
```

Or without URL encoding:
```http
POST http://localhost:9090/api/users/verify-otp?phone=+917666084107&otp=456789
```

**Where:**
- `phone` = `+917666084107` (your phone number)
- `otp` = `456789` (from console - replace with actual OTP)

**Expected Response:**
```json
{
  "success": true,
  "message": "Phone verified successfully",
  "data": null
}
```

✅ **Phone verification complete!** Now you need to verify email.

---

### **Step 3️⃣: Verify Email**

Two options:

**Option A: Click Email Link (Recommended)**
- Check your email inbox for: `vaibhav@example.com`
- Look for subject: **"Verify your account"**
- Click the verification link in the email

**Option B: Use API (If you didn't receive email)**

Get the token from console output and use:
```http
GET http://localhost:9090/api/users/verify?token=YOUR_EMAIL_TOKEN_HERE
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Email verified successfully",
  "data": null
}
```

✅ **Email verification complete!** Now both are verified.

---

### **Step 4️⃣: Login with Your Credentials**

**Request:**
```http
POST http://localhost:9090/api/users/login
Content-Type: application/json
```

**Body:**
```json
{
  "email": "vaibhav@example.com",
  "password": "password123"
}
```

**Expected Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ2YWliaGF2QGV4YW1wbGUuY29tIiwiaWF0IjoxNjcwMDAwMDAwfQ.xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
}
```

✅ **Login successful!** You got JWT token!

---

## **Postman Collection: Copy-Paste Ready**

### **Request 1: Register**
```
POST http://localhost:9090/api/users/register
Content-Type: application/json

{
  "firstName": "Vaibhav",
  "lastName": "Pawase",
  "email": "vaibhav@example.com",
  "phone": "+917666084107",
  "password": "password123"
}
```

### **Request 2: Send OTP (Optional - Auto-sent in Register)**
```
POST http://localhost:9090/api/users/send-otp?phone=%2B917666084107
```

### **Request 3: Verify OTP**
```
POST http://localhost:9090/api/users/verify-otp?phone=%2B917666084107&otp=456789
```

Replace `456789` with the OTP from console!

### **Request 4: Verify Email**
```
GET http://localhost:9090/api/users/verify?token=YOUR_EMAIL_TOKEN
```

Get token from email link or console!

### **Request 5: Login**
```
POST http://localhost:9090/api/users/login
Content-Type: application/json

{
  "email": "vaibhav@example.com",
  "password": "password123"
}
```

---

## **Step-by-Step in Postman UI**

### **1. Create New Request - Register**
- Click **+ New Tab**
- Select **POST**
- URL: `http://localhost:9090/api/users/register`
- Tab: **Body** → **raw** → **JSON**
- Paste the register JSON
- Click **Send**
- ✅ Save response for later

### **2. Check IDE Console**
- Switch to IDE window
- Look at **Console tab**
- Find the line with your OTP: `Your OTP: 456789`
- **Copy the 6-digit code**

### **3. Create New Request - Verify OTP**
- Click **+ New Tab**
- Select **POST**
- URL: `http://localhost:9090/api/users/verify-otp?phone=%2B917666084107&otp=456789`
- Replace `456789` with your actual OTP from console
- Click **Send**
- ✅ Should get success response

### **4. Verify Email**
- Check your email inbox
- Find email from your mail server
- Click verification link (or use API endpoint)

### **5. Create New Request - Login**
- Click **+ New Tab**
- Select **POST**
- URL: `http://localhost:9090/api/users/login`
- Tab: **Body** → **raw** → **JSON**
- Paste login JSON
- Click **Send**
- ✅ Get JWT token!

---

## **🔧 Alternative Phone Numbers to Test**

You can also test with these Indian phone numbers:

```
+919876543210  (Example 1)
+918765432109  (Example 2)
+917666084107  (YOUR NUMBER)
```

All follow Indian format: `+91XXXXXXXXXX`

---

## **Debugging Tips**

### **Issue: "No OTP found for this phone"**
✅ **Solution:** Call `/send-otp` first or register again

### **Issue: "OTP expired"**
✅ **Solution:** OTP valid for 10 minutes only, request new one

### **Issue: "Too many incorrect attempts"**
✅ **Solution:** Called `/resend-otp` to get fresh OTP

### **Issue: "Invalid OTP"**
✅ **Solution:** Check console again, copy exact 6 digits

### **Issue: "Please verify your phone number first"**
✅ **Solution:** You skipped Step 2 (verify OTP)

### **Issue: "Please verify your email first"**
✅ **Solution:** You skipped Step 3 (verify email)

---

## **Console Output Explained**

When you register with phone `+917666084107`, you'll see:

```
📱 [MOCK SMS - OTP DISABLED] Phone: +917666084107
📱 [MOCK SMS - OTP CODE] Your OTP: 123456 (Valid for 10 minutes)
✅ OTP sent to phone: +917666084107
```

**Breaking it down:**
- 📱 = Mobile SMS (mock)
- Phone: Your phone number
- Your OTP: The 6-digit code you need
- ✅ = Confirmation message

---

## **Expected Timeline**

```
0s   → Register in Postman
0s   → OTP appears in console
1s   → Verify OTP
2s   → Email sent (check inbox)
3s   → Verify email
4s   → Call login
5s   → JWT token received ✅
```

---

## **Quick Test Checklist**

- [ ] Application running on `http://localhost:9090`
- [ ] IDE Console visible
- [ ] Postman open
- [ ] Phone ready to copy OTP from console
- [ ] Email account to verify (check spam folder!)
- [ ] All 5 requests in Postman

---

## **Sample Complete Flow**

```
1️⃣ REGISTER
   Email: vaibhav@example.com
   Phone: +917666084107
   → Response: Success

2️⃣ CHECK CONSOLE
   Output: "Your OTP: 789456"
   → Copy: 789456

3️⃣ VERIFY OTP
   Phone: +917666084107
   OTP: 789456
   → Response: Success

4️⃣ VERIFY EMAIL
   Check: vaibhav@example.com inbox
   Click: Email link
   → Response: Success

5️⃣ LOGIN
   Email: vaibhav@example.com
   Password: password123
   → Response: JWT Token ✅
```

---

## **Postman Environment Variables (Optional)**

Create variables for easier testing:

```
BASE_URL: http://localhost:9090
PHONE: +917666084107
EMAIL: vaibhav@example.com
PASSWORD: password123
OTP: {{paste_from_console}}
TOKEN: {{paste_from_register_response}}
```

Then in requests:
```
{{BASE_URL}}/api/users/register
```

---

## **Real SMS Integration (Future)**

Once you enable Twilio/AWS SNS:
- OTP will be sent to your real phone
- You'll receive actual SMS
- No need to check console
- Same testing flow applies

---

**Ready to test? Start with the Register request! 🚀**

**Questions?** Check the response messages or console output.

