CREATE TABLE blood_groups (
    id BIGSERIAL PRIMARY KEY,

    admin_id BIGINT,

    type VARCHAR(10) NOT NULL UNIQUE,

    status BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT fk_bloodgroup_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id)
);

-- Index
CREATE INDEX idx_blood_group ON blood_groups(type);