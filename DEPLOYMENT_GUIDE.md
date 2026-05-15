# 🚀 Matrimony Website Deployment Guide

## 📋 Prerequisites

### Backend Requirements
- **Java 17** or higher
- **Maven 3.6+**
- **PostgreSQL 13+**
- **Redis** (for caching and sessions)
- **Node.js 16+** (for frontend build)

### Environment Setup
- **Git** for version control
- **Docker** (optional, for containerization)
- **Cloud server** (AWS, DigitalOcean, etc.)

---

## 🔧 Backend Configuration

### 1. Database Setup

```sql
-- Create PostgreSQL database
CREATE DATABASE gathbandhan;
CREATE USER gathbandhan_user WITH PASSWORD 'your_secure_password';
GRANT ALL PRIVILEGES ON DATABASE gathbandhan TO gathbandhan_user;
```

### 2. Environment Variables

Create `application-prod.properties`:

```properties
# Production Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/gathbandhan
spring.datasource.username=gathbandhan_user
spring.datasource.password=your_secure_password

# Production Security
jwt.secret=your_super_secure_jwt_secret_key_at_least_256_bits_long
jwt.expiration=86400000

# Production Email (SendGrid)
spring.mail.host=smtp.sendgrid.net
spring.mail.port=587
spring.mail.username=apikey
spring.mail.password=YOUR_SENDGRID_API_KEY

# Production Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.password=your_redis_password

# Production File Upload
file.upload-dir=/var/www/gathbandhan/uploads/

# Production Razorpay
razorpay.api.key=your_production_razorpay_key
razorpay.api.secret=your_production_razorpay_secret

# Production Base URL
app.base-url=https://yourdomain.com
```

### 3. Build and Deploy Backend

```bash
# Clone repository
git clone <your-repo-url>
cd matrimony-website

# Build the application
mvn clean package -DskipTests

# Run the application
java -jar target/demo-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

---

## 🎨 Frontend Configuration

### 1. Install Dependencies

```bash
cd frontend
npm install
```

### 2. Environment Configuration

Create `.env.production`:

```env
REACT_APP_API_URL=https://yourdomain.com/api
REACT_APP_BASE_URL=https://yourdomain.com
```

### 3. Build and Deploy

```bash
# Build for production
npm run build

