-- V41__create_user_subscriptions.sql

CREATE TABLE user_subscriptions (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,
    plan_id BIGINT NOT NULL,

    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- ✅ Foreign keys
    CONSTRAINT fk_user_subscriptions_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_subscriptions_plan
        FOREIGN KEY (plan_id)
        REFERENCES subscription_plans(id)
        ON DELETE RESTRICT,

    -- ✅ Validations
    CONSTRAINT chk_subscription_dates
        CHECK (start_date <= end_date),

    CONSTRAINT chk_subscription_status
        CHECK (status IN ('ACTIVE', 'EXPIRED', 'CANCELLED'))
);

-- ✅ Indexes
CREATE INDEX idx_user_subscription_user ON user_subscriptions(user_id);

-- 🔥 IMPORTANT: Fast active subscription lookup
CREATE INDEX idx_user_subscription_active 
ON user_subscriptions(user_id, is_active);

-- 🔥 Optional: status-based queries
CREATE INDEX idx_user_subscription_status 
ON user_subscriptions(status);