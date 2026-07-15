-- Insert demo shortlists for users

INSERT INTO shortlists (
    user_id,
    profile_id,
    is_active,
    created_at,
    updated_at
) VALUES
(
    (SELECT id FROM users WHERE email='john.doe@example.com'),
    (SELECT id FROM profiles WHERE user_id = (SELECT id FROM users WHERE email='jane.smith@example.com')),
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
),
(
    (SELECT id FROM users WHERE email='jane.smith@example.com'),
    (SELECT id FROM profiles WHERE user_id = (SELECT id FROM users WHERE email='john.doe@example.com')),
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
);