# 🚀 PRODUCTION DEPLOYMENT REQUIREMENTS AUDIT
**Project Name:** Gathbandhan Matrimonial Application  
**Audit Date:** September 2026  
**Audit Scope:** Complete Full-Stack Platform (Spring Boot Backend, User React Frontend, Admin React Frontend, PostgreSQL Database, Redis Cache, RabbitMQ Broker, Security, Infrastructure & VPS Sizing)  
**Execution Status:** Inspection Only (Zero files modified, Zero active deployments made)

---

## EXECUTIVE SUMMARY & ARCHITECTURAL STATUS

The Gathbandhan Matrimony platform is an enterprise-scale, multi-tier web application designed for high-concurrency user engagement, real-time messaging, scheduled automated workflows, and bulk broadcast capabilities.

### Current Deployment Topology:
- **Backend:** Spring Boot 3.2.5 (Java 17) monolithic REST & STOMP WebSocket server with HikariCP, JPA/Hibernate, Spring Security (JWT), and Flyway database migrations.
- **Frontends (Two Independent SPAs):**
  1. **User Client:** React 18.3.1 SPA built with Vite 5.4.19, Tailwind CSS 3.4.17, Radix UI, SockJS, and STOMP client.
  2. **Admin Portal:** React 19.2.7 SPA built with Vite 8.1.1, Tailwind CSS 4.3.3, Redux Toolkit, and reporting tools (jsPDF, xlsx).
- **Current Docker Status:** **PARTIALLY DOCKERIZED (INFRASTRUCTURE ONLY)**.
  - Confirmed from system state: Only Redis (`redis:7`) and RabbitMQ (`rabbitmq:3-management`) are running as Docker containers.
  - The Spring Boot Backend and React Frontends currently have **NO Dockerfiles** and **NO docker-compose files** in the codebase.
- **Production Readiness Status:** **ACTION REQUIRED BEFORE DEPLOYMENT**. 14 critical production blockers were discovered in the codebase (hardcoded localhost URLs, hardcoded Redis hosts, hardcoded credentials, missing Prometheus dependencies, and rate-limiting proxy IP resolution bugs).

---

# SECTION A: EXACT TECHNOLOGY STACK

### 1. Backend Layer [CONFIRMED FROM CODE]
- **Language / Runtime:** Java 17 (OpenJDK / Temurin 17.0.x)
- **Framework:** Spring Boot `3.2.5` (`spring-boot-starter-parent`)
- **Build System:** Apache Maven `3.9.6` (Maven Wrapper `mvnw` / `mvnw.cmd` bundled)
- **Persistence & ORM:** Spring Data JPA / Hibernate 6.x
- **Database Driver:** PostgreSQL JDBC Driver (`org.postgresql:postgresql:42.6.x` runtime)
- **Database Migration:** Flyway Core & Flyway Maven Plugin `9.22.3`
- **Security:** Spring Security 6.x with Stateless JWT (`io.jsonwebtoken:jjwt-api:0.11.5`, `jjwt-impl`, `jjwt-jackson`)
- **Real-Time Communication:** Spring Boot Starter WebSocket (`spring-boot-starter-websocket`) with STOMP broker and SockJS
- **Message Broker Client:** Spring AMQP / Spring Rabbit (`spring-boot-starter-amqp`)
- **Cache & Key-Value:** Spring Data Redis (`spring-boot-starter-data-redis` Lettuce client)
- **Reactive HTTP Client:** Spring WebFlux (`spring-boot-starter-webflux` / Project Reactor / Netty HttpClient) for Google reCAPTCHA v3 and Prokerala API
- **Document & Spreadsheet Processing:** Apache POI (`org.apache.poi:poi-ooxml:5.4.1`)
- **Payment Gateway SDK:** Razorpay Java SDK (`com.razorpay:razorpay-java:1.4.8`)
- **Email Engines:** Spring Boot Mail (`spring-boot-starter-mail` Jakarta Mail) + Thymeleaf (`spring-boot-starter-thymeleaf`) + SendGrid REST API
- **Monitoring & Metrics:** Spring Boot Starter Actuator (`spring-boot-starter-actuator`)
- **API Documentation:** SpringDoc OpenAPI Swagger UI (`org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0`)

### 2. User Frontend Layer (`frontend/`) [CONFIRMED FROM CODE]
- **Runtime:** Node.js 18+ (Node 20.x or 22.x LTS recommended)
- **Package Manager:** npm (v10+ / v11.x)
- **Core Library:** React `^18.3.1` & React DOM `^18.3.1`
- **Build Tool:** Vite `^5.4.19` (`@vitejs/plugin-react: ^4.3.1`)
- **Styling:** Tailwind CSS `^3.4.17`, Autoprefixer `^10.4.21`, PostCSS `^8.5.6`, `tailwind-merge`, `tailwindcss-animate`
- **Component UI:** Radix UI primitives (`@radix-ui/react-*`), Lucide React `^0.462.0`, Embla Carousel `^8.6.0`, Sonner, React Hot Toast
- **Routing:** React Router DOM `^6.30.1`
- **State & Data Fetching:** `@tanstack/react-query: ^5.83.0`
- **Forms & Validation:** `react-hook-form: ^7.61.1`, `@hookform/resolvers: ^3.10.0`, `zod: ^3.25.76`
- **WebSockets:** `@stomp/stompjs: ^7.3.0`, `sockjs-client: ^1.6.1`
- **Security & Bot Protection:** Google reCAPTCHA v3 (`reCaptchaKey` via Vite env)
- **Analytics:** Google Analytics 4 (`react-ga4: ^3.0.1`)

### 3. Admin Frontend Layer (`admin panel/my-react-app/`) [CONFIRMED FROM CODE]
- **Runtime:** Node.js 20+ LTS required (Vite 8 requires modern Node)
- **Core Library:** React `^19.2.7` & React DOM `^19.2.7`
- **Build Tool:** Vite `^8.1.1` (`@vitejs/plugin-react: ^6.0.3`)
- **Styling:** Tailwind CSS `^4.3.3` (`@tailwindcss/vite: ^4.3.3`, `lightningcss: ^1.33.0`)
- **Routing:** React Router DOM `^7.18.1`
- **State Management:** Redux Toolkit (`@reduxjs/toolkit: ^2.12.0`, `react-redux: ^9.3.0`)
- **Reporting & Exporting:** `xlsx: ^0.18.5`, `jspdf: ^4.2.1`, `jspdf-autotable: ^5.0.8`, `recharts: ^3.9.2`
- **WebSockets:** `@stomp/stompjs: ^7.3.0`, `sockjs-client: ^1.6.1`
- **Icons & Alerts:** `lucide-react: ^1.25.0`, `react-icons: ^5.7.0`, `react-toastify: ^11.1.0`, `sonner: ^2.0.7`

### 4. Infrastructure & Middleware [CONFIRMED FROM CODE / SYSTEM INSPECTION]
- **Database:** PostgreSQL 13+ (PostgreSQL 15 or 16 Recommended)
- **In-Memory Store:** Redis 7.x (Standalone)
- **Message Broker:** RabbitMQ 3.x (with Management Plugin)
- **Reverse Proxy / Web Server [RECOMMENDED]:** Nginx (Mainline / Stable) with HTTP/2 and WebSocket upgrade support
- **TLS/SSL [RECOMMENDED]:** Let's Encrypt (Certbot) TLS 1.2 / TLS 1.3

---

