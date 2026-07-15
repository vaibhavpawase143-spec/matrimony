-- Insert demo partner preferences for users

INSERT INTO partner_preferences (
    user_id,
    min_age,
    max_age,
    min_height,
    max_height,
    religion_id,
    caste_id,
    city_id,
    is_active,
    created_at,
    updated_at
) VALUES
(
    (SELECT id FROM users WHERE email='john.doe@example.com'),
    25,
    30,
    5.5,
    6.2,
    (SELECT id FROM religions WHERE name='Hindu'),
    (SELECT id FROM castes WHERE name='Kshatriya' AND religion_id = (SELECT id FROM religions WHERE name='Hindu')),
    (SELECT id FROM cities WHERE name='Pune'),
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
),
(
    (SELECT id FROM users WHERE email='jane.smith@example.com'),
    28,
    35,
    5.6,
    6.0,
    (SELECT id FROM religions WHERE name='Hindu'),
    (SELECT id FROM castes WHERE name='Brahmin' AND religion_id = (SELECT id FROM religions WHERE name='Hindu')),
    (SELECT id FROM cities WHERE name='Mumbai'),
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
);