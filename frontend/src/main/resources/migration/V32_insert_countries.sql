-- Insert default countries (commonly used in matrimony apps)

INSERT INTO countries (
    admin_id,
    name,
    is_active,
    created_at,
    updated_at
) VALUES
(NULL, 'India', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'United States', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'United Kingdom', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Canada', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Australia', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'New Zealand', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'United Arab Emirates', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Singapore', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Saudi Arabia', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Other', TRUE, CURRENT_TIMESTAMP, NULL);