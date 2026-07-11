-- V17__create_heights.sql

CREATE TABLE heights (
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    height VARCHAR(20) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT uq_heights_height_admin 
        UNIQUE (height, admin_id),

    CONSTRAINT fk_heights_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL
);

-- ================= INDEXES =================

CREATE INDEX idx_height_value 
ON heights(height);

CREATE INDEX idx_heights_admin_id 
ON heights(admin_id);