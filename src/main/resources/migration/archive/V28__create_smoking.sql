-- V28__create_smoking.sql

CREATE TABLE smoking (
    id BIGSERIAL PRIMARY KEY,

    value VARCHAR(50) NOT NULL,

    admin_id BIGINT,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    -- ================= CONSTRAINTS =================

    CONSTRAINT uq_smoking_admin_value 
        UNIQUE (admin_id, value),

    CONSTRAINT fk_smoking_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL,

    -- 🔥 Prevent empty values
    CONSTRAINT chk_smoking_value_not_empty
        CHECK (value <> '')
);

-- ================= INDEXES =================

CREATE INDEX idx_smoking_value 
ON smoking(value);

CREATE INDEX idx_smoking_admin_id 
ON smoking(admin_id);

-- 🔥 Composite index (important)
CREATE INDEX idx_smoking_admin_value 
ON smoking(admin_id, value);