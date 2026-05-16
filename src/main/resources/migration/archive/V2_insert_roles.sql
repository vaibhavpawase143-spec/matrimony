-- Insert default roles into roles table

INSERT INTO roles (
    admin_id,
    name,
    is_active,
    created_at,
    updated_at
) VALUES
(
    NULL,
    'SUPER_ADMIN',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
),
(
    NULL,
    'ADMIN',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
),
(
    NULL,
    'MANAGER',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
),
(
    NULL,
    'USER',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);