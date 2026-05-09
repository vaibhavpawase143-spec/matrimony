-- Insert default marital statuses

INSERT INTO marital_status (
    admin_id,
    name,
    is_active,
    created_at,
    updated_at
) VALUES
(NULL, 'Never Married', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Married', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Divorced', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Widowed', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Separated', TRUE, CURRENT_TIMESTAMP, NULL);