# SECTION B: EXACT SOFTWARE VERSIONS

| Component | Exact Version in Project | Status |
| :--- | :--- | :--- |
| Spring Boot | `3.2.5` | Confirmed in root `pom.xml` |
| Java Version | `17` (Target/Source) | Confirmed in root `pom.xml` |
| Maven Version | `3.9.6` | Confirmed via root `mvnw` wrapper |
| Flyway Core | `9.22.3` | Confirmed in root `pom.xml` |
| PostgreSQL JDBC | `42.6.x` (managed by Spring Boot 3.2.5) | Confirmed in root `pom.xml` |
| JJWT (JSON Web Token) | `0.11.5` | Confirmed in root `pom.xml` |
| Razorpay Java SDK | `1.4.8` | Confirmed in root `pom.xml` |
| Apache POI | `5.4.1` | Confirmed in root `pom.xml` |
| SpringDoc OpenAPI | `2.2.0` | Confirmed in root `pom.xml` |
| React (User Frontend) | `^18.3.1` | Confirmed in `frontend/package.json` |
| Vite (User Frontend) | `^5.4.19` | Confirmed in `frontend/package.json` |
| React Router (User) | `^6.30.1` | Confirmed in `frontend/package.json` |
| React (Admin Frontend) | `^19.2.7` | Confirmed in `admin panel/my-react-app/package.json` |
| Vite (Admin Frontend) | `^8.1.1` | Confirmed in `admin panel/my-react-app/package.json` |
| React Router (Admin) | `^7.18.1` | Confirmed in `admin panel/my-react-app/package.json` |
| Redux Toolkit (Admin) | `^2.12.0` | Confirmed in `admin panel/my-react-app/package.json` |
| Redis (Docker Image) | `redis:7` | Confirmed from running Docker container |
| RabbitMQ (Docker Image) | `rabbitmq:3-management` | Confirmed from running Docker container |
| Node.js (Host Runtime) | `v24.14.0` (Dev host) / **Node 20.x or 22.x LTS Recommended for VPS** | Confirmed / Recommended |
| PostgreSQL Server | PostgreSQL 15 or 16 Recommended | Recommended |

---

# SECTION C: EXACT VPS SPECIFICATION (COMPUTED AUDIT)

This sizing is **strictly calculated** from the actual application configuration in the codebase:
- **Tomcat Max Threads:** `server.tomcat.threads.max=300` (each 64-bit JVM thread allocates a 1MB stack = ~300MB).
- **RabbitMQ Consumer Concurrency:**
  - Critical Notifications: 5 to 15 workers (`criticalRabbitListenerContainerFactory`)
  - Bulk App Broadcast: 15 to 25 workers (`bulkRabbitListenerContainerFactory`)
  - Bulk Email Broadcast: 5 to 10 workers (`bulkEmailRabbitListenerContainerFactory`)
  - Default Container: 3 to 10 workers (`rabbitListenerContainerFactory`)
  - *Total RabbitMQ listener threads:* 40 to 60 concurrent threads (~60MB stack).
- **Spring Async Executors:**
  - `applicationTaskExecutor`: 10 to 50 threads (queue 500)
  - `criticalEmailExecutor`: 10 to 30 threads (queue 500)
  - `bulkEmailExecutor`: 5 to 15 threads (queue 10,000)
  - *Total Async Task threads:* up to 95 threads (~95MB stack).
- **Database Connection Pool (HikariCP):** `maximum-pool-size=50`, `minimum-idle=10`.
- **Scheduled Background Jobs:** 6 concurrent scheduled jobs (Broadcast engine, Subscriptions, Dashboard cache, Online status every 10s, Master data every 6h).
- **Middleware Footprint:** PostgreSQL 15/16 + Redis 7 + RabbitMQ 3.x (Erlang VM) + Nginx.

### 1. Sizing Breakdown Table

| Component | Minimum Allocation | Recommended Allocation | Technical Justification |
| :--- | :--- | :--- | :--- |
| **Spring Boot JVM (Heap)** | `2.0 GB` (`-Xms1g -Xmx2g`) | `3.5 GB` (`-Xms2g -Xmx3500m`) | Caches master data (castes, cities, religions), POI Excel reports, batch buffer of 100 items. |
| **Spring Boot JVM (Off-Heap & Stacks)** | `1.0 GB` | `1.5 GB` | 475+ active threads (Tomcat + RabbitMQ + Async), 256MB-512MB Metaspace, Netty buffers. |
| **PostgreSQL 15/16** | `1.5 GB` | `3.0 GB` | 50 Hikari connections + admin tools. `shared_buffers=512MB-1GB`, `work_mem=16MB`. |
| **RabbitMQ & Erlang VM** | `600 MB` | `1.2 GB` | Erlang runtime baseline, 6 durable exchanges, 6 durable queues + 6 DLQs, prefetch buffering. |
| **Redis 7** | `256 MB` | `512 MB` | Session tokens, sliding-window rate limit counters (`rl:*`), master cache (`master:*`). |
| **Nginx & OS Kernel** | `600 MB` | `1.0 GB` | Reverse proxy buffers, gzip compression, OS page cache, network socket buffers. |
| **Buffer / Headroom** | `2.0 GB` | `5.0 GB` | Prevents Linux Out-Of-Memory (OOM) killer from terminating PostgreSQL or Java during peak broadcast dispatches. |
| **TOTAL RAM REQUIRED** | **8 GB RAM** | **16 GB RAM** | **Absolute minimum 8GB; Recommended 16GB for 1M user operations.** |

### 2. VPS Hardware Requirements

| Resource | Minimum Specification | Recommended Specification |
| :--- | :--- | :--- |
| **Operating System** | Ubuntu 22.04 LTS or 24.04 LTS x86_64 | Ubuntu 24.04 LTS x86_64 |
| **vCPU** | **4 vCPUs** (Dedicated or high-priority) | **6 to 8 vCPUs** (Compute-optimized) |
| **RAM** | **8 GB** (Mandatory 4GB-8GB Swap file) | **16 GB** (Mandatory 8GB Swap file) |
| **Disk Storage** | **80 GB NVMe SSD** | **160 GB to 250 GB NVMe SSD** |
| **Disk I/O** | > 1,500 IOPS (NVMe required for DB & logs) | > 3,000 IOPS |
| **Swap File** | **8 GB Swap** (`/swapfile`) | **8 GB Swap** (`/swapfile`) |
| **Bandwidth / Uplink** | 100 Mbps (1 TB / month transfer) | 1 Gbps port (3 TB to 5 TB / month transfer) |

> [!WARNING]
> **Do NOT run this stack on a 2GB or 4GB VPS.**
> Between the JVM's ~475 concurrent threads (Tomcat threads: 300, RabbitMQ: 60, Async: 95), HikariCP's 50 connections, the Erlang VM for RabbitMQ, Redis, and PostgreSQL, an 8GB VPS is the strict operational floor. An unbuffered 4GB instance will experience frequent JVM crashes or PostgreSQL OOM terminates.

---

# SECTION D: EXACT DOMAIN & SUBDOMAIN REQUIREMENTS

The application codebase consists of two distinct client frontends and one backend API. Deploying both frontends under clean domains/subdomains isolates browser cookies, prevents cross-origin session pollution, and aligns with standard production architecture.

### Recommended Multi-Subdomain Architecture:
1. **User Portal (Main Web App):**
   - Primary: `https://gathbandhan.com` (or `https://yourmatrimony.com`)
   - Alias: `https://www.gathbandhan.com`
