-- =====================================================
-- V41__create_user_subscriptions.sql
-- User Subscriptions Table
-- =====================================================

CREATE TABLE user_subscriptions (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- USER
    -- =====================================================
    user_id BIGINT NOT NULL,

    -- =====================================================
    -- SUBSCRIPTION PLAN
    -- =====================================================
    plan_id BIGINT NOT NULL,

    -- =====================================================
    -- SUBSCRIPTION PERIOD
    -- =====================================================
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    -- =====================================================
    -- STATUS
    -- =====================================================
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    -- =====================================================
    -- REFUND
    -- =====================================================
    refund_amount NUMERIC(10,2),

    refund_date TIMESTAMP WITHOUT TIME ZONE,

    refund_reason TEXT,

    -- =====================================================
    -- CANCELLATION
    -- =====================================================
    cancellation_reason TEXT,

    cancelled_at TIMESTAMP WITHOUT TIME ZONE,

    -- =====================================================
    -- AUDIT (Inherited from Auditable)
    -- =====================================================
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    created_by BIGINT,

    updated_at TIMESTAMP WITHOUT TIME ZONE,

    updated_by BIGINT,

    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    deleted_at TIMESTAMP WITHOUT TIME ZONE,

    deleted_by BIGINT,

    deletion_reason VARCHAR(500),

    version BIGINT NOT NULL DEFAULT 0,

    -- =====================================================
    -- FOREIGN KEYS
    -- =====================================================

    CONSTRAINT fk_user_subscriptions_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_subscriptions_plan
        FOREIGN KEY (plan_id)
        REFERENCES subscription_plans(id),

    -- =====================================================
    -- CHECK CONSTRAINTS
    -- =====================================================

    CONSTRAINT chk_user_subscription_status
        CHECK (
            status IN (
                'ACTIVE',
                'EXPIRED',
                'CANCELLED',
                'REFUNDED'
            )
        ),

    CONSTRAINT chk_user_subscription_dates
        CHECK (end_date >= start_date),

    CONSTRAINT chk_refund_amount
        CHECK (
            refund_amount IS NULL
            OR refund_amount >= 0
        )
);

-- =====================================================
-- ENTITY INDEX
-- =====================================================

CREATE INDEX idx_user_subscription_user
    ON user_subscriptions(user_id);

-- =====================================================
-- PRODUCTION INDEXES
-- =====================================================

CREATE INDEX idx_user_subscription_plan
    ON user_subscriptions(plan_id);

CREATE INDEX idx_user_subscription_status
    ON user_subscriptions(status);

CREATE INDEX idx_user_subscription_active
    ON user_subscriptions(is_active);

CREATE INDEX idx_user_subscription_start_date
    ON user_subscriptions(start_date);

CREATE INDEX idx_user_subscription_end_date
    ON user_subscriptions(end_date);

CREATE INDEX idx_user_subscription_deleted
    ON user_subscriptions(is_deleted);

CREATE INDEX idx_user_subscription_created_at
    ON user_subscriptions(created_at);

CREATE INDEX idx_user_subscription_user_status
    ON user_subscriptions(user_id, status);

CREATE INDEX idx_user_subscription_user_active
    ON user_subscriptions(user_id, is_active);

CREATE INDEX idx_user_subscription_user_plan
    ON user_subscriptions(user_id, plan_id);