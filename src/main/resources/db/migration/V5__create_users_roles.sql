CREATE TABLE users_roles
(
    users_id BIGINT NOT NULL,

    roles_id BIGINT NOT NULL,

    PRIMARY KEY (users_id, roles_id),

    CONSTRAINT fk_users_roles_user
        FOREIGN KEY (users_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_users_roles_role
        FOREIGN KEY (roles_id)
        REFERENCES roles(id)
        ON DELETE CASCADE
);

-- ==========================================
-- INDEXES
-- ==========================================

CREATE INDEX idx_users_roles_user
ON users_roles(users_id);

CREATE INDEX idx_users_roles_role
ON users_roles(roles_id);