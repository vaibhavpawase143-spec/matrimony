-- Insert default family types

INSERT INTO family (
    name,
    admin_id,
    is_active,
    created_at,
    updated_at
) VALUES
('Joint Family', NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Nuclear Family', NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Extended Family', NULL, TRUE, CURRENT_TIMESTAMP, NULL);