-- Insert default fields of study

INSERT INTO fields_of_study (
    admin_id,
    name,
    is_active,
    created_at,
    updated_at
) VALUES
(NULL, 'Engineering', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Medical', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Commerce', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Arts', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Science', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Management', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Law', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Education', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Other', TRUE, CURRENT_TIMESTAMP, NULL);