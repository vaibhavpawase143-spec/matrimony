# Gathbandhan Platform - Environment Setup & Security Guide

This document defines the centralized environment configuration strategy for the Gathbandhan platform across the Backend, User Frontend, and Admin Frontend.

---

## 1. Environment Architecture Overview

The platform uses a single centralized template file (`.env.example`) at the root of the workspace. System configuration is strictly isolated into three domains:

```
                            .env.example
                                 │
             ┌───────────────────┼───────────────────┐
             ↓                   ↓                   ↓
          Backend          User Frontend      Admin Frontend
             │                   │                   │
      ALL Backend Secrets   VITE_* Public Only  VITE_* Public Only
```

### Key Principles:
- **Centralization**: All environment variable placeholders are documented in root `.env.example`.
- **Secret Isolation**: Backend secrets (`JWT_SECRET`, `DATABASE_PASSWORD`, `MAIL_PASSWORD`, `RAZORPAY_API_SECRET`, `RABBITMQ_PASSWORD`, `BULK_EMAIL_API_KEY`, `RECAPTCHA_SECRET_KEY`, `PROKERALA_CLIENT_SECRET`) are accessible **ONLY** by the Spring Boot Backend.
- **Frontend Scope**: Frontend applications receive **ONLY** public `VITE_*` variables.
- **Security Rule**: No secrets are ever exposed to Vite or packaged into browser JavaScript bundles.

---

## 2. Environment Variables Reference

### A. Backend Variables (Backend Only - Confidential)
Configured in `application-prod.properties` via environment variable substitution `${VARIABLE}`.

| Variable Name | Description | ConfidentialITY |
| :--- | :--- | :--- |
| `SERVER_PORT` | Spring Boot HTTP port (Default: `9090`) | Internal |
| `JWT_SECRET` | 256-bit+ HMAC SHA key for signing JWT tokens | **CRITICAL SECRET** |
| `DATABASE_URL` | PostgreSQL JDBC connection URL | Secret |
| `DATABASE_USERNAME` | PostgreSQL database username | Secret |
| `DATABASE_PASSWORD` | PostgreSQL database user password | **CRITICAL SECRET** |
| `HIKARI_MAX_POOL_SIZE` | Connection pool limit for HikariCP (Default: `50`) | Performance Tuning |
| `REDIS_HOST` | Redis cache hostname/IP | Internal |
| `REDIS_PORT` | Redis server port (Default: `6379`) | Internal |
| `REDIS_PASSWORD` | Redis authentication password | **SECRET** |
| `MAX_FILE_SIZE` | Max file upload limit (Default: `10MB`) | Internal |
| `MAX_REQUEST_SIZE` | Max multipart request limit (Default: `10MB`) | Internal |
| `FILE_UPLOAD_DIR` | Disk path for user avatar and document storage | Internal |
| `MAIL_HOST` | SMTP server host | Secret |
| `MAIL_PORT` | SMTP port (Default: `587`) | Internal |
| `MAIL_USERNAME` | SMTP account email | Secret |
| `MAIL_PASSWORD` | SMTP authentication app password | **CRITICAL SECRET** |
| `MAIL_FROM` | Default sender email address | Public/Internal |
| `BACKEND_URL` | Fully qualified backend URL | Internal |
| `FRONTEND_URL` | Allowed CORS origin & user website URL | Internal |
| `BASE_URL` | Base service URL | Internal |
| `SMS_ENABLED` | Toggle for SMS dispatches (`true`/`false`) | Internal |
| `MSG91_AUTH_KEY` | MSG91 SMS gateway API auth key | **SECRET** |
| `RAZORPAY_API_KEY` | Razorpay public key ID | Public |
| `RAZORPAY_API_SECRET` | Razorpay private API key secret | **CRITICAL SECRET** |
| `PROKERALA_CLIENT_ID` | Prokerala Kundli API Client ID | Secret |
| `PROKERALA_CLIENT_SECRET` | Prokerala Kundli API Client Secret | **CRITICAL SECRET** |
| `PROKERALA_BASE_URL` | Prokerala API base URL | Internal |
| `RECAPTCHA_ENABLED` | Toggle for Google reCAPTCHA verification | Internal |
| `RECAPTCHA_SITE_KEY` | Public reCAPTCHA v3 site key | Public |
| `RECAPTCHA_SECRET_KEY` | Private reCAPTCHA v3 verification secret | **CRITICAL SECRET** |
| `BULK_EMAIL_API_KEY` | Real transactional email provider API key | **CRITICAL SECRET** |
| `BULK_EMAIL_PROVIDER_NAME` | Provider identification header | Internal |
| `RABBITMQ_HOST` | RabbitMQ broker host | Internal |
| `RABBITMQ_PORT` | RabbitMQ AMQP port (Default: `5672`) | Internal |
| `RABBITMQ_USERNAME` | RabbitMQ username | Secret |
| `RABBITMQ_PASSWORD` | RabbitMQ user password | **CRITICAL SECRET** |
| `RABBITMQ_VHOST` | RabbitMQ virtual host | Internal |

