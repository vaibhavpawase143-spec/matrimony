# PRODUCTION AUDIT VERIFICATION & TESTING GUIDE

## QUICK START - VERIFY FIXES

### 1. Verify Code Splitting ✅
```bash
# Frontend build size check
cd frontend
npm run build

# Look for:
# - Initial bundle: < 100KB (was 300KB+)
# - Separate chunks for each lazy route
# - dist/ size report

# Expected output:
# ✓ app.js: 45KB (code splitting!)
# ✓ pages/Home.js: 25KB (lazy loaded)
# ✓ pages/Search.js: 30KB (lazy loaded)
# Total: ~300KB split across chunks (good!)
```

### 2. Verify Response Compression ✅
```bash
# Test compression in production
curl -i http://localhost:9090/api/profiles \
  -H "Accept-Encoding: gzip" \
  -H "Authorization: Bearer <token>"

# Expected headers:
# Content-Encoding: gzip
# Content-Length: ~50KB (was 500KB+)
# X-Original-Size: ~500KB (shown by some servers)

# Compression working if:
# - Content-Encoding header present
# - Content-Length < 100KB
# - 90% bandwidth reduction
```

### 3. Verify Payment EAGER→LAZY ✅
```bash
# Database query monitoring
# In application logs, search for Payment entity load queries

# Before fix: Multiple SELECT queries for Payment + User
# After fix: Single SELECT query, User loaded separately only when needed

# Test: Load payments list API
curl http://localhost:9090/api/payments \
  -H "Authorization: Bearer <token>"

# Monitor database queries (enable query logging):
# spring.jpa.show-sql=true
# logging.level.org.hibernate.SQL=DEBUG

# Expected: Fewer queries in logs = Fix working!
```

### 4. Verify Conversation Repository Fix ✅
```bash
# Load conversation list
curl http://localhost:9090/api/conversations \
  -H "Authorization: Bearer <token>"

# Monitor execution time:
# Before: 3000-5000ms (3 subqueries per conversation × 50 convos)
# After: 100-200ms (single query with GROUP BY)

# Check logs for query count:
# Expected: 1 query, not 150+ queries
```

### 5. Verify AdminRoute Role Check ✅
```bash
# Test 1: Login as regular user
curl -X POST http://localhost:9090/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@test.com",
    "password": "password123"
  }'

# Get token from response
TOKEN="<received_token>"

# Test 2: Try to access admin endpoint as user (SHOULD FAIL)
curl http://localhost:9090/api/admin/users \
  -H "Authorization: Bearer $TOKEN"

# Expected Response: 403 Forbidden or redirect to /home
# Verify in logs: "[SECURITY] Unauthorized admin access attempt"

# Test 3: Login as admin
curl -X POST http://localhost:9090/api/auth/admin/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@test.com",
    "password": "admin123"
  }'

# Get admin token
ADMIN_TOKEN="<received_admin_token>"

# Test 4: Access admin endpoint as admin (SHOULD SUCCEED)
curl http://localhost:9090/api/admin/users \
  -H "Authorization: Bearer $ADMIN_TOKEN"

# Expected: 200 OK with user data
```

### 6. Verify JWT Token Caching (When Implemented) ⏳
```bash
# Enable Redis monitoring
redis-cli MONITOR

# Login (generates token and caches in Redis)
curl -X POST http://localhost:9090/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "user@test.com", "password": "pass"}'

# In Redis monitor, should see:
# SET jwt:token:user@test.com <claims> EX 86400

# Make multiple requests with same token
for i in {1..100}; do
  curl http://localhost:9090/api/profile \
    -H "Authorization: Bearer $TOKEN" \
    -s > /dev/null
done

# Monitor: Database queries should NOT increase significantly
# Before: 100-300 database queries
# After: <10 database queries (token cache HIT)
```

### 7. Verify Master Data Caching (When Implemented) ⏳
```bash
# Check application startup logs
grep "Master data cache" logs/app.log

# Expected output:
# "Warming up master data cache..."
# "Cached 150+ religions"
# "Cached 200+ castes"
# "Master data cache warmed successfully"

# Test dropout load time
curl http://localhost:9090/api/master/religions \
  -H "Authorization: Bearer $TOKEN" \
  -w "@curl-format.txt"

# Expected response time: < 50ms (from cache)
# Before: 100-500ms (from database)
```

---

## PERFORMANCE TESTING

