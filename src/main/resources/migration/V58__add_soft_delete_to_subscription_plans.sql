ALTER TABLE subscription_plans
    ADD COLUMN deleted_at TIMESTAMP;

ALTER TABLE subscription_plans
    ADD COLUMN deleted_by BIGINT;

CREATE INDEX idx_subscription_plan_deleted
    ON subscription_plans(deleted_at);

CREATE INDEX idx_subscription_plan_active
    ON subscription_plans(is_active);

CREATE INDEX idx_subscription_plan_admin
    ON subscription_plans(admin_id);