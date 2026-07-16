-- Insert demo photos for users

INSERT INTO user_photos (
    user_id,
    photo_type,
    photo_url,
    created_at,
    updated_at
) VALUES
(
    (SELECT id FROM users WHERE email='john.doe@example.com'),
    'PROFILE',
    'https://example.com/photos/john_profile.jpg',
    CURRENT_TIMESTAMP,
    NULL
),
(
    (SELECT id FROM users WHERE email='jane.smith@example.com'),
    'PROFILE',
    'https://example.com/photos/jane_profile.jpg',
    CURRENT_TIMESTAMP,
    NULL
),
(
    (SELECT id FROM users WHERE email='john.doe@example.com'),
    'COVER',
    'https://example.com/photos/john_cover.jpg',
    CURRENT_TIMESTAMP,
    NULL
),
(
    (SELECT id FROM users WHERE email='jane.smith@example.com'),
    'COVER',
    'https://example.com/photos/jane_cover.jpg',
    CURRENT_TIMESTAMP,
    NULL
);