### Load Test Setup
```bash
# Install Apache Bench or similar
apt-get install apache2-utils

# OR use wrk (better for concurrent testing)
# https://github.com/wg/wrk
```

### Test 1: Homepage Load (Code Splitting)
```bash
# Before fixes
ab -n 100 -c 10 http://localhost:3000/

# Expected before: ~5-10s per request
# Expected after: ~1-2s per request

# Check bundle size
du -sh frontend/dist/

# Before: ~300KB
# After: ~50KB + lazy chunks on demand
```

### Test 2: Conversation List API (N+1 Fix)
```bash
ab -n 100 -c 50 \
  -H "Authorization: Bearer $TOKEN" \
  http://localhost:9090/api/conversations

# Monitor database CPU during test:
# Before: CPU spike to 80-90%
# After: CPU stays < 30%
```

### Test 3: Master Data API (Caching)
```bash
# First call (cache miss, if not warmed)
time curl http://localhost:9090/api/master/religions \
  -H "Authorization: Bearer $TOKEN"

# Subsequent calls (cache hit)
time curl http://localhost:9090/api/master/religions \
  -H "Authorization: Bearer $TOKEN"

# Expected:
# First: 100-500ms
# Subsequent: 5-20ms
```

### Test 4: Concurrent User Simulation
```bash
# Simulate 1000 concurrent users
wrk -t12 -c1000 -d30s \
  -H "Authorization: Bearer $TOKEN" \
  http://localhost:9090/api/profiles

# Before fixes:
# - Requests/sec: 100-200
# - Latency avg: 5-10s
# - Errors: High

# After fixes:
# - Requests/sec: 1000+
# - Latency avg: 100-200ms
# - Errors: <1%
```

### Test 5: Response Size Verification
```bash
# Check uncompressed size
curl -i http://localhost:9090/api/profiles \
  -H "Authorization: Bearer $TOKEN" | grep Content-Length

# Check compressed size
curl -i http://localhost:9090/api/profiles \
  -H "Accept-Encoding: gzip" \
  -H "Authorization: Bearer $TOKEN"

# Compression ratio check
# Original size / Compressed size

# Before: ~500KB uncompressed
# After: ~50KB compressed (90% reduction)
```

---

## DATABASE VERIFICATION

### Connection Pool Health
```sql
-- Check active connections
SELECT datname, count(*) 
FROM pg_stat_activity 
GROUP BY datname;

-- Before fixes: 80-95/100 connections active at peak
-- After fixes: 15-30/100 connections active

-- Check connection pool usage
SELECT 
  count(*) as total_connections,
  state,
  query
FROM pg_stat_activity
WHERE datname = 'gathbandhan'
GROUP BY state;
```

### Query Performance Analysis
```sql
-- Enable query logging
ALTER SYSTEM SET log_min_duration_statement = 500; -- Log queries > 500ms
SELECT pg_reload_conf();

-- Check slow query log
SELECT 
  query,
  mean_exec_time,
  calls
FROM pg_stat_statements
ORDER BY mean_exec_time DESC
LIMIT 10;

-- Expected before fixes:
-- SELECT FROM payments WHERE user_id = X → 50-100ms (N+1)
-- SELECT FROM conversations (with subqueries) → 3000-5000ms
-- SELECT FROM profiles (with full EntityGraph) → 150-200ms

-- Expected after fixes:
-- All queries < 100ms
-- Most < 50ms
```

### Index Verification
```sql
-- Check if indexes are being used
SELECT schemaname, tablename, indexname 
FROM pg_indexes 
WHERE tablename IN ('payments', 'profiles', 'conversations');

-- Check index sizes
SELECT 
  indexname, 
  pg_size_pretty(pg_relation_size(indexrelid)) as size
FROM pg_indexes 
WHERE tablename = 'profiles';

-- Verify composite indexes exist (after Phase 2)
-- Expected: idx_profile_search, idx_conversation_messages, etc.
```

---

## SECURITY VERIFICATION

### Configuration Secrets Check
```bash
# Verify no secrets in git history
git log --all -p --source -- "application.properties" | \
  grep -i "secret\|password\|key" | head -20

# Should output: Nothing (clean)

# Verify .gitignore excludes sensitive files
cat .gitignore | grep -E "application-.*\.properties|\.env"

# Should include these lines:
# application-prod.properties
# application-local.properties
# .env
# *.key
# *.pem
```

