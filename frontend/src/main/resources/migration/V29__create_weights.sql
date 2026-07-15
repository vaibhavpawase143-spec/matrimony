-- V29__create_weights.sql

CREATE TABLE weights (
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    value VARCHAR(20) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- ✅ Unique constraint
    CONSTRAINT uq_weights_admin_value UNIQUE (admin_id, value),

    -- ✅ Foreign key
    CONSTRAINT fk_weights_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL
);

-- ✅ Indexes
CREATE INDEX idx_weight_value ON weights(value);
CREATE INDEX idx_weights_admin_id ON weights(admin_id);

-- 🔥 Optional composite index (recommended)
CREATE INDEX idx_weights_active 
ON weights(is_active);