2. **Admin Portal:**
   - Primary: `https://admin.gathbandhan.com`
   *(Cleanly isolates the Admin React 19 application and its distinct JWT authentication tokens)*
3. **Backend API & WebSocket Engine:**
   - Option A (Direct Subdomain): `https://api.gathbandhan.com`
   - Option B (Reverse-Proxied Path): `https://gathbandhan.com/api` and `https://admin.gathbandhan.com/api`
   *(Option B eliminates all browser CORS pre-flight requests and avoids WebSocket cross-origin handshake restrictions)*

---

# SECTION E: EXACT DNS RECORDS

Assuming the production domain is `gathbandhan.com` and your VPS public IPv4 is `203.0.113.10`:

| Type | Name / Host | Target / Value | TTL | Purpose |
| :--- | :--- | :--- | :--- | :--- |
| **A** | `@` (root) | `203.0.113.10` | 300 / Auto | Points root domain to VPS Nginx server |
| **A** | `www` | `203.0.113.10` | 300 / Auto | User web portal www alias |
| **A** | `admin` | `203.0.113.10` | 300 / Auto | Dedicated Admin Portal SPA |
| **A** | `api` | `203.0.113.10` | 300 / Auto | Backend REST API & WebSocket endpoint (if using api subdomain) |
| **TXT** | `@` | `v=spf1 include:sendgrid.net ~all` | 3600 | SPF record for SendGrid/SMTP email deliverability |
| **CNAME**| `s1._domainkey`| `s1.domainkey.sendgrid.net` | 3600 | DKIM verification for transactional/bulk email delivery |
| **TXT** | `_dmarc` | `v=DMARC1; p=quarantine; rua=mailto:dmarc@gathbandhan.com` | 3600 | DMARC policy protecting domain reputation |

---

# SECTION F: EXACT PORTS

### 1. External Ports (Internet Facing - Open on VPS Firewall)
- **Port 80 (TCP):** HTTP (Nginx) - Automatically redirects to HTTPS (Port 443) and handles Let's Encrypt ACME challenges.
- **Port 443 (TCP):** HTTPS / WSS (Nginx) - Encrypted TLS reverse proxy for Frontend, Admin, Backend REST, and STOMP WebSocket.
- **Port 22 (TCP):** SSH - Administrative server management (strongly recommended to configure custom port or SSH key authentication only).

### 2. Internal Ports (Host Localhost / Docker Network ONLY - NEVER EXPOSE PUBLICLY)
- **Port 9090 (TCP):** Spring Boot Backend Application (`server.port=${SERVER_PORT:9090}`)
- **Port 5432 (TCP):** PostgreSQL Database (Bound to `127.0.0.1` or Docker bridge)
- **Port 6379 (TCP):** Redis In-Memory Cache (Bound to `127.0.0.1` or Docker bridge)
- **Port 5672 (TCP):** RabbitMQ AMQP Broker protocol (Internal backend queue publishing)
- **Port 15672 (TCP):** RabbitMQ Management UI (Optional: access via SSH tunnel only; NEVER open to public)
- **Port 3000 / 5173 (TCP):** Vite dev servers (Development only - **NOT used in production**; production serves static precompiled `dist/` folders via Nginx).

---

# SECTION G: EXACT DOCKER SERVICES

### Current Codebase Docker Reality:
- **No Dockerfiles exist** for the Spring Boot Backend or Frontends.
- **No `docker-compose.yml` exists** in the repository root.
- The developer's environment currently has two running containers:
  - `redis:7` (Forwarding `0.0.0.0:6379->6379/tcp`)
  - `rabbitmq:3-management` (Forwarding `0.0.0.0:5672->5672/tcp`, `0.0.0.0:15672->15672/tcp`)

### Recommended Production Docker Compose Architecture:
To achieve clean isolation on the VPS, the following services should run in Docker via `docker-compose.yml`:

```yaml
services:
  # 1. Database
  postgres:
    image: postgres:16-alpine
    container_name: gathbandhan_postgres
    restart: always
    environment:
      POSTGRES_DB: gathbandhan_prod
      POSTGRES_USER: gathbandhan_app
      POSTGRES_PASSWORD: ${DATABASE_PASSWORD}
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "127.0.0.1:5432:5432"
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U gathbandhan_app -d gathbandhan_prod"]
      interval: 10s
      timeout: 5s
      retries: 5

  # 2. Redis Cache & Token Store
  redis:
    image: redis:7-alpine
    container_name: gathbandhan_redis
    restart: always
    command: ["redis-server", "--appendonly", "yes", "--requirepass", "${REDIS_PASSWORD}"]
    volumes:
      - redis_data:/data
    ports:
      - "127.0.0.1:6379:6379"
    healthcheck:
      test: ["CMD", "redis-cli", "-a", "${REDIS_PASSWORD}", "ping"]
      interval: 10s
      timeout: 5s
      retries: 5

  # 3. RabbitMQ Message Broker
  rabbitmq:
    image: rabbitmq:3-management-alpine
    container_name: gathbandhan_rabbitmq
    restart: always
    environment:
      RABBITMQ_DEFAULT_USER: ${RABBITMQ_USERNAME}
      RABBITMQ_DEFAULT_PASS: ${RABBITMQ_PASSWORD}
      RABBITMQ_DEFAULT_VHOST: /
    volumes:
      - rabbitmq_data:/var/lib/rabbitmq
    ports:
      - "127.0.0.1:5672:5672"
      - "127.0.0.1:15672:15672"
    healthcheck:
      test: ["CMD", "rabbitmq-diagnostics", "-q", "ping"]
      interval: 15s
      timeout: 10s
      retries: 5

volumes:
  postgres_data:
  redis_data:
  rabbitmq_data:
```

---

# SECTION H: EXACT ENVIRONMENT VARIABLES

*(Variable names only — zero secret values revealed)*

