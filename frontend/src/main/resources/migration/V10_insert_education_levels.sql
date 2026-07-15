-- Insert default education levels

INSERT INTO education_levels (
    admin_id,
    name,
    is_active,
    created_at,
    updated_at
) VALUES
(NULL, 'High School', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Diploma', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Bachelor\'s Degree', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Master\'s Degree', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Doctorate (PhD)', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Other', TRUE, CURRENT_TIMESTAMP, NULL);