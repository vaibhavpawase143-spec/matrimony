-- Insert default photo types

INSERT INTO photo_types (
    name,
    created_at,
    updated_at
) VALUES
('Profile Photo', CURRENT_TIMESTAMP, NULL),
('Album Photo', CURRENT_TIMESTAMP, NULL),
('Cover Photo', CURRENT_TIMESTAMP, NULL),
('Other', CURRENT_TIMESTAMP, NULL);