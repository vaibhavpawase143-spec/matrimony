-- Insert default family types

INSERT INTO family_types (
    admin_id,
    name,
    is_active,
    created_at,
    updated_at
) VALUES
(NULL, 'Joint', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Nuclear', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Extended', TRUE, CURRENT_TIMESTAMP, NULL);