# The build will be in the 'dist' folder
```

---

## 🌐 Nginx Configuration

Create `/etc/nginx/sites-available/gathbandhan`:

```nginx
server {
    listen 80;
    server_name yourdomain.com www.yourdomain.com;
    
    # Redirect to HTTPS
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name yourdomain.com www.yourdomain.com;

    # SSL Configuration
    ssl_certificate /path/to/your/certificate.crt;
    ssl_certificate_key /path/to/your/private.key;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;

    # Frontend
    location / {
        root /var/www/gathbandhan/frontend/dist;
        try_files $uri $uri/ /index.html;
    }

    # Backend API
    location /api/ {
        proxy_pass http://localhost:9090;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # File uploads
    location /uploads/ {
        alias /var/www/gathbandhan/uploads/;
        expires 1y;
        add_header Cache-Control "public, immutable";
    }

    # Security headers
    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-XSS-Protection "1; mode=block" always;
    add_header X-Content-Type-Options "nosniff" always;
    add_header Referrer-Policy "no-referrer-when-downgrade" always;
    add_header Content-Security-Policy "default-src 'self' http: https: data: blob: 'unsafe-inline'" always;
}
```

Enable the site:

```bash
sudo ln -s /etc/nginx/sites-available/gathbandhan /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

---

## 🔐 SSL Certificate Setup

### Using Let's Encrypt

```bash
# Install Certbot
sudo apt install certbot python3-certbot-nginx

# Obtain SSL certificate
sudo certbot --nginx -d yourdomain.com -d www.yourdomain.com

# Auto-renewal (already configured)
sudo systemctl status certbot.timer
```

---

## 🐳 Docker Deployment (Optional)

### Dockerfile for Backend

```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9090

CMD ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
```

### Dockerfile for Frontend

```dockerfile
FROM node:16-alpine as build

WORKDIR /app
COPY package*.json ./
RUN npm ci --only=production

COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

### Docker Compose

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:13
    environment:
      POSTGRES_DB: gathbandhan
      POSTGRES_USER: gathbandhan_user
      POSTGRES_PASSWORD: your_secure_password
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"

  redis:
    image: redis:alpine
    ports:
      - "6379:6379"

  backend:
    build: .
    ports:
      - "9090:9090"
    depends_on:
      - postgres
      - redis
    environment:
      - SPRING_PROFILES_ACTIVE=prod

  frontend:
    build: ./frontend
    ports:
      - "80:80"
    depends_on:
      - backend

volumes:
  postgres_data:
```

---

## 🚀 Deployment Steps

### 1. Server Setup

```bash
# Update system
sudo apt update && sudo apt upgrade -y

# Install required packages
sudo apt install -y openjdk-17-jdk maven postgresql redis-server nginx certbot python3-certbot-nginx

# Create application user
sudo useradd -m -s /bin/bash gathbandhan
sudo usermod -aG sudo gathbandhan
```

### 2. Application Deployment

```bash
# Switch to application user
sudo su - gathbandhan

# Clone and build application
git clone <your-repo-url>
cd matrimony-website

# Build backend
mvn clean package -DskipTests

# Build frontend
cd frontend
npm install
npm run build
cd ..

# Create necessary directories
mkdir -p /var/www/gathbandhan/uploads
mkdir -p /var/www/gathbandhan/logs
```

### 3. Systemd Service

Create `/etc/systemd/system/gathbandhan.service`:

```ini
[Unit]
Description=Gathbandhan Matrimony Application
After=network.target

[Service]
Type=simple
User=gathbandhan
WorkingDirectory=/home/gathbandhan/matrimony-website
ExecStart=/usr/bin/java -jar target/demo-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
Restart=always
RestartSec=10
StandardOutput=syslog
StandardError=syslog
SyslogIdentifier=gathbandhan

[Install]
WantedBy=multi-user.target
```

Enable and start the service:

```bash
sudo systemctl daemon-reload
sudo systemctl enable gathbandhan
sudo systemctl start gathbandhan
sudo systemctl status gathbandhan
```

---

## 🔍 Monitoring and Logs

### Application Logs

```bash
# View application logs
sudo journalctl -u gathbandhan -f

# View Nginx logs
sudo tail -f /var/log/nginx/access.log
sudo tail -f /var/log/nginx/error.log
```

### Health Checks

```bash
# Backend health check
curl https://yourdomain.com/api/admin/operations/health

# Database connectivity
curl https://yourdomain.com/api/health
```

---

## 📊 Performance Optimization

### 1. Database Optimization

```sql
-- Create indexes for better performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_phone ON users(phone);
CREATE INDEX idx_users_created_at ON users(created_at);
CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_payments_created_at ON payments(created_at);
```

### 2. Redis Configuration

```bash
# Edit Redis configuration
sudo nano /etc/redis/redis.conf

# Set max memory and eviction policy
maxmemory 256mb
maxmemory-policy allkeys-lru
```

### 3. Application Performance

```properties
# In application-prod.properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=1200000
```

---

## 🔒 Security Considerations

### 1. Firewall Setup

```bash
# Configure UFW
sudo ufw allow ssh
sudo ufw allow 80
sudo ufw allow 443
sudo ufw enable
```

### 2. Database Security

```sql
-- Remove default PostgreSQL user
DROP USER IF EXISTS postgres;

-- Limit database connections
ALTER USER gathbandhan_user CONNECTION LIMIT 50;
```

### 3. Application Security

- Regularly update dependencies
- Use strong passwords
- Enable 2FA for admin accounts
- Regular security audits
- Monitor suspicious activities

---

## 🚨 Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Check PostgreSQL service status
   - Verify database credentials
   - Check network connectivity

2. **Application Not Starting**
   - Check Java version compatibility
   - Verify port availability
   - Check logs for specific errors

3. **Frontend Not Loading**
   - Check Nginx configuration
   - Verify file permissions
   - Check SSL certificate validity

### Log Locations

- Application logs: `/var/log/gathbandhan/`
- Nginx logs: `/var/log/nginx/`
- System logs: `/var/log/syslog`

---

## 📈 Scaling Considerations

### Horizontal Scaling

- Load balancer setup
- Multiple application instances
- Database replication
- Redis clustering

### Vertical Scaling

- Increase server resources
- Optimize database queries
- Implement caching strategies
- Use CDN for static assets

---

## 🔄 Backup Strategy

### Database Backup

```bash
# Create backup script
#!/bin/bash
BACKUP_DIR="/var/backups/gathbandhan"
DATE=$(date +%Y%m%d_%H%M%S)
pg_dump -h localhost -U gathbandhan_user gathbandhan > $BACKUP_DIR/gathbandhan_$DATE.sql

# Keep only last 7 days
find $BACKUP_DIR -name "*.sql" -mtime +7 -delete
```

### File Backup

```bash
# Backup uploaded files
rsync -av /var/www/gathbandhan/uploads/ /var/backups/gathbandhan/uploads/
```

---

## 🎯 Production Checklist

- [ ] Database configured and secured
- [ ] Environment variables set
- [ ] SSL certificate installed
- [ ] Nginx configured
- [ ] Firewall enabled
- [ ] Monitoring setup
- [ ] Backup strategy implemented
- [ ] Performance optimization done
- [ ] Security measures in place
- [ ] Health checks configured
- [ ] Logging setup
- [ ] Error handling tested

---

## 📞 Support

For deployment issues:
1. Check application logs
2. Verify configuration files
3. Test database connectivity
4. Check network connectivity
5. Review security settings

---

**🎉 Your Matrimony Website is now ready for production!**
