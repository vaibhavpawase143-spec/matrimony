-- Insert default disability statuses

INSERT INTO disability_statuses (
    admin_id,
    value,
    is_active,
    created_at,
    updated_at
) VALUES
(NULL, 'None', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Physically Challenged', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Visually Impaired', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Hearing Impaired', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Other', TRUE, CURRENT_TIMESTAMP, NULL);