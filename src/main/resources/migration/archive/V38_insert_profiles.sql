-- Insert demo profiles for users

INSERT INTO profiles (
    user_id,
    religion_id,
    caste_id,
    education_level_id,
    occupation_id,
    height_id,
    weight_id,
    city_id,
    about,
    is_active,
    created_at,
    updated_at
) VALUES
(
    (SELECT id FROM users WHERE email='john.doe@example.com'),
    (SELECT id FROM religions WHERE name='Hindu'),
    (SELECT id FROM castes WHERE name='Brahmin' AND religion_id = (SELECT id FROM religions WHERE name='Hindu')),
    (SELECT id FROM education_levels WHERE name='Bachelor'),
    (SELECT id FROM occupations WHERE name='Software Engineer'),
    (SELECT id FROM heights WHERE height='5.9'),
    (SELECT id FROM weights WHERE value='75kg'),
    (SELECT id FROM cities WHERE name='Mumbai'),
    'I am a software engineer who loves coding and traveling.',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
),
(
    (SELECT id FROM users WHERE email='jane.smith@example.com'),
    (SELECT id FROM religions WHERE name='Hindu'),
    (SELECT id FROM castes WHERE name='Kshatriya' AND religion_id = (SELECT id FROM religions WHERE name='Hindu')),
    (SELECT id FROM education_levels WHERE name='Master'),
    (SELECT id FROM occupations WHERE name='Doctor'),
    (SELECT id FROM heights WHERE height='5.6'),
    (SELECT id FROM weights WHERE value='60kg'),
    (SELECT id FROM cities WHERE name='Pune'),
    'I enjoy reading books, yoga, and exploring new cuisines.',
    TRUE,
    CURRENT_TIMESTAMP,
    NULL
);