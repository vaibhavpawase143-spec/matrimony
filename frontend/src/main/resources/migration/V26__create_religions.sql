-- V26__create_religions.sql

CREATE TABLE religions (
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    name VARCHAR(120) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- ✅ Unique constraint
    CONSTRAINT uq_religions_admin_name UNIQUE (admin_id, name),

    -- ✅ Foreign key
    CONSTRAINT fk_religions_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL
);

-- ✅ Indexes
CREATE INDEX idx_religion_name ON religions(name);
CREATE INDEX idx_religions_admin_id ON religions(admin_id);