-- Insert default complexion values

INSERT INTO complexions (
    admin_id,
    value,
    is_active,
    created_at,
    updated_at
) VALUES
(NULL, 'Very Fair', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Fair', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Wheatish', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Wheatish Brown', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Dark', TRUE, CURRENT_TIMESTAMP, NULL);