### JWT Validation
```bash
# Test JWT expiration
TOKEN="<expired_jwt_token>"

curl http://localhost:9090/api/profile \
  -H "Authorization: Bearer $TOKEN"

# Expected: 401 Unauthorized
# Message: "Invalid or expired token"
```

### Admin Access Control
```bash
# Test unauthorized access
USER_TOKEN="<regular_user_token>"

curl -i http://localhost:9090/api/admin/users \
  -H "Authorization: Bearer $USER_TOKEN"

# Expected: 403 Forbidden or 401 Unauthorized

# Check audit log
tail -f logs/security.log | grep "Unauthorized"

# Should see: "[SECURITY] Unauthorized admin access attempt by user X"
```

---

## MONITORING DASHBOARD

### Key Metrics to Track

#### Performance
- [ ] API Response Time (avg, p95, p99)
- [ ] Database Query Time
- [ ] Connection Pool Utilization
- [ ] Memory Usage
- [ ] CPU Usage
- [ ] Bandwidth Usage

#### Reliability
- [ ] Error Rate (4xx, 5xx)
- [ ] Uptime %
- [ ] Crash Count
- [ ] Log Error Rate

#### Security
- [ ] Unauthorized Access Attempts
- [ ] Rate Limit Violations
- [ ] SQL Injection Attempts
- [ ] Token Validation Failures

#### Business
- [ ] Active Users
- [ ] Requests/Second
- [ ] API Throughput
- [ ] Cache Hit Rate

### Prometheus Metrics
```yaml
# Add to application.properties
management.endpoints.web.exposure.include=health,metrics,prometheus
management.metrics.enable.jvm.memory=true
management.metrics.enable.jvm.gc=true
management.metrics.enable.jvm.threads=true
```

### Sample Queries
```
# Database Connection Pool Usage
http_connections_active / http_connections_max

# API Response Time (95th percentile)
histogram_quantile(0.95, http_request_duration_seconds)

# Error Rate
rate(http_requests_total{status=~"5.."}[5m])

# Memory Usage
jvm_memory_used_bytes / jvm_memory_max_bytes
```

---

## DEPLOYMENT CHECKLIST

### Pre-Deployment
- [ ] All tests pass locally
- [ ] Code review completed
- [ ] Database backups taken
- [ ] Monitoring configured
- [ ] Rollback plan documented
- [ ] Team notified of deployment time

### Deployment
- [ ] Deploy to staging
- [ ] Run smoke tests
- [ ] Verify metrics improving
- [ ] Get approval from tech lead
- [ ] Deploy to production (Blue/Green if possible)
- [ ] Monitor closely for 30 minutes
- [ ] Enable monitoring dashboards

### Post-Deployment
- [ ] Verify all metrics normal
- [ ] Check error logs
- [ ] Validate user experience
- [ ] Collect performance data
- [ ] Document improvements
- [ ] Plan next phase

---

## ROLLBACK PROCEDURE

If issues detected:

```bash
# 1. Identify issue
# Check logs and metrics

# 2. Immediate rollback
git revert <commit_hash>
./mvnw clean package
docker build -t gathbandhan:rollback .
docker push gathbandhan:rollback

# 3. Redeploy previous version
kubectl set image deployment/gathbandhan \
  gathbandhan=gathbandhan:rollback --record

# 4. Monitor recovery
kubectl rollout status deployment/gathbandhan

# 5. Post-mortem
# - What went wrong?
# - How to prevent?
# - Fix and redeploy
```

---

## SUCCESS CRITERIA - SIGN OFF

Before considering audit complete, verify:

### Performance
- ✅ Homepage < 2s load time
- ✅ API responses < 300ms (99th %)
- ✅ Search results < 500ms
- ✅ Concurrent users: 10K+ stable

### Scalability
- ✅ Support 1M+ users architecture-wise
- ✅ Database CPU < 50% at peak
- ✅ Memory stable and predictable
- ✅ Horizontal scaling possible

### Security
- ✅ No secrets in version control
- ✅ All APIs properly authenticated/authorized
- ✅ Audit trail for admin operations
- ✅ Security scan: 0 critical/high issues

### Quality
- ✅ Production logging configured
- ✅ Error handling comprehensive
- ✅ Monitoring dashboards live
- ✅ Alerting rules configured

---

**Testing Complete When**: All checks pass ✅
**Deployment Ready When**: All success criteria met ✅
**Production Ready When**: Stable for 7 days in production ✅
