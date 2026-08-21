-- Migration V127: Create broadcast_recipient_status table for per-recipient broadcast delivery tracking
CREATE TABLE IF NOT EXISTS broadcast_recipient_status (
    id BIGSERIAL PRIMARY KEY,
    broadcast_job_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    user_email VARCHAR(255),
    app_notification_status VARCHAR(50) NOT NULL DEFAULT 'QUEUED',
    email_status VARCHAR(50) NOT NULL DEFAULT 'QUEUED',
    email_attempt_count INT NOT NULL DEFAULT 0,
    email_error TEXT,
    notification_processed_at TIMESTAMP,
    email_queued_at TIMESTAMP,
    email_accepted_at TIMESTAMP,
    email_delivered_at TIMESTAMP,
    last_error TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_brs_job_id ON broadcast_recipient_status(broadcast_job_id);
CREATE INDEX IF NOT EXISTS idx_brs_job_user ON broadcast_recipient_status(broadcast_job_id, user_id);
CREATE INDEX IF NOT EXISTS idx_brs_job_app_status ON broadcast_recipient_status(broadcast_job_id, app_notification_status);
CREATE INDEX IF NOT EXISTS idx_brs_job_email_status ON broadcast_recipient_status(broadcast_job_id, email_status);
