-- Insert default sub-castes for castes (example)

INSERT INTO sub_castes (
    admin_id,
    caste_id,
    name,
    is_active,
    created_at,
    updated_at
) VALUES
(NULL, (SELECT id FROM castes WHERE name='Brahmin' AND religion_id = (SELECT id FROM religions WHERE name='Hindu')), 'Saraswat', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM castes WHERE name='Brahmin' AND religion_id = (SELECT id FROM religions WHERE name='Hindu')), 'Gaur', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM castes WHERE name='Kshatriya' AND religion_id = (SELECT id FROM religions WHERE name='Hindu')), 'Rajput', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM castes WHERE name='Vaishya' AND religion_id = (SELECT id FROM religions WHERE name='Hindu')), 'Agarwal', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM castes WHERE name='Vaishya' AND religion_id = (SELECT id FROM religions WHERE name='Hindu')), 'Gupta', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM castes WHERE name='Shudra' AND religion_id = (SELECT id FROM religions WHERE name='Hindu')), 'Other', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM castes WHERE name='Sunni' AND religion_id = (SELECT id FROM religions WHERE name='Muslim')), 'Deobandi', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM castes WHERE name='Sunni' AND religion_id = (SELECT id FROM religions WHERE name='Muslim')), 'Barelvi', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, (SELECT id FROM castes WHERE name='Shia' AND religion_id = (SELECT id FROM religions WHERE name='Muslim')), 'Twelver', TRUE, CURRENT_TIMESTAMP, NULL);