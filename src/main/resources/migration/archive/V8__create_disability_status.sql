-- V8__create_disability_statuses.sql

CREATE TABLE disability_statuses (
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    value VARCHAR(100) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT uq_disability_value_admin 
        UNIQUE (value, admin_id),

    CONSTRAINT fk_disability_statuses_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL
);

-- ================= INDEXES =================

CREATE INDEX idx_disability_admin 
ON disability_statuses(admin_id);

CREATE INDEX idx_disability_active 
ON disability_statuses(is_active);