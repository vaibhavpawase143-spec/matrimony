# Components

This folder contains React components used across the application.

## Structure

- **Common/** - Reusable UI components (Button, Modal, Card, etc.)
- **Auth/** - Authentication related components (Login, Register, etc.)
- **Profile/** - User profile related components
- **Match/** - Matching/matching view components
- **Payment/** - Payment and subscription components
- **Chat/** - Real-time chat components
- **Layout/** - Layout components (Header, Sidebar, Footer, etc.)

## Example

```
components/
├── Common/
│   ├── Button.jsx
│   ├── Modal.jsx
│   ├── Card.jsx
│   └── Navbar.jsx
├── Auth/
│   ├── Login.jsx
│   ├── Register.jsx
│   └── VerifyEmail.jsx
├── Profile/
│   ├── UserProfile.jsx
│   ├── EditProfile.jsx
│   └── ProfileCard.jsx
└── Payment/
    ├── SubscriptionPlans.jsx
    ├── PaymentForm.jsx
    └── PaymentSuccess.jsx
```

## Notes

- Keep components focused and single-responsibility
- Use PropTypes for prop validation
- Use functional components with hooks
- Keep styling in separate CSS files or CSS modules

