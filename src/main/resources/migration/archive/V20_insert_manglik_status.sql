-- Insert default manglik statuses

INSERT INTO manglik_status (
    admin_id,
    name,
    is_active,
    created_at,
    updated_at
) VALUES
(NULL, 'Manglik', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Non-Manglik', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Partial Manglik', TRUE, CURRENT_TIMESTAMP, NULL);