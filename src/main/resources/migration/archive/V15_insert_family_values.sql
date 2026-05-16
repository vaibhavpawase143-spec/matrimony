-- Insert default family values

INSERT INTO family_values (
    name,
    admin_id,
    is_active,
    created_at,
    updated_at
) VALUES
('Traditional', NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Moderate', NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Liberal', NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);