### 1. Spring Boot Backend (`application-prod.properties`)
- `SERVER_PORT`: Port for Spring Boot HTTP engine (Default: `9090`)
- `SPRING_PROFILES_ACTIVE`: Must be explicitly set to `prod`
- `JWT_SECRET`: Minimum 32-character (256-bit) HMAC-SHA256 secret key
- `DATABASE_URL`: JDBC URL (e.g. `jdbc:postgresql://localhost:5432/gathbandhan_prod`)
- `DATABASE_USERNAME`: PostgreSQL user
- `DATABASE_PASSWORD`: PostgreSQL user password
- `HIKARI_MAX_POOL_SIZE`: Max Hikari connections (Default: `50`)
- `REDIS_HOST`: Redis host (`localhost` or `127.0.0.1`)
- `REDIS_PORT`: Redis port (Default: `6379`)
- `REDIS_PASSWORD`: Redis authentication password
- `MAX_FILE_SIZE`: Max single photo upload limit (Default: `10MB`)
- `MAX_REQUEST_SIZE`: Max multipart HTTP payload limit (Default: `10MB`)
- `FILE_UPLOAD_DIR`: Absolute filesystem path for uploaded media (`/opt/gathbandhan/uploads`)
- `MAIL_HOST`: SMTP server hostname (e.g. `smtp.sendgrid.net`)
- `MAIL_PORT`: SMTP port (Default: `587`)
- `MAIL_USERNAME`: SMTP user / `apikey`
- `MAIL_PASSWORD`: SMTP app password / API key
- `MAIL_FROM`: Verified sender email address (e.g. `noreply@gathbandhan.com`)
- `BACKEND_URL`: Fully qualified backend URL (`https://api.gathbandhan.com` or `https://gathbandhan.com`)
- `FRONTEND_URL`: Fully qualified user frontend URL (`https://gathbandhan.com`)
- `BASE_URL`: Base domain origin
- `SMS_ENABLED`: Boolean flag to enable/disable SMS OTP (`false` by default)
- `MSG91_AUTH_KEY`: Auth key for MSG91 SMS gateway
- `RAZORPAY_API_KEY`: Production Razorpay Key ID (`rzp_live_...`)
- `RAZORPAY_API_SECRET`: Production Razorpay Key Secret
- `PROKERALA_CLIENT_ID`: Prokerala Kundli API Client ID
- `PROKERALA_CLIENT_SECRET`: Prokerala Kundli API Client Secret
- `PROKERALA_BASE_URL`: Prokerala base URL (`https://api.prokerala.com`)
- `RECAPTCHA_ENABLED`: Boolean flag for Google reCAPTCHA v3 (`true` in production)
- `RECAPTCHA_SITE_KEY`: Public Google reCAPTCHA v3 Site Key
- `RECAPTCHA_SECRET_KEY`: Private Google reCAPTCHA v3 Secret Key
- `BULK_EMAIL_API_KEY`: Real SendGrid / transactional bulk email API Key (Fail-fast enforced in prod)
- `BULK_EMAIL_PROVIDER_NAME`: Name identifier for provider (`Gathbandhan-Bulk-Provider`)
- `RABBITMQ_HOST`: RabbitMQ host (`localhost` or `127.0.0.1`)
- `RABBITMQ_PORT`: RabbitMQ AMQP port (Default: `5672`)
- `RABBITMQ_USERNAME`: RabbitMQ user
- `RABBITMQ_PASSWORD`: RabbitMQ password
- `RABBITMQ_VHOST`: RabbitMQ Virtual Host (Default: `/`)

### 2. User Frontend Build Variables (`frontend/.env`)
- `VITE_API_BASE_URL`: Base API path (`/api`)
- `VITE_WS_URL`: WebSocket endpoint (`/ws`)
- `VITE_BACKEND_URL`: Backend origin URL (`https://gathbandhan.com` or `https://api.gathbandhan.com`)
- `VITE_RECAPTCHA_SITE_KEY`: Public Google reCAPTCHA v3 site key
- `VITE_RAZORPAY_KEY`: Public Razorpay Key ID (`rzp_live_...`)
- `VITE_GA_MEASUREMENT_ID`: Google Analytics 4 Measurement ID (`G-XXXXXXXXXX`)

### 3. Admin Frontend Build Variables (`admin panel/my-react-app/.env`)
- `VITE_API_BASE_URL`: Admin API base path (`/api`)
- `VITE_WS_URL`: Admin WebSocket endpoint (`/ws`)
- `VITE_BACKEND_URL`: Backend origin URL (`https://admin.gathbandhan.com` or `https://gathbandhan.com`)
- `VITE_RECAPTCHA_SITE_KEY`: Public Google reCAPTCHA v3 site key for admin login

---

# SECTION I: POSTGRESQL REQUIREMENTS

- **Version:** PostgreSQL 15 or 16 Recommended (Minimum PostgreSQL 13+).
- **Database Name:** `gathbandhan_prod` (or `gathbandhan_db`).
- **Encoding & Collation:** `UTF8`, `en_US.UTF-8` or `C.UTF-8`.
- **Flyway Migrations:** Exactly **139 migrations** (`V1__create_admins.sql` through `V139__add_extended_family_details.sql`).
  - In `application-prod.properties`, `spring.flyway.out-of-order=false`. Migrations **must** be applied strictly in sequence.
- **Extensions:** No proprietary PostgreSQL extensions required. All tables use standard PostgreSQL types (`BIGINT`, `VARCHAR`, `TEXT`, `BOOLEAN`, `NUMERIC`, `TIMESTAMP`, `JSONB`).
- **Connection Configuration (`postgresql.conf`):**
  - `max_connections = 150` (Hikari pool uses 50; remaining 100 for background workers, migration tasks, and admin CLI).
  - `shared_buffers = 1GB` (for 8GB VPS) or `2GB` (for 16GB VPS).
  - `work_mem = 16MB`.
  - `maintenance_work_mem = 256MB`.
  - `effective_cache_size = 3GB` (8GB VPS) or `6GB` (16GB VPS).
- **Storage & Sizing:**
  - Base schema + Master seeds (Religions, Castes, Cities, States): ~50 MB.
  - Active operations (10,000 active users + chat messages + likes): ~2 GB.
  - Broadcast scale (1,000,000 users): Each broadcast campaign generates 1,000,000 rows in `broadcast_recipient_status` (~250 MB per campaign). Retaining 20 campaigns = ~5 GB.
  - Estimated DB disk budget: 20 GB to 40 GB.
- **Deployment Mode:** Docker container (`postgres:16-alpine`) or direct VPS installation via `apt install postgresql-16`. Docker is recommended for automated volume backups and environment isolation.

---

# SECTION J: REDIS REQUIREMENTS

- **Version:** Redis 7.x.
- **Application Purpose:**
  1. **Token Revocation (`TokenRevocationService`):** Fast blacklist check on every HTTP request (`jwt:revoked:<jti>`).
  2. **Rate Limiting (`RateLimitService`):** Sliding window and atomic counters for login attempts, registration, and OTPs (`rl:*`, `rl:fail:*`).
  3. **Master Data Cache (`MasterDataCacheService`):** Caching master tables (religions, castes, professions) with 6-hour TTL.
  4. **Matches Cache (`CacheService`):** Caching user search results (`matches:*`).
- **Configuration (`redis.conf`):**
  - `maxmemory 512mb`
  - `maxmemory-policy allkeys-lru` (Evicts least recently used cache keys when memory is exhausted without affecting token operations).
  - `appendonly yes` (AOF persistence enabled to survive restarts).
  - `requirepass <STRONG_REDIS_PASSWORD>`
- **Exposure:** **NEVER expose publicly.** Must bind only to `127.0.0.1` or private Docker network.
- **VPS Resource Allocation:** 512 MB RAM, 1 CPU core shared.

---

# SECTION K: RABBITMQ REQUIREMENTS

- **Version:** RabbitMQ 3.12+ (or 3-management) with Erlang 25+.
- **Exchanges Configured [CONFIRMED FROM `RabbitMQConfig.java`]:**
  - `notification.exchange` (Direct, Durable)
  - `notification.dlx` (Direct, Durable - Dead Letter Exchange)
  - `subscription.expiry.exchange` (Direct, Durable)
  - `subscription.expiry.email.dlx` (Direct, Durable)
- **Queues Configured:**
  1. `notification.critical.queue` (Priority queue for OTP, verification emails, critical alerts)
  2. `notification.high.queue` (High-priority user actions)
  3. `notification.medium.queue` (Standard user notifications)
  4. `notification.low.queue` (Bulk in-app broadcast alerts)
  5. `notification.bulk.email.queue` (Decoupled bulk broadcast emails)
  6. `subscription.expiry.email` (Subscription renewal reminders)
  7. **DLQs:** `notification.critical.dlq`, `notification.high.dlq`, `notification.medium.dlq`, `notification.low.dlq`, `notification.bulk.email.dlq`, `subscription.expiry.email.dlq`
