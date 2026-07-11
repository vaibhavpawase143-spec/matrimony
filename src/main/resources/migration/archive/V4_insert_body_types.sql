-- Insert default body types

INSERT INTO body_types (
    admin_id,
    value,
    is_active,
    created_at,
    updated_at
) VALUES
(NULL, 'Slim', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Average', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Athletic', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Heavy', TRUE, CURRENT_TIMESTAMP, NULL);