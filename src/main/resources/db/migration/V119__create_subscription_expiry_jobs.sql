-- Flyway Migration V119: Create subscription expiry jobs table for idempotent queue worker architecture

CREATE TABLE subscription_expiry_jobs (
    id BIGSERIAL PRIMARY KEY,
    subscription_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    job_type VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    attempt_count INT NOT NULL DEFAULT 0,
    idempotency_key VARCHAR(100) NOT NULL UNIQUE,
    scheduled_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    started_at TIMESTAMP WITHOUT TIME ZONE,
    completed_at TIMESTAMP WITHOUT TIME ZONE,
    last_error TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sub_expiry_jobs_subscription FOREIGN KEY (subscription_id) REFERENCES user_subscriptions (id) ON DELETE CASCADE,
    CONSTRAINT fk_sub_expiry_jobs_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_sub_expiry_jobs_status ON subscription_expiry_jobs(status);
CREATE INDEX idx_sub_expiry_jobs_sub_user ON subscription_expiry_jobs(subscription_id, user_id);
