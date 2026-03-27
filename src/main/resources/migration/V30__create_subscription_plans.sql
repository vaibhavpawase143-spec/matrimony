-- V30__create_subscription_plans.sql

CREATE TABLE subscription_plans (
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(100) NOT NULL,

    price NUMERIC(10,2) NOT NULL,

    duration INTEGER NOT NULL,

    description VARCHAR(500),

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    admin_id BIGINT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    -- ================= CONSTRAINTS =================

    CONSTRAINT uq_subscription_plans_admin_name 
        UNIQUE (admin_id, name),

    CONSTRAINT fk_subscription_plans_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL,

    -- 🔥 VALIDATIONS (VERY IMPORTANT)
    CONSTRAINT chk_plan_price_positive 
        CHECK (price > 0),

    CONSTRAINT chk_plan_duration_positive 
        CHECK (duration > 0),

    CONSTRAINT chk_plan_name_not_empty
        CHECK (name <> '')
);

-- ================= INDEXES =================

CREATE INDEX idx_plan_name 
ON subscription_plans(name);

CREATE INDEX idx_subscription_plans_admin_id 
ON subscription_plans(admin_id);

CREATE INDEX idx_subscription_plans_active 
ON subscription_plans(is_active);

-- 🔥 Composite index (important for filtering)
CREATE INDEX idx_subscription_plans_admin_active
ON subscription_plans(admin_id, is_active);