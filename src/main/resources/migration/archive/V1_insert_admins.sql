-- Insert default admin records into admins table

INSERT INTO admins (
    name,
    username,
    email,
    password,
    role_id,
    phone,
    is_active,
    last_login,
    created_at,
    updated_at
) VALUES 
(
    'Super Admin',
    'superadmin',
    'superadmin@example.com',
    '$2a$10$hashedpassword1234567890', -- replace with real bcrypt hash
    1,
    '9999999999',
    TRUE,
    NULL,
    CURRENT_TIMESTAMP,
    NULL
),
(
    'Admin User',
    'admin',
    'admin@example.com',
    '$2a$10$hashedpassword1234567890',
    1,
    '8888888888',
    TRUE,
    NULL,
    CURRENT_TIMESTAMP,
    NULL
);