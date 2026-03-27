-- V35__create_castes.sql

CREATE TABLE castes (
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    name VARCHAR(100) NOT NULL,

    religion_id BIGINT,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT uq_castes_name_religion 
        UNIQUE (name, religion_id),

    CONSTRAINT fk_castes_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_castes_religion
        FOREIGN KEY (religion_id)
        REFERENCES religions(id)
        ON DELETE SET NULL
);

-- ================= INDEXES =================

CREATE INDEX idx_caste_religion 
ON castes(religion_id);

CREATE INDEX idx_caste_active 
ON castes(is_active);

CREATE INDEX idx_castes_admin_id 
ON castes(admin_id);