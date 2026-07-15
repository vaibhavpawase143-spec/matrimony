-- Insert default family status values

INSERT INTO family_status (
    name,
    admin_id,
    is_active,
    created_at,
    updated_at
) VALUES
('Middle Class', NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Upper Middle Class', NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Rich / Affluent', NULL, TRUE, CURRENT_TIMESTAMP, NULL);