---

### B. User Frontend Variables (Browser Public)
Consumed by Vite during build time for the User web application.

| Variable Name | Description | Usage |
| :--- | :--- | :--- |
| `VITE_API_BASE_URL` | Base API URL or proxy path (`/api`) | `apiClient`, `chatApi`, `websocket` |
| `VITE_WS_URL` | WebSocket STOMP endpoint (`/ws`) | SockJS & STOMP live dispatches |
| `VITE_BACKEND_URL` | Backend base origin for relative images | Avatar & photo resolution |
| `VITE_RECAPTCHA_SITE_KEY` | Public reCAPTCHA v3 Site Key | `GoogleReCaptchaProvider` in `main.jsx` |
| `VITE_RAZORPAY_KEY` | Public Razorpay Key ID | Checkout modal |
| `VITE_GA_MEASUREMENT_ID` | Google Analytics Measurement ID | `ReactGA` tracker |

---

### C. Admin Frontend Variables (Browser Public)
Consumed by Vite during build time for the Admin Portal.

| Variable Name | Description | Usage |
| :--- | :--- | :--- |
| `VITE_API_BASE_URL` | Admin API base path (`/api`) | Admin `apiClient` |
| `VITE_WS_URL` | Admin WebSocket endpoint (`/ws`) | Admin notification tracker |
| `VITE_BACKEND_URL` | Backend base origin for admin images | `imageUtils.js` |

---

## 3. Security Warning: Vite Environment Variables

> [!CAUTION]
> All variables prefixed with `VITE_` are embedded into client-side JavaScript bundles during `npm run build`.
> **NEVER prefix backend secrets (database passwords, private keys, API secrets, JWT secrets) with `VITE_`.** Doing so exposes those credentials to any user inspecting browser source code.

---

## 4. Local Development Setup & Git Tracking Rules

- **`.env.example`**: **TRACKED by Git**. Serves as the centralized, safe template with dummy placeholders.
- **`.env`**: **IGNORED by Git**. Contains local developer or runtime secret values and must **NEVER** be committed.

1. Copy `.env.example` to local `.env`:
   ```bash
   cp .env.example .env
   ```
2. Adjust local `.env` with developer-specific credentials (e.g. local PostgreSQL password, local RabbitMQ port).
3. Local `.env` is automatically ignored by Git via `.gitignore`. **Do NOT commit any `.env` file containing real or local values**.

---

## 5. Production Deployment Workflow

### Backend (Spring Boot VPS Deployment)
1. Set production environment variables directly on the target VPS host or container runner (systemd service file, Docker environment, or Vault/AWS Secrets Manager).
2. Launch the packaged backend JAR with the `prod` profile:
   ```bash
   java -Dspring.profiles.active=prod -jar demo-0.0.1-SNAPSHOT.jar
   ```
3. `application-prod.properties` automatically resolves all `${VARIABLE}` parameters from the environment.

### User & Admin Frontends (Vite Build)
1. Build the production React static assets by passing production `VITE_*` parameters:
   ```bash
   # User Frontend
   cd frontend
   VITE_API_BASE_URL="https://api.gathbandhan.com/api" VITE_WS_URL="https://api.gathbandhan.com/ws" npm run build

   # Admin Frontend
   cd "admin panel/my-react-app"
   VITE_API_BASE_URL="https://api.gathbandhan.com/api" VITE_WS_URL="https://api.gathbandhan.com/ws" npm run build
   ```
2. Deploy generated `dist/` build folders to your web server (Nginx/Cloudflare Pages).
