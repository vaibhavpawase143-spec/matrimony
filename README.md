# 💍 Matrimony App - Full Stack

A complete matrimonial application platform built with **Spring Boot** (backend) and **React** (frontend).

## 📁 Project Structure

```
matrimony-app/
├── backend/                    # Spring Boot Backend
│   ├── src/main/java/         # Java source code
│   ├── src/main/resources/    # Configuration files
│   ├── pom.xml                # Maven configuration
│   └── mvnw                   # Maven wrapper
│
├── frontend/                   # React Frontend
│   ├── src/                   # React source code
│   ├── public/                # Static files
│   ├── package.json           # NPM configuration
│   └── .env.local             # Environment variables (local)
│
└── README.md                  # This file
```

## 🚀 Quick Start

### Prerequisites
- **Java 17+** (for backend)
- **Node.js 16+** (for frontend)
- **PostgreSQL** (database)
- **Git** (version control)

### Backend Setup (Spring Boot)

```bash
# Navigate to backend
cd backend

# Setup environment variables
export RAZORPAY_KEY_ID=rzp_test_your_key_id
export RAZORPAY_KEY_SECRET=rzp_test_your_key_secret
export STRIPE_API_KEY=sk_test_your_key  # If using Stripe

# Run the application
./mvnw spring-boot:run

# Application runs on: http://localhost:9090
```

### Frontend Setup (React)

```bash
# Navigate to frontend
cd frontend

# Install dependencies
npm install

# Create .env.local file
cp .env.example .env.local

# Update .env.local with your backend URL
# REACT_APP_API_URL=http://localhost:9090

# Start development server
npm start

# Application runs on: http://localhost:3000
```

## 🔑 Key Features

✅ **User Authentication**
- Registration with email verification
- JWT-based authentication
- Secure login/logout

✅ **Matrimonial Matching**
- User profiles with detailed information
- Smart matching algorithm
- Interest/shortlist management

✅ **Payment Integration**
- Razorpay payment gateway (India)
- Subscription plans (Basic, Premium, VIP)
- Secure transaction handling

✅ **Real-time Communication**
- WebSocket-based chat
- Live notifications
- User activity tracking

✅ **Admin Dashboard**
- User management
- Subscription management
- Activity monitoring

## 🛠 API Endpoints

### Authentication
```
POST   /api/users/register      - Register new user
POST   /api/users/login         - User login
GET    /api/users/verify        - Verify email
```

### User Profiles
```
GET    /api/users/profile/{id}  - Get user profile
PUT    /api/users/profile/{id}  - Update profile
GET    /api/users/search        - Search users
```

### Matching
```
GET    /api/matches/{userId}    - Get matched profiles
GET    /api/recommendation/{userId} - Get recommendations
```

### Payments
```
GET    /api/subscription/plans  - Get subscription plans
POST   /api/razorpay/create-order - Create payment order
POST   /api/razorpay/verify-payment - Verify payment
```

## 🔐 Security Features

- ✅ CORS enabled for frontend
- ✅ JWT token-based authentication
- ✅ Role-based access control (RBAC)
- ✅ Password encryption (BCrypt)
- ✅ Rate limiting on API endpoints
- ✅ HTTPS ready for production

## 📦 Tech Stack

### Backend
- **Framework**: Spring Boot 3.2.5
- **Language**: Java 17
- **Database**: PostgreSQL
- **Payment**: Razorpay
- **Authentication**: JWT
- **API Docs**: Swagger/OpenAPI

### Frontend
- **Framework**: React 18+
- **Code**: JavaScript/CSS
- **Build**: Create React App / Vite
- **State Management**: React Hooks / Redux (if needed)
- **HTTP Client**: Fetch API / Axios

## 🌐 Deployment

### Backend Deployment
```bash
# Build the application
./mvnw clean package

# Deploy JAR file
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### Frontend Deployment
```bash
# Build for production
npm run build

# Deploy build/ folder to CDN or server
```

## 📝 Environment Variables

### Backend (.env or application.properties)
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/matrimony
spring.datasource.username=postgres
spring.datasource.password=your_password

razorpay.api.key=your_razorpay_key
razorpay.api.secret=your_razorpay_secret

app.base-url=https://yourdomain.com
```

### Frontend (.env.local)
```
REACT_APP_API_URL=http://localhost:9090
REACT_APP_RAZORPAY_KEY_ID=your_razorpay_public_key
REACT_APP_WS_URL=ws://localhost:9090/ws
```

## 🤝 Contributing

1. Create a new branch: `git checkout -b feature/your-feature`
2. Make your changes
3. Commit: `git commit -m "Add your feature"`
4. Push: `git push origin feature/your-feature`
5. Create a Pull Request

## 📋 Git Workflow

### Backend Development
```bash
git checkout backend
git pull origin backend
# Make changes
git commit -m "Message"
git push origin backend
```

### Frontend Development
```bash
git checkout frontend
git pull origin frontend
# Make changes
git commit -m "Message"
git push origin frontend
```

### Integration
```bash
git checkout main
git merge backend
git merge frontend
git push origin main
```

## 🐛 Troubleshooting

### Backend won't start
```bash
# Clear Maven cache
rm -rf ~/.m2/repository

# Rebuild
./mvnw clean install
./mvnw spring-boot:run
```

### Frontend API errors
- Check if backend is running on port 9090
- Verify CORS configuration in SecurityConfig.java
- Check browser console for detailed errors

### Database connection issues
```bash
# Verify PostgreSQL is running
# Check database credentials in application.properties
# Ensure database 'matrimony' exists
```

## 📞 Support

For issues or questions:
1. Check the documentation
2. Review GitHub Issues
3. Contact the development team

## 📄 License

This project is proprietary and confidential.

---

**Last Updated**: May 4, 2026
**Version**: 1.0.0
