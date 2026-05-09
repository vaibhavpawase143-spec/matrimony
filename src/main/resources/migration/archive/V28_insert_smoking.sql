-- Insert default smoking options

INSERT INTO smoking (
    admin_id,
    value,
    is_active,
    created_at,
    updated_at
) VALUES
(NULL, 'Non-Smoker', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Occasional Smoker', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Regular Smoker', TRUE, CURRENT_TIMESTAMP, NULL);