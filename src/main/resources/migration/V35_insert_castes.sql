-- Insert default castes for religions (example)

INSERT INTO castes (
    admin_id,
    religion_id,
    name,
    is_active,
    created_at,
    updated_at
) VALUES
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Brahmin', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Kshatriya', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Vaishya', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Hindu'), 'Shudra', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Muslim'), 'Sunni', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Muslim'), 'Shia', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Christian'), 'Catholic', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Christian'), 'Protestant', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM religions WHERE name='Other'), 'Other', TRUE, CURRENT_TIMESTAMP, NULL);