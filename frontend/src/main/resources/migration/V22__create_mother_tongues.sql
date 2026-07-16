-- V22__create_mother_tongues.sql

CREATE TABLE mother_tongues (
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    name VARCHAR(100) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT uq_mother_tongues_admin_name 
        UNIQUE (admin_id, name),

    CONSTRAINT fk_mother_tongues_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL
);

-- ================= INDEXES =================

CREATE INDEX idx_mother_tongue_name 
ON mother_tongues(name);

CREATE INDEX idx_mother_tongues_admin_id 
ON mother_tongues(admin_id);