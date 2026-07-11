# 🌸 Gathbandhan Matrimony Frontend

A beautiful, modern matrimony application frontend built with React, Vite, and shadcn/ui components.

## 🚀 Quick Start

### Prerequisites
- Node.js 18+ installed
- Modern web browser

### Start the Application

#### Option 1: One-Click Start (Windows)
```bash
# Double-click this file:
start-frontend.bat
```

#### Option 2: Manual Start
```bash
# Install dependencies
npm install

# Start development server
npm run dev
```

#### Option 3: Using Package Manager
```bash
# Using npm
npm run dev

# Using yarn (if installed)
yarn dev

# Using pnpm (if installed)
pnpm dev
```

### Access the Application
- **URL**: http://localhost:5173
- **Status**: Development server with hot reload

## 🎯 Features

### ✅ Working Features
- **User Registration** - Form validation and mock signup
- **User Login** - Authentication simulation
- **Dashboard** - Statistics and overview
- **Profile Management** - View and edit profiles
- **Partner Search** - Advanced filtering system
- **Profile Details** - Detailed profile viewing
- **Interest System** - Send/receive interests
- **Messaging** - Chat interface (mock data)
- **Settings** - Account preferences
- **Responsive Design** - Mobile-friendly UI

### 🎨 UI Components
- Modern, clean interface
- Beautiful color scheme
- Smooth animations
- Toast notifications
- Loading states
- Form validation
- Search filters

## 📁 Project Structure

```
src/
├── components/          # Reusable UI components
│   ├── Navbar.jsx      # Navigation bar
│   ├── DashboardStats.jsx  # Statistics cards
│   ├── ProfileForm.jsx     # Profile editing form
│   ├── SearchForm.jsx      # Search filters
│   └── ui/             # shadcn/ui components
├── pages/               # Page components
│   ├── Home.jsx        # Dashboard page
│   ├── Login.jsx       # Login page
│   ├── Register.jsx    # Registration page
│   ├── Search.jsx      # Partner search
│   ├── ProfileDetails.jsx  # Profile details
│   ├── Messages.jsx    # Messaging interface
│   ├── Matches.jsx     # User matches
│   ├── Account.jsx     # Account management
│   └── SettingsPage.jsx  # Settings page
├── hooks/               # Custom React hooks
│   └── useAuth.jsx     # Authentication context
├── assets/              # Static assets
│   └── *.jpg          # Profile images
└── index.css           # Global styles
```

## 🛠️ Technologies Used

### Frontend Stack
- **React 18** - UI framework
- **Vite** - Build tool and dev server
- **React Router v6** - Client-side routing
- **shadcn/ui** - Component library
- **TailwindCSS** - Styling framework
- **Lucide React** - Icon library
- **React Hook Form** - Form management
- **Zod** - Schema validation

### Development Tools
- **ESLint** - Code linting
- **PostCSS** - CSS processing
- **Autoprefixer** - CSS vendor prefixes

## 🎮 How to Use

### 1. Registration
- Click "Register" on the homepage
- Fill in your details (name, email, password)
- Click "Create Account"

### 2. Login
- Use your registered credentials
- Click "Login" to access the dashboard

### 3. Search Partners
- Go to "Search" in the navigation
- Apply filters (age, religion, location, etc.)
- Browse matching profiles

### 4. View Profiles
- Click on any profile card
- View detailed information
- Send interest or shortlist

### 5. Messaging
- Navigate to "Messages"
- View conversation list
- Send messages to matches

## 🎨 Customization

### Change Colors
Edit `src/index.css` and modify CSS variables:
```css
:root {
  --primary: 270 60% 35%;
  --secondary: 290 55% 45%;
  --accent: 330 65% 55%;
}
```

### Add New Pages
1. Create component in `src/pages/`
2. Add route in `src/App.jsx`
3. Update navigation in `src/components/Navbar.jsx`

### Modify Components
- All components are in `src/components/`
- Follow the existing naming convention
- Use shadcn/ui components for consistency

## 🔧 Development

### Available Scripts
```bash
npm run dev      # Start development server
npm run build    # Build for production
npm run preview  # Preview production build
npm run lint     # Run ESLint
```

### Code Style
- Use functional components with hooks
- Follow React best practices
- Maintain component reusability
- Keep styles with TailwindCSS

## 📱 Browser Support

- ✅ Chrome 90+
- ✅ Firefox 88+
- ✅ Safari 14+
- ✅ Edge 90+

## 🚀 Deployment

### Build for Production
```bash
npm run build
```

### Deploy to Static Hosting
- Netlify
- Vercel
- GitHub Pages
- Any static hosting service

## 🐛 Troubleshooting

### Common Issues

#### Port Already in Use
```bash
# Kill process on port 5173
netstat -ano | findstr :5173
taskkill /PID <PID> /F
```

#### Module Not Found
```bash
# Clear cache and reinstall
npm cache clean --force
rm -rf node_modules
npm install
```

#### Build Errors
```bash
# Check Node.js version
node --version  # Should be 18+

# Update dependencies
npm update
```

## 📞 Support

If you encounter issues:
1. Check the browser console for errors
2. Verify Node.js version is 18+
3. Clear browser cache
4. Reinstall dependencies

## 🎉 Enjoy!

Your Gathbandhan matrimony frontend is ready to use! The application features a modern, responsive design with smooth user experience and all essential matrimony features.

**Happy coding!** 🌸
