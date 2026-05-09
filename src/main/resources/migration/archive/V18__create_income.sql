-- V18__create_incomes.sql

CREATE TABLE incomes (
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    range VARCHAR(50) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT uq_incomes_admin_range 
        UNIQUE (admin_id, range),

    CONSTRAINT fk_incomes_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL
);

-- ================= INDEXES =================

CREATE INDEX idx_income_range 
ON incomes(range);

CREATE INDEX idx_incomes_admin_id 
ON incomes(admin_id);