- **Consumer Concurrency Settings:**
  - `broadcast.rabbitmq.bulk-app.concurrency=15`, `max-concurrency=25`, `prefetch=200`
  - `broadcast.rabbitmq.bulk-email.concurrency=5`, `max-concurrency=10`, `prefetch=50`
  - `criticalRabbitListenerContainerFactory`: 5 to 15 concurrent consumers, `prefetch=1`
- **Persistence:** All queues and exchanges are declared as durable.
- **Management UI:** Bound to port 15672; accessible only via SSH port forwarding (`ssh -L 15672:localhost:15672 user@vps`).
- **Exposure:** Port 5672 and 15672 must **NEVER** be opened on the public firewall.

---

# SECTION L: NGINX REQUIREMENTS

Nginx serves as the unified reverse proxy, SSL terminator, static file server, and WebSocket gateway.

### Production Nginx Virtual Host Configuration:

```nginx
# ========================================================
# HTTP to HTTPS Global Redirect
# ========================================================
server {
    listen 80;
    listen [::]:80;
    server_name gathbandhan.com www.gathbandhan.com admin.gathbandhan.com;

    location /.well-known/acme-challenge/ {
        root /var/www/certbot;
    }

    location / {
        return 301 https://$host$request_uri;
    }
}

# ========================================================
# 1. USER FRONTEND & MAIN API (gathbandhan.com)
# ========================================================
server {
    listen 443 ssl http2;
    listen [::]:443 ssl http2;
    server_name gathbandhan.com www.gathbandhan.com;

    # SSL Certificates
    ssl_certificate /etc/letsencrypt/live/gathbandhan.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/gathbandhan.com/privkey.pem;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    ssl_prefer_server_ciphers on;

    # Maximum file upload size (Must match or exceed Spring Boot 10MB)
    client_max_body_size 15M;

    # Gzip Compression
    gzip on;
    gzip_types text/plain text/css application/json application/javascript text/xml application/xml image/svg+xml;

    # Security Headers
    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-Content-Type-Options "nosniff" always;
    add_header X-XSS-Protection "1; mode=block" always;
    add_header Referrer-Policy "strict-origin-when-cross-origin" always;

    # User Frontend SPA static build
    root /var/www/gathbandhan/frontend/dist;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    # Static Uploads Directory (Direct Nginx serving for high performance)
    location /uploads/ {
        alias /opt/gathbandhan/uploads/;
        expires 30d;
        add_header Cache-Control "public, no-transform";
        access_log off;
    }

    # Backend REST API Proxy
    location /api/ {
        proxy_pass http://127.0.0.1:9090;
        proxy_http_version 1.1;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_connect_timeout 60s;
        proxy_read_timeout 120s;
    }

    # Backend STOMP WebSocket Gateway (/ws)
    location /ws {
        proxy_pass http://127.0.0.1:9090;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_read_timeout 86400s;
        proxy_send_timeout 86400s;
    }
}

# ========================================================
# 2. ADMIN FRONTEND PORTAL (admin.gathbandhan.com)
# ========================================================
server {
    listen 443 ssl http2;
    listen [::]:443 ssl http2;
    server_name admin.gathbandhan.com;

    ssl_certificate /etc/letsencrypt/live/gathbandhan.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/gathbandhan.com/privkey.pem;
    ssl_protocols TLSv1.2 TLSv1.3;

    client_max_body_size 15M;
    gzip on;
    gzip_types text/plain text/css application/json application/javascript text/xml application/xml image/svg+xml;

    root /var/www/gathbandhan/admin/dist;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /uploads/ {
        alias /opt/gathbandhan/uploads/;
        expires 30d;
        add_header Cache-Control "public, no-transform";
    }

    location /api/ {
        proxy_pass http://127.0.0.1:9090;
        proxy_http_version 1.1;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /ws {
        proxy_pass http://127.0.0.1:9090;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        proxy_set_header Host $host;
        proxy_read_timeout 86400s;
    }
}
```

---

# SECTION M: SSL REQUIREMENTS

- **Requirement:** 100% mandatory HTTPS for all user sessions, payment processing, camera capture, and WebSockets (`wss://`).
- **Provider:** Let's Encrypt Free Automated TLS Certificates (via `certbot` & `python3-certbot-nginx`).
- **Required Domains in Certificate:**
  - `gathbandhan.com`
  - `www.gathbandhan.com`
  - `admin.gathbandhan.com`
- **Installation Command:**
  ```bash
  sudo certbot --nginx -d gathbandhan.com -d www.gathbandhan.com -d admin.gathbandhan.com
  ```
- **Auto-Renewal:** Tested via `sudo certbot renew --dry-run` and managed by systemd `certbot.timer`.
- **In-Application SSL:** In `application-prod.properties`, `server.ssl.enabled=false`. SSL is terminated at Nginx, which forwards clean unencrypted HTTP traffic to Spring Boot on `127.0.0.1:9090`.

---

# SECTION N: EMAIL PROVIDER REQUIREMENTS

The application has a dual-architecture email pipeline:

### 1. Critical Transactional Emails (`JavaMailSender`)
- **Use Cases:** Account verification (`sendVerificationEmail`), Password reset, OTP delivery.
- **Trigger:** Immediate async dispatch via `criticalEmailExecutor` (10-30 threads).
- **Protocol:** Standard SMTP (Port 587 with STARTTLS).
- **Credentials Required:** `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`, `MAIL_FROM`.
- **Recommended Provider:** SendGrid SMTP, Amazon SES, Mailgun, or Google Workspace SMTP relay.

### 2. Decoupled Bulk Broadcast Emails (`DefaultEmailProviderImpl`)
- **Use Cases:** Admin promotional broadcasts to thousands or millions of users.
- **Trigger:** RabbitMQ `notification.bulk.email.queue` -> `bulkEmailRabbitListenerContainerFactory` -> REST API call.
- **Protocol:** SendGrid v3 Mail Send HTTP REST API (`https://api.sendgrid.com/v3/mail/send`).
- **Credentials Required:** `BULK_EMAIL_API_KEY` (Bearer Token), `BULK_EMAIL_PROVIDER_NAME`.
- **CRITICAL FAIL-FAST AUDIT FINDING:**
  In `DefaultEmailProviderImpl.java:77`, if `broadcast.test-mode=false` and `app.email.bulk-api-key` is missing, empty, or set to `"simulated-bulk-api-key-prod"`, the application throws an **`IllegalStateException`**. A valid SendGrid API key is **strictly required** for production broadcasts.
- **Domain Verification:** The sender domain (`MAIL_FROM`) must be verified in SendGrid with SPF and DKIM DNS records to prevent emails being marked as spam.

---

# SECTION O: RAZORPAY REQUIREMENTS

- **Integration Mode [CONFIRMED FROM CODE]:**
  1. `POST /api/razorpay/create-order` -> Generates official Razorpay Order ID (`order_...`) with amount in paise (`currency: INR`).
  2. Client-side Razorpay modal completes transaction.
  3. `POST /api/razorpay/verify-payment` -> Verifies HMAC SHA-256 signature using `razorpayKeySecret` and `Utils.verifyPaymentSignature()`.
- **Webhooks:** **No webhooks are implemented in the codebase.** Payment completion relies on client-side callback verification to `/api/razorpay/verify-payment`.
- **Production Guard [CONFIRMED FROM `RazorpayPaymentService.java:61`]:**
  - In `prod` profile, sandbox mock fallback is **100% prohibited**.
  - If `RAZORPAY_API_KEY` or `RAZORPAY_API_SECRET` contains `"xxxx"`, payments will immediately fail.
