CREATE TABLE IF NOT EXISTS request_audit_logs (
    id BIGSERIAL PRIMARY KEY,
    actor_id BIGINT,
    actor_name VARCHAR(255),
    actor_type VARCHAR(32) NOT NULL,
    http_method VARCHAR(10) NOT NULL,
    request_path VARCHAR(1000) NOT NULL,
    query_string VARCHAR(2000),
    status_code INTEGER NOT NULL,
    outcome VARCHAR(16) NOT NULL,
    failure_type VARCHAR(255),
    ip_address VARCHAR(45),
    user_agent VARCHAR(1000),
    duration_ms BIGINT NOT NULL,
    occurred_at TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_request_audit_occurred_at ON request_audit_logs (occurred_at);
CREATE INDEX IF NOT EXISTS idx_request_audit_actor ON request_audit_logs (actor_type, actor_id);
CREATE INDEX IF NOT EXISTS idx_request_audit_path ON request_audit_logs (request_path);
CREATE INDEX IF NOT EXISTS idx_request_audit_status ON request_audit_logs (status_code);
