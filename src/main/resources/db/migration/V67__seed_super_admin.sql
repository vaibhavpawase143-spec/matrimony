-- =====================================================
-- GATHBANDHAN MATRIMONY
-- DEFAULT SUPER ADMIN
-- =====================================================

INSERT INTO admins (
    name,
    username,
    email,
    password,
    profile_photo,
    role_id,
    phone,
    is_active,
    last_login,
    created_at,
    updated_at,
    deleted_by,
    deleted_at
)
SELECT
    'Vaibhav Pawase',
    'superadmin',
    'vaibhavpawase143@gmail.com',
    '$2a$10$l.b5mCe689JQBwxFWih4ZOfL/AKWaPf5P3WBN072NH9DsVaUiKpMm',
    NULL,
    r.id,
    '7666084107',
    TRUE,
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL,
    NULL
FROM roles r
WHERE r.name = 'ROLE_SUPER_ADMIN'
AND NOT EXISTS (
    SELECT 1
    FROM admins a
    WHERE LOWER(a.username) = LOWER('superadmin')
       OR LOWER(a.email) = LOWER('vaibhavpawase143@gmail.com')
);