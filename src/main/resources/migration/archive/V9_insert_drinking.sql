-- Insert default drinking habits

INSERT INTO drinking (
    name,
    value,
    admin_id,
    is_active,
    created_at,
    updated_at
) VALUES
('Drinking Habit', 'Non-Drinker', NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Drinking Habit', 'Occasionally', NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Drinking Habit', 'Regularly', NULL, TRUE, CURRENT_TIMESTAMP, NULL);