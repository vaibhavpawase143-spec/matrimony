-- V36__create_sub_castes.sql

CREATE TABLE sub_castes (
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    name VARCHAR(120) NOT NULL,

    caste_id BIGINT NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- ✅ Unique constraint
    CONSTRAINT uq_sub_castes_caste_name UNIQUE (caste_id, name),

    -- ✅ Foreign keys
    CONSTRAINT fk_sub_castes_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_sub_castes_caste
        FOREIGN KEY (caste_id)
        REFERENCES castes(id)
        ON DELETE CASCADE
);

-- ✅ Indexes
CREATE INDEX idx_subcaste_name ON sub_castes(name);
CREATE INDEX idx_subcaste_caste ON sub_castes(caste_id);

-- 🔥 Optional composite index for filtering
CREATE INDEX idx_subcaste_caste_active 
ON sub_castes(caste_id, is_active);