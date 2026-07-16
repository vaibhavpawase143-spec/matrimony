-- Insert default height values (in feet & inches)

INSERT INTO heights (
    admin_id,
    height,
    is_active,
    created_at,
    updated_at
) VALUES
(NULL, '4ft 6in', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, '4ft 8in', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, '5ft 0in', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, '5ft 2in', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, '5ft 4in', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, '5ft 6in', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, '5ft 8in', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, '5ft 10in', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, '6ft 0in', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, '6ft 2in', TRUE, CURRENT_TIMESTAMP, NULL);