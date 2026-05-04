# 🚀 Quick Reference - Test with Your Phone

**Your Phone:** `+917666084107`  
**Port:** `9090`  
**Email:** `vaibhav@example.com`  

---

## **5-Minute Testing Flow**

### **1️⃣ Register (Postman)**
```
POST http://localhost:9090/api/users/register

{
  "firstName": "Vaibhav",
  "lastName": "Pawase",
  "email": "vaibhav@example.com",
  "phone": "+917666084107",
  "password": "password123"
}
```
✅ Check console for OTP

### **2️⃣ Copy OTP from Console**
```
Look for: "Your OTP: 456789"
Copy: 456789
```

### **3️⃣ Verify OTP (Postman)**
```
POST http://localhost:9090/api/users/verify-otp?phone=%2B917666084107&otp=456789
```
Replace `456789` with your actual OTP

### **4️⃣ Verify Email**
- Check email: `vaibhav@example.com`
- Click link or use API

### **5️⃣ Login (Postman)**
```
POST http://localhost:9090/api/users/login

{
  "email": "vaibhav@example.com",
  "password": "password123"
}
```
✅ Get JWT token!

---

## **Console Output Example**

```
📱 [MOCK SMS - OTP CODE] Your OTP: 789456 (Valid for 10 minutes)
✅ OTP sent to phone: +917666084107
```

---

## **Files Created for You**

✅ `TESTING_GUIDE_YOUR_PHONE.md` - Full detailed guide  
✅ `Postman_YOUR_PHONE.json` - Import in Postman (pre-filled)  
✅ `QUICK_REFERENCE.md` - This file  

---

## **Postman Import Steps**

1. Open Postman
2. Top menu → **File** → **Import**
3. Choose **Postman_YOUR_PHONE.json**
4. Click **Import**
5. All requests ready with your phone! 🎉

---

## **Troubleshooting**

| Error | Fix |
|-------|-----|
| No OTP found | Run Step 1 first (register) |
| OTP expired | Wait 10 min or run OPTIONAL: Send New OTP |
| Invalid OTP | Copy exact OTP from console |
| Port already in use | Change port in `application.properties` |
| Email not sent | Check spam folder or use API option |

---

**Start with Step 1 in Postman!** 🚀

