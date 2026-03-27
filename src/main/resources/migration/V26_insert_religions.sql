-- Insert default religions

INSERT INTO religions (
    admin_id,
    name,
    is_active,
    created_at,
    updated_at
) VALUES
(NULL, 'Hindu', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Muslim', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Christian', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Sikh', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Buddhist', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Jain', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Parsi/Zoroastrian', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Jewish', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Other', TRUE, CURRENT_TIMESTAMP, NULL);