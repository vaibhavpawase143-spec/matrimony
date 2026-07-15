-- Insert default occupations

INSERT INTO occupations (
    admin_id,
    name,
    is_active,
    created_at,
    updated_at
) VALUES
(NULL, 'Software Engineer', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Doctor', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Teacher', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Lawyer', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Business Owner', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Accountant', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Entrepreneur', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Artist', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Government Service', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Other', TRUE, CURRENT_TIMESTAMP, NULL);