-- Insert default sister types

INSERT INTO sister_types (
    admin_id,
    value,
    is_active,
    created_at,
    updated_at
) VALUES
(NULL, 'Elder Sister', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Younger Sister', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'No Sister', TRUE, CURRENT_TIMESTAMP, NULL);