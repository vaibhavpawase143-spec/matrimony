-- Insert default states for India (example)

INSERT INTO states (
    admin_id,
    country_id,
    name,
    is_active,
    created_at,
    updated_at
) VALUES
(NULL, (SELECT id FROM countries WHERE name='India'), 'Maharashtra', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM countries WHERE name='India'), 'Gujarat', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM countries WHERE name='India'), 'Tamil Nadu', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM countries WHERE name='India'), 'Karnataka', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM countries WHERE name='India'), 'Punjab', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM countries WHERE name='India'), 'West Bengal', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM countries WHERE name='India'), 'Uttar Pradesh', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM countries WHERE name='India'), 'Rajasthan', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM countries WHERE name='India'), 'Kerala', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM countries WHERE name='India'), 'Other', TRUE, CURRENT_TIMESTAMP, NULL);