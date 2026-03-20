CREATE TABLE IF NOT EXISTS diets (
    id BIGSERIAL PRIMARY KEY,
    admin_id BIGINT,
    name VARCHAR(100),
    isActive BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_diet_admin FOREIGN KEY (admin_id) REFERENCES admins(id)
);