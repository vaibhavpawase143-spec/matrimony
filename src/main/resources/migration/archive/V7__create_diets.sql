-- V7__create_diets.sql

CREATE TABLE diets (
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    name VARCHAR(100) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT uq_diets_name_admin 
        UNIQUE (name, admin_id),

    CONSTRAINT fk_diets_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL
);

-- ================= INDEXES =================

CREATE INDEX idx_diet_admin 
ON diets(admin_id);

CREATE INDEX idx_diet_active 
ON diets(is_active);