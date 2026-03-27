-- V34__create_cities.sql

CREATE TABLE cities (
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(100) NOT NULL,

    state_id BIGINT NOT NULL,

    admin_id BIGINT,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT uq_cities_name_state 
        UNIQUE (name, state_id),

    CONSTRAINT fk_cities_state
        FOREIGN KEY (state_id)
        REFERENCES states(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_cities_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
        ON DELETE SET NULL
);

-- ================= INDEXES =================

CREATE INDEX idx_city_state 
ON cities(state_id);

CREATE INDEX idx_city_active 
ON cities(is_active);

CREATE INDEX idx_cities_admin_id 
ON cities(admin_id);