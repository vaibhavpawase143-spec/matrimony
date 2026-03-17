CREATE TABLE subscription_plans (
                                    id BIGSERIAL PRIMARY KEY,
                                    name VARCHAR(120),
                                    price DOUBLE PRECISION,
                                    duration_days INT,
                                    description TEXT,
                                    active BOOLEAN DEFAULT TRUE,
                                    admin_id BIGINT,
                                    CONSTRAINT fk_subscription_plan_admin
                                        FOREIGN KEY (admin_id)
                                            REFERENCES admins(id)
);