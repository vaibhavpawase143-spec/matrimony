-- V25__create_qualifications.sql

CREATE TABLE qualifications (
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    name VARCHAR(120) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    -- ================= CONSTRAINTS =================

    CONSTRAINT uq_qualifications_admin_name 
        UNIQUE (admin_id, name),

    CONSTRAINT fk_qualifications_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL,

    -- 🔥 Prevent empty values
    CONSTRAINT chk_qualifications_name_not_empty
        CHECK (name <> '')
);

-- ================= INDEXES =================

CREATE INDEX idx_qualification_name 
ON qualifications(name);

CREATE INDEX idx_qualifications_admin_id 
ON qualifications(admin_id);

-- 🔥 Composite index (important)
CREATE INDEX idx_qualifications_admin_name 
ON qualifications(admin_id, name);