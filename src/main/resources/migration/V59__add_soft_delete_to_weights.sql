ALTER TABLE weights
    ADD COLUMN deleted_at TIMESTAMP;

ALTER TABLE weights
    ADD COLUMN deleted_by BIGINT;

CREATE INDEX idx_weight_deleted
    ON weights(deleted_at);

CREATE INDEX idx_weight_active
    ON weights(is_active);

CREATE INDEX idx_weight_admin
    ON weights(admin_id);