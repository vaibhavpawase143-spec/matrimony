# 🚀 Production Email Setup Guide

## 📧 Why Gmail Doesn't Work for External Emails

**Gmail has strict security policies:**
- ❌ Blocks emails to non-Gmail addresses
- ❌ Daily limit: 500 emails (free accounts)
- ❌ Emails often go to spam
- ❌ Requires account verification for bulk sending

## ✅ Production Email Solutions

### 1️⃣ **SendGrid** (Recommended - Free tier: 100 emails/day)

#### Setup Steps:
1. **Sign up:** https://sendgrid.com
2. **Verify your email** and phone number
3. **Create API Key:**
   - Go to Settings → API Keys
   - Create "Full Access" API Key
   - Copy the API Key

#### Configuration:
```properties
# Comment out Gmail config and uncomment SendGrid
# spring.mail.host=smtp.sendgrid.net
# spring.mail.port=587
# spring.mail.username=apikey
# spring.mail.password=YOUR_SENDGRID_API_KEY_HERE
# spring.mail.properties.mail.smtp.auth=true
# spring.mail.properties.mail.smtp.starttls.enable=true
# spring.mail.properties.mail.smtp.starttls.required=true

# Update email config
app.email.provider=sendgrid
app.email.from=noreply@gathbandhan.com
app.email.from-name=Gathbandhan Matrimony
```

#### Domain Verification (Important):
1. Go to Settings → Sender Authentication
2. Verify your domain (gathbandhan.com)
3. Add SPF, DKIM, DMARC records

---

### 2️⃣ **Mailgun** (Free tier: 5,000 emails/month)

#### Setup Steps:
1. **Sign up:** https://mailgun.com
2. **Verify domain** (recommended) or use sandbox
3. **Get SMTP credentials:**
   - Go to Sending → Domain Settings
   - Copy SMTP Host, Username, Password

#### Configuration:
```properties
# spring.mail.host=smtp.mailgun.org
# spring.mail.port=587
# spring.mail.username=YOUR_MAILGUN_SMTP_USERNAME
# spring.mail.password=YOUR_MAILGUN_SMTP_PASSWORD
# spring.mail.properties.mail.smtp.auth=true
# spring.mail.properties.mail.smtp.starttls.enable=true

app.email.provider=mailgun
app.email.from=noreply@gathbandhan.com
```

---

### 3️⃣ **AWS SES** (Free tier: 62,000 emails/month)

#### Setup Steps:
1. **Sign up:** https://aws.amazon.com/ses/
2. **Verify domain/email** in SES console
3. **Create SMTP credentials:**
   - Go to SMTP Settings
   - Create SMTP user in IAM
   - Get SMTP username and password

#### Configuration:
```properties
# spring.mail.host=email-smtp.us-east-1.amazonaws.com
# spring.mail.port=587
# spring.mail.username=YOUR_AWS_SES_SMTP_USERNAME
# spring.mail.password=YOUR_AWS_SES_SMTP_PASSWORD
# spring.mail.properties.mail.smtp.auth=true
# spring.mail.properties.mail.smtp.starttls.enable=true
# spring.mail.properties.mail.smtp.ssl.trust=*

app.email.provider=aws-ses
app.email.from=noreply@gathbandhan.com
```

---

### 4️⃣ **Postmark** (Transactional Email - $10/month)

#### Setup Steps:
1. **Sign up:** https://postmarkapp.com
2. **Create server** and get Server Token
3. **Verify sending domain**

#### Configuration:
```properties
# spring.mail.host=smtp.postmarkapp.com
# spring.mail.port=587
# spring.mail.username=YOUR_SERVER_TOKEN
# spring.mail.password=YOUR_SERVER_TOKEN
# spring.mail.properties.mail.smtp.auth=true
# spring.mail.properties.mail.smtp.starttls.enable=true

app.email.provider=postmark
app.email.from=noreply@gathbandhan.com
```

---

## 🧪 Testing Your Email Setup

### 1. Update Configuration
```properties
# Choose your provider and update properties
app.email.provider=sendgrid  # or mailgun, aws-ses, postmark
```

### 2. Test Email Sending
```bash
# Use Postman to test
POST http://localhost:9090/api/users/register

{
  "firstName": "Test",
  "lastName": "User",
  "email": "your-friend@example.com",
  "phone": "+919876543210",
  "password": "password123"
}
```

### 3. Check Logs
```
✅ Verification email sent successfully to: your-friend@example.com
```

---

## 📊 Email Service Features

### ✅ What's Included:
- **Rate Limiting:** 100 emails/hour per recipient
- **Professional Templates:** HTML emails with branding
- **Error Handling:** Proper exception handling
- **Multiple Providers:** Easy switching between providers
- **Deliverability:** Proper headers and authentication
- **Logging:** Comprehensive logging for debugging

### 🔧 Email Templates:
- **Verification Email:** Professional design with CTA button
- **Welcome Email:** Post-verification welcome message
- **Generic Email:** For notifications and updates

---

## 🚨 Troubleshooting

### Common Issues:

| Issue | Solution |
|-------|----------|
| **Emails going to spam** | Verify domain, add SPF/DKIM records |
| **Rate limit exceeded** | Wait 1 hour or increase limit |
| **Authentication failed** | Check API keys/credentials |
| **Domain not verified** | Complete domain verification process |
| **Emails not sending** | Check SMTP host/port settings |

### Debug Mode:
```properties
logging.level.com.example.serviceimpl.EmailServiceImpl=DEBUG
```

---

## 💰 Cost Comparison

| Provider | Free Tier | Paid Plan | Best For |
|----------|-----------|-----------|----------|
| **SendGrid** | 100/day | $20/month (50k emails) | Startups |
| **Mailgun** | 5,000/month | $35/month (50k emails) | Small businesses |
| **AWS SES** | 62,000/month | $0.10/1k emails | Enterprise |
| **Postmark** | None | $10/month (10k emails) | Transactional |

---

## 🎯 Recommendation

**For your matrimony app, I recommend SendGrid:**
- ✅ Free tier sufficient for testing
- ✅ Easy setup and management
- ✅ Good deliverability
- ✅ Professional features

**Quick Start:**
1. Sign up at SendGrid
2. Get API key
3. Update `application.properties`
4. Test with your friend's email

---

## 📞 Need Help?

If you need help setting up any provider, just let me know which one you prefer and I'll guide you through the complete setup!

**Your emails will now reach any email address!** 🎉
