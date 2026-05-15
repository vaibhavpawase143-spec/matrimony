-- Insert default profile types

INSERT INTO profile_types (
    admin_id,
    name,
    is_active,
    created_at,
    updated_at
) VALUES
(NULL, 'Standard', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Premium', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'VIP', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Other', TRUE, CURRENT_TIMESTAMP, NULL);