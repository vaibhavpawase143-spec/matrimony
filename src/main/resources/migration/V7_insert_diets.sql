-- Insert default diet types

INSERT INTO diets (
    admin_id,
    name,
    is_active,
    created_at,
    updated_at
) VALUES
(NULL, 'Vegetarian', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Non-Vegetarian', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Eggetarian', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Vegan', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Jain', TRUE, CURRENT_TIMESTAMP, NULL);