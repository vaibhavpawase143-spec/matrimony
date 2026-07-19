-- =====================================================
-- V41__create_subscription_plans.sql
-- Subscription Plans Table
-- =====================================================

CREATE TABLE subscription_plans (

    -- =====================================================
    -- PRIMARY KEY
    -- =====================================================
    id BIGSERIAL PRIMARY KEY,

    -- =====================================================
    -- ADMIN
    -- =====================================================
    admin_id BIGINT NOT NULL,

    -- =====================================================
    -- PLAN DETAILS
    -- =====================================================
    name VARCHAR(100) NOT NULL,

    price NUMERIC(10,2) NOT NULL,

    duration INTEGER NOT NULL,

    description VARCHAR(500),

    -- =====================================================
    -- STATUS
    -- =====================================================
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    -- =====================================================
    -- AUDIT
    -- =====================================================
    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    deleted_at TIMESTAMP,

    deleted_by BIGINT,

    -- =====================================================
    -- CONSTRAINTS
    -- =====================================================

    CONSTRAINT uk_subscription_plan_admin_name
        UNIQUE (admin_id, name),

    CONSTRAINT fk_subscription_plan_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id),

    CONSTRAINT chk_subscription_plan_price
        CHECK (price >= 0),

    CONSTRAINT chk_subscription_plan_duration
        CHECK (duration >= 0)

);

-- =====================================================
-- INDEXES
-- =====================================================

CREATE INDEX idx_subscription_plan_name
    ON subscription_plans(name);

CREATE INDEX idx_subscription_plan_admin
    ON subscription_plans(admin_id);

CREATE INDEX idx_subscription_plan_active
    ON subscription_plans(is_active);

CREATE INDEX idx_subscription_plan_deleted
    ON subscription_plans(deleted_at);

CREATE INDEX idx_subscription_plan_price
    ON subscription_plans(price);

CREATE INDEX idx_subscription_plan_duration
    ON subscription_plans(duration);

CREATE INDEX idx_subscription_plan_created_at
    ON subscription_plans(created_at);

CREATE INDEX idx_subscription_plan_admin_active
    ON subscription_plans(admin_id, is_active);

CREATE INDEX idx_subscription_plan_admin_name
    ON subscription_plans(admin_id, name);