- **Required Credentials:**
  - `RAZORPAY_API_KEY`: Production Key ID (`rzp_live_...`)
  - `RAZORPAY_API_SECRET`: Production Secret Key
- **Frontend Key Injection:** Passed to user frontend build as `VITE_RAZORPAY_KEY`.
- **Compliance:** Razorpay requires fully functional HTTPS (`https://`) on the frontend to load its `checkout.js` modal script.

---

# SECTION P: GOOGLE reCAPTCHA REQUIREMENTS

- **Version:** Google reCAPTCHA v3 (Invisible risk-score based bot protection).
- **Protected Endpoints [CONFIRMED FROM CODE]:**
  1. User Registration (`UserRegisterRequestDTO.recaptchaToken` - `@NotBlank`)
  2. User Login (`UserLoginRequestDTO.recaptchaToken` - `@NotBlank`)
  3. Admin Login (`AdminLoginDTO.recaptchaToken` - `@NotBlank`)
- **Backend Verification Flow:**
  - Calls `https://www.google.com/recaptcha/api/siteverify` via reactive Netty `WebClient` (`recaptchaWebClient`).
  - Score threshold configured at `0.5` (`recaptcha.score-threshold=0.5`).
- **Production Configuration:**
  - `RECAPTCHA_ENABLED=true`
  - `RECAPTCHA_SITE_KEY`: Public Site Key (configured in Google Admin Console for domain `gathbandhan.com`).
  - `RECAPTCHA_SECRET_KEY`: Private Secret Key.
- **Frontend Integration:**
  - `VITE_RECAPTCHA_SITE_KEY` passed to `frontend/.env` and `admin panel/my-react-app/.env`.

---

# SECTION Q: STORAGE REQUIREMENTS

### 1. Uploaded File Types & Directories
- **Categories:**
  - Profile Avatars & Gallery: `/uploads/images/`
  - Voice Notes / Audio: `/uploads/audio/`
  - Identification Documents: `/uploads/documents/`
  - Video Introductions: `/uploads/videos/`
- **Max File Size:** `10 MB` per file (`spring.servlet.multipart.max-file-size=10MB`).
- **EXIF Sanitization [CONFIRMED IN `FileStorageServiceImpl.java:63`]:** Images are decoded and re-encoded via Java `BufferedImage` to strip camera EXIF data and GPS coordinates for user privacy.
- **Storage Path on VPS:** `/opt/gathbandhan/uploads/`
- **Volume Sizing Calculation:**
  - 10,000 active users with an average of 3 compressed photos (500 KB each) = 15 GB.
  - Chat media & documents = 10 GB.
  - Recommended minimum disk allocation for storage: **50 GB NVMe partition**.

---

# SECTION R: BACKUP REQUIREMENTS

| Target | Content | Method | Frequency | Retention |
| :--- | :--- | :--- | :--- | :--- |
| **PostgreSQL Database** | Users, matches, payments, chats, audit logs | Automated `pg_dump -Fc` script | Daily at 02:00 AM UTC | 14 Days Local + Cloud (S3/Backblaze) |
| **User Media (`/uploads`)** | Photos, voice notes, documents | `rsync -avz` or `rclone sync` | Daily incremental | Mirror + Weekly Snapshot |
| **Redis Dump** | Session blacklist & cache | Snapshot `dump.rdb` | Weekly | 7 Days |
| **Application Config** | `.env`, Nginx configs, systemd unit files | Encrypted archive (`gpg`) | On every deployment / config change | Versioned in secure vault |

### Production Automated PostgreSQL Backup Script (`/opt/scripts/backup-db.sh`):
```bash
#!/bin/bash
BACKUP_DIR="/var/backups/gathbandhan"
DATE=$(date +%Y%m%d_%H%M%S)
mkdir -p $BACKUP_DIR
docker exec gathbandhan_postgres pg_dump -U gathbandhan_app -d gathbandhan_prod -F c > $BACKUP_DIR/db_$DATE.dump
find $BACKUP_DIR -type f -name "*.dump" -mtime +14 -delete
```

---

# SECTION S: MONITORING REQUIREMENTS

1. **Spring Boot Actuator:**
   - Exposed endpoints: `/actuator/health`, `/actuator/info`, `/actuator/metrics`.
   - In `application-prod.properties`, `management.endpoint.health.show-details=never` (hides internal stack details from unauthenticated users).
2. **Prometheus Metrics:**
   - **AUDIT DEFECT:** `pom.xml` currently lacks the `io.micrometer:micrometer-registry-prometheus` dependency. Once added, Prometheus will scrape `/actuator/prometheus` for JVM heap, GC pauses, HikariCP pool saturation, and Tomcat thread states.
3. **Application Logs:**
   - Log directory: `/var/log/gathbandhan/app.log`
   - Log rotation: Linux `logrotate` with 7-day retention and gzip compression.
4. **Host & Container Monitoring:**
   - Uptime monitor (BetterUptime or UptimeRobot) pinging `https://gathbandhan.com/api/admin/operations/health`.
   - Node Exporter / Netdata for real-time CPU, RAM, Disk I/O, and Network monitoring.

---

# SECTION T: SECURITY REQUIREMENTS

1. **Firewall (UFW):**
   - Strictly ALLOW only: `22` (SSH), `80` (HTTP), `443` (HTTPS).
   - Strictly DENY from public: `9090` (Backend), `5432` (PostgreSQL), `6379` (Redis), `5672` & `15672` (RabbitMQ).
2. **Rate Limiting & Proxy IP Resolution [AUDIT ALERT]:**
   - The backend includes Redis-based rate limiting on logins, OTPs, and password resets (`RateLimitService`).
   - Behind Nginx, `ClientIpResolver.java` checks `security.rate-limit.trusted-proxies`. If `127.0.0.1` is not listed, all requests resolve to `127.0.0.1`, causing global lockout after 5 failed attempts. `security.rate-limit.trusted-proxies=127.0.0.1` **must** be set in production!
3. **Database Security:**
   - Never use default `postgres` superuser for the application.
   - Restrict application user permissions to `gathbandhan_prod`.
4. **JWT Security:**
   - Enforce 256-bit+ randomly generated secret key for `JWT_SECRET`.
   - Token expiration set to 24 hours (`jwt.expiration=86400000`).
   - Revoked tokens stored in Redis (`TokenRevocationService`).
5. **CORS Security:**
   - Isolate allowed origins strictly to `https://gathbandhan.com` and `https://admin.gathbandhan.com`.

---

# SECTION U: PRODUCTION PROBLEMS & BLOCKERS

The following **14 specific issues** in the existing codebase will cause failures or security vulnerabilities if deployed without remediation:

### 1. 🚨 Hardcoded Gmail Credentials Overriding Production Properties
- **Location:** [`EmailConfig.java:22-26`](file:///d:/demo/demo/src/main/java/com/example/config/EmailConfig.java#L22-L26)
- **Problem:** `EmailConfig.java` defines a `@Bean public JavaMailSender javaMailSender()` with hardcoded host `"smtp.gmail.com"`, port `587`, username `"vaibhavpawase143@gmail.com"`, and an active Google app password.
- **Impact:** This bean completely overrides Spring Boot's property-driven `JavaMailSender`. The backend will ignore `MAIL_HOST`, `MAIL_USERNAME`, and `MAIL_PASSWORD` set in `application-prod.properties` and attempt to send emails through this personal Gmail account.

### 2. 🚨 Hardcoded Redis Host in Java Configuration
- **Location:** [`RedisConfig.java:45`](file:///d:/demo/demo/src/main/java/com/example/config/RedisConfig.java#L45)
- **Problem:** `RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration("localhost", 6379);` is hardcoded in Java.
- **Impact:** It completely ignores `spring.data.redis.host`, `spring.data.redis.port`, and `spring.data.redis.password` defined in properties. If Redis is deployed in Docker with a password, the connection will fail.

### 3. 🚨 Hardcoded `localhost:9090` in User Photo Service
- **Location:** [`UserPhotoServiceImpl.java:35, 74`](file:///d:/demo/demo/src/main/java/com/example/serviceimpl/UserPhotoServiceImpl.java#L35)
- **Problem:** `private static final String BASE_URL = "http://localhost:9090/uploads/";` is prepended to all uploaded user photos before saving to the database.
- **Impact:** In production, photo URLs stored in PostgreSQL will be `http://localhost:9090/uploads/<uuid>.jpg`. When end-users view profiles in their browsers, the images will fail to load.

### 4. 🚨 Hardcoded `localhost:3000` Email Verification Redirect
- **Location:** [`AuthController.java:88`](file:///d:/demo/demo/src/main/java/com/example/controller/auth/AuthController.java#L88)
- **Problem:** `response.sendRedirect("http://localhost:3000/email-verified");`
- **Impact:** When a user clicks the verification link in their email, they are redirected to `localhost:3000` instead of the production frontend (`https://gathbandhan.com/email-verified`).

### 5. 🚨 Hardcoded `System.getProperty("user.dir")` for Upload Directories
- **Location:** [`FileStorageServiceImpl.java:21`](file:///d:/demo/demo/src/main/java/com/example/serviceimpl/FileStorageServiceImpl.java#L21), [`ImageUploadController.java:31`](file:///d:/demo/demo/src/main/java/com/example/controller/user/ImageUploadController.java#L31), [`WebConfig.java:13`](file:///d:/demo/demo/src/main/java/com/example/config/WebConfig.java#L13)
- **Problem:** All three classes construct upload paths using `System.getProperty("user.dir") + File.separator + "uploads"`.
- **Impact:** The environment property `FILE_UPLOAD_DIR` (`/opt/gathbandhan/uploads`) is completely ignored. If Spring Boot is run from another working directory or inside a container, files will be saved in unexpected locations.

### 6. 🚨 Missing Trusted Proxy Configuration Causing Global Rate Limit Lockout
- **Location:** [`ClientIpResolver.java:32-55`](file:///d:/demo/demo/src/main/java/com/example/security/ratelimit/ClientIpResolver.java#L32-L55), `application-prod.properties`
- **Problem:** `security.rate-limit.trusted-proxies` is empty in `application.properties` and missing in `application-prod.properties`.
- **Impact:** Behind Nginx, `request.getRemoteAddr()` will always return `127.0.0.1`. The rate limiter will treat all internet visitors as the same IP (`127.0.0.1`). After 5 failed logins by anyone, all users worldwide will be locked out!

### 7. 🚨 CORS Configuration Property Mismatch
- **Location:** [`CorsConfig.java:18`](file:///d:/demo/demo/src/main/java/com/example/config/CorsConfig.java#L18) vs [`application-prod.properties:155`](file:///d:/demo/demo/src/main/resources/application-prod.properties#L155)
- **Problem:** `CorsConfig.java` injects `@Value("${app.cors.allowed-origins:}")`, but `application-prod.properties` configures `spring.web.cors.allowed-origins=${FRONTEND_URL}`.
- **Impact:** `app.cors.allowed-origins` evaluates to empty, so `CorsConfig` falls back to allowing only `localhost:3000` and `localhost:5173`. Cross-origin API calls from `https://gathbandhan.com` or `https://admin.gathbandhan.com` will be rejected by Spring Security.

### 8. 🚨 Hardcoded `@CrossOrigin(origins = "http://localhost:3000")` on Specific Controllers
- **Location:** `UserController.java:24`, `ProfileSearchController.java:14`, `ProfileController.java:22`, `SupportController.java:16`, `AdminUserManagementController.java:22`, `AdminAuditLogController.java:17`
- **Problem:** Controller-level `@CrossOrigin` annotations hardcode `http://localhost:3000`.
- **Impact:** Can interfere with centralized CORS settings in production.

### 9. 🚨 Seeded Super Admin with Fixed Default Credentials
- **Location:** [`V67__seed_super_admin.sql:21-42`](file:///d:/demo/demo/src/main/resources/db/migration/V67__seed_super_admin.sql#L21-L42)
- **Problem:** Migration seeds a default superadmin account (`vaibhavpawase143@gmail.com`) with a fixed bcrypt hash.
- **Impact:** If deployed without immediately changing this account, anyone with codebase access can log into the production Admin Portal.

### 10. ⚠️ Missing `micrometer-registry-prometheus` Dependency
- **Location:** Root `pom.xml`, `application-prod.properties:227`
- **Problem:** `management.metrics.export.prometheus.enabled=true` is set, but `io.micrometer:micrometer-registry-prometheus` is not in `pom.xml`.
- **Impact:** The `/actuator/prometheus` endpoint will fail to load or return 404.

### 11. ⚠️ Frontend Image Utility Fallback to `localhost:9090`
- **Location:** [`admin panel/my-react-app/src/utils/imageUtils.js:16`](file:///d:/demo/demo/admin%20panel/my-react-app/src/utils/imageUtils.js#L16)
- **Problem:** If `VITE_BACKEND_URL` is omitted during `npm run build`, `getBackendBaseUrl()` falls back to `"http://localhost:9090"`.
- **Impact:** Broken avatars and profile photos in production.

### 12. ⚠️ Hardcoded Database Credentials in Maven Flyway Plugin
- **Location:** [`pom.xml:178-180`](file:///d:/demo/demo/pom.xml#L178-L180)
- **Problem:** `<url>jdbc:postgresql://localhost:5432/gathbandhan_perf</url>`, `<user>postgres</user>`, `<password>root</password>`.
- **Impact:** Running `mvn flyway:migrate` on VPS will attempt to connect to localhost with dev credentials.

### 13. ⚠️ Incomplete Docker Configuration
- **Location:** Project root
- **Problem:** Backend and frontends have no Dockerfiles.
- **Impact:** Cannot run `docker-compose up` directly for the entire application.

### 14. ⚠️ Stray Backend Files Inside Frontend Directory
- **Location:** `frontend/pom.xml`, `frontend/mvnw`, `frontend/.mvn`, `frontend/target`
- **Problem:** Accidental copies of the Java backend build files exist inside `frontend/`.
- **Impact:** Causes build confusion and clutters the repository.

---

# SECTION V: PRE-DEPLOYMENT CHECKLIST

Before deploying any artifact to the production server:

- [ ] **1. Remediate `EmailConfig.java`:** Remove hardcoded credentials; inject `@Value("${spring.mail.host}")`, username, and password from Spring properties.
- [ ] **2. Remediate `RedisConfig.java`:** Replace hardcoded `"localhost", 6379` with `@Value("${spring.data.redis.host}")` and password support.
- [ ] **3. Remediate `UserPhotoServiceImpl.java`:** Inject `@Value("${app.base-url}")` instead of hardcoded `http://localhost:9090/uploads/`.
- [ ] **4. Remediate `AuthController.java`:** Replace hardcoded `http://localhost:3000/email-verified` with `${app.frontend-url}/email-verified`.
- [ ] **5. Configure Upload Directory:** Align `FileStorageServiceImpl`, `ImageUploadController`, and `WebConfig` to read `file.upload-dir` instead of `user.dir`.
- [ ] **6. Set Trusted Proxy for Rate Limiting:** Add `security.rate-limit.trusted-proxies=127.0.0.1` in `application-prod.properties`.
- [ ] **7. Align CORS Property:** Ensure `app.cors.allowed-origins` matches `FRONTEND_URL` and admin domain in `application-prod.properties`.
- [ ] **8. Add Prometheus Dependency:** Add `io.micrometer:micrometer-registry-prometheus` to `pom.xml`.
- [ ] **9. Obtain Production API Credentials:**
  - [ ] Razorpay Live Key & Secret
  - [ ] Google reCAPTCHA v3 Live Site & Secret Keys
  - [ ] SendGrid Production API Key for bulk email
  - [ ] Verified Domain SMTP credentials
  - [ ] Prokerala Kundli API live credentials
- [ ] **10. Generate Production JWT Secret:** Generate a cryptographically secure 512-bit secret:
  ```bash
  openssl rand -base64 64
  ```
- [ ] **11. Verify Clean Build:**
  - [ ] Backend: `mvn clean package -DskipTests` -> produces `demo-0.0.1-SNAPSHOT.jar`
  - [ ] User Frontend: `npm run build` -> produces `frontend/dist/`
  - [ ] Admin Frontend: `npm run build` -> produces `admin panel/my-react-app/dist/`

---

# SECTION W: PRODUCTION DEPLOYMENT CHECKLIST

Execute these steps on the target VPS:

- [ ] **1. Server Hardening & UFW:**
  ```bash
  sudo apt update && sudo apt upgrade -y
  sudo ufw default deny incoming
  sudo ufw default allow outgoing
  sudo ufw allow 22/tcp
  sudo ufw allow 80/tcp
  sudo ufw allow 443/tcp
  sudo ufw enable
  ```
- [ ] **2. Configure Swap Space:**
  ```bash
  sudo fallocate -l 8G /swapfile
  sudo chmod 600 /swapfile
  sudo mkswap /swapfile
  sudo swapon /swapfile
  echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab
  ```
- [ ] **3. Launch Middleware (Docker):**
  - Launch PostgreSQL, Redis, and RabbitMQ via Docker Compose.
  - Verify all 3 containers are healthy: `docker ps`.
- [ ] **4. Create Application User & Directories:**
  ```bash
  sudo useradd -m -s /bin/bash gathbandhan
  sudo mkdir -p /opt/gathbandhan/uploads/{images,audio,documents,videos}
  sudo mkdir -p /var/log/gathbandhan
  sudo mkdir -p /var/www/gathbandhan/frontend/dist
  sudo mkdir -p /var/www/gathbandhan/admin/dist
  sudo chown -R gathbandhan:gathbandhan /opt/gathbandhan /var/log/gathbandhan /var/www/gathbandhan
  ```
- [ ] **5. Deploy Spring Boot Systemd Unit (`/etc/systemd/system/gathbandhan.service`):**
  ```ini
  [Unit]
  Description=Gathbandhan Matrimony Spring Boot Application
  After=network.target docker.service

  [Service]
  User=gathbandhan
  WorkingDirectory=/opt/gathbandhan
  EnvironmentFile=/opt/gathbandhan/.env.prod
  ExecStart=/usr/bin/java -Xms2048m -Xmx3500m -XX:+UseG1GC -Dspring.profiles.active=prod -jar demo-0.0.1-SNAPSHOT.jar
  Restart=always
  RestartSec=10
  StandardOutput=journal
  StandardError=journal
  SyslogIdentifier=gathbandhan-backend

  [Install]
  WantedBy=multi-user.target
  ```
- [ ] **6. Deploy Static Frontends:** Copy compiled `dist/` folders to `/var/www/gathbandhan/frontend/dist` and `/var/www/gathbandhan/admin/dist`.
- [ ] **7. Configure & Reload Nginx:** Copy the production Nginx configuration to `/etc/nginx/sites-available/gathbandhan`, enable it, test with `nginx -t`, and reload.
- [ ] **8. Provision SSL via Certbot:**
  ```bash
  sudo certbot --nginx -d gathbandhan.com -d www.gathbandhan.com -d admin.gathbandhan.com
  ```
- [ ] **9. Start Backend Service:**
  ```bash
  sudo systemctl daemon-reload
  sudo systemctl enable gathbandhan
  sudo systemctl start gathbandhan
  sudo systemctl status gathbandhan
  ```

---

# SECTION X: POST-DEPLOYMENT TESTING CHECKLIST

Execute these smoke and integration tests immediately following deployment:

- [ ] **1. DNS & SSL Validation:**
  - Verify HTTPS certificate validity in browser on `https://gathbandhan.com` and `https://admin.gathbandhan.com`.
  - Verify HTTP -> HTTPS 301 redirection.
- [ ] **2. Backend Health & Actuator:**
  - Verify endpoint: `curl -I https://gathbandhan.com/api/admin/operations/health` (Should return 200 OK).
- [ ] **3. Database Migrations Verification:**
  - Verify table count: `docker exec -it gathbandhan_postgres psql -U gathbandhan_app -d gathbandhan_prod -c "SELECT COUNT(*) FROM flyway_schema_history;"` (Should equal 139 migrations).
- [ ] **4. Super Admin Security:**
  - Immediately log into `https://admin.gathbandhan.com`.
  - Change the default superadmin email and password from migration `V67`.
- [ ] **5. User Registration & Email Delivery:**
  - Register a new user on `https://gathbandhan.com/register`.
  - Confirm Google reCAPTCHA v3 validates successfully.
  - Verify verification email lands in user inbox with correct production URL (`https://gathbandhan.com/api/auth/verify?token=...`).
  - Click verification link; confirm redirection to `https://gathbandhan.com/email-verified`.
- [ ] **6. WebSocket & Real-Time Chat:**
  - Open browser DevTools Network tab -> Filter by `WS`.
  - Verify STOMP connection to `wss://gathbandhan.com/ws` completes with `CONNECTED` frame.
  - Test real-time message sending and notification reception.
- [ ] **7. Photo Upload & EXIF Stripping:**
  - Upload a profile photo.
  - Confirm file is written to `/opt/gathbandhan/uploads/`.
  - Verify image URL in response begins with `https://gathbandhan.com/uploads/...` (NOT `http://localhost:9090`).
  - Verify image renders properly on profile view.
- [ ] **8. Payment Gateway Test:**
  - Navigate to Subscription Plans.
  - Click Upgrade -> verify Razorpay live checkout modal opens in INR.
- [ ] **9. Rate Limiting Verification:**
  - Attempt 6 consecutive failed logins from an external device.
  - Verify the 6th attempt returns `429 Too Many Requests`.
  - Confirm other devices/users from different IPs are NOT blocked.
- [ ] **10. Automated Backups Verification:**
  - Manually trigger `/opt/scripts/backup-db.sh` and verify `.dump` file creation in `/var/backups/gathbandhan/`.
