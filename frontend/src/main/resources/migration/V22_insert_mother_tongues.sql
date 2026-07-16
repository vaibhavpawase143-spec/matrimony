-- Insert default mother tongues (India-focused)

INSERT INTO mother_tongues (
    admin_id,
    name,
    is_active,
    created_at,
    updated_at
) VALUES
(NULL, 'Hindi', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Marathi', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Gujarati', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Punjabi', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Tamil', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Telugu', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Kannada', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Malayalam', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Bengali', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Urdu', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Odia', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Assamese', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Other', TRUE, CURRENT_TIMESTAMP, NULL);