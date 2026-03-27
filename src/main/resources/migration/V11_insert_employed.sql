-- Insert default employment statuses

INSERT INTO employed (
    admin_id,
    name,
    is_active,
    created_at,
    updated_at
) VALUES
(NULL, 'Employed', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Self-Employed', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Business Owner', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Unemployed', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Student', TRUE, CURRENT_TIMESTAMP, NULL);