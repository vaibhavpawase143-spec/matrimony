-- Migration script for Notification Outbox and Broadcast Jobs tables
CREATE TABLE IF NOT EXISTS notification_job_outbox (
    id BIGSERIAL PRIMARY KEY,
    idempotency_key VARCHAR(120) NOT NULL UNIQUE,
    user_id BIGINT,
    user_email VARCHAR(255),
    user_first_name VARCHAR(255),
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    channel_type VARCHAR(20) NOT NULL DEFAULT 'BOTH',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    attempt_count INT NOT NULL DEFAULT 0,
    last_error TEXT,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_outbox_status_priority ON notification_job_outbox (status, priority);
CREATE INDEX IF NOT EXISTS idx_outbox_idempotency ON notification_job_outbox (idempotency_key);

CREATE TABLE IF NOT EXISTS broadcast_jobs (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    last_processed_user_id BIGINT NOT NULL DEFAULT 0,
    total_recipients BIGINT NOT NULL DEFAULT 0,
    processed_recipients BIGINT NOT NULL DEFAULT 0,
    created_by_admin_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_broadcast_jobs_status ON broadcast_jobs (status);
