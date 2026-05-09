-- Insert default subscription plans

INSERT INTO subscription_plans (
    admin_id,
    name,
    price,
    duration,
    description,
    is_active,
    created_at,
    updated_at
) VALUES
(NULL, 'Basic', 499.00, 30, 'Access to basic features for 1 month', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Standard', 1299.00, 90, 'Access to standard features for 3 months', TRUE, CURRENT_TIMESTAMP, NULL),
(NULL, 'Premium', 2499.00, 180, 'Access to premium features for 6 months', TRUE, CURRENT_TIMESTAMP, NULL);
