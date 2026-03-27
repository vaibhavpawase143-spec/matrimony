-- Insert demo family details for profiles

INSERT INTO family_details (
    profile_id,
    family_type_id,
    family_id,
    brother_type_id,
    sister_type_id,
    father_occupation,
    mother_occupation,
    is_active,
    created_at,
    updated_at
) VALUES
(
    (SELECT id FROM profiles WHERE user_id = (SELECT id FROM users WHERE email='john.doe@example.com')),
    (SELECT id FROM family_types WHERE name='Joint'),
    (SELECT id FROM family WHERE name='Doe Family'),
    (SELECT id FROM brother_types WHERE value='1 Brother'),
    (SELECT id FROM sister_types WHERE value='1 Sister'),
    'Businessman',
    'Homemaker',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
),
(
    (SELECT id FROM profiles WHERE user_id = (SELECT id FROM users WHERE email='jane.smith@example.com')),
    (SELECT id FROM family_types WHERE name='Nuclear'),
    (SELECT id FROM family WHERE name='Smith Family'),
    (SELECT id FROM brother_types WHERE value='0 Brother'),
    (SELECT id FROM sister_types WHERE value='1 Sister'),
    'Engineer',
    'Teacher',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
);