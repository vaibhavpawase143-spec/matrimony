-- V33__create_states.sql

CREATE TABLE states (
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    name VARCHAR(120) NOT NULL,

    country_id BIGINT NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- ✅ Unique constraint
    CONSTRAINT uq_states_country_name UNIQUE (country_id, name),

    -- ✅ Foreign keys
    CONSTRAINT fk_states_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_states_country
        FOREIGN KEY (country_id)
        REFERENCES countries(id)
        ON DELETE CASCADE
);

-- ✅ Indexes
CREATE INDEX idx_state_name ON states(name);
CREATE INDEX idx_state_country ON states(country_id);
CREATE INDEX idx_states_admin_id ON states(admin_id);

-- 🔥 Optional composite index for search
CREATE INDEX idx_states_country_active 
ON states(country_id, is_active);