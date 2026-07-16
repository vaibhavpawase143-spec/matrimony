CREATE TABLE body_types (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    admin_id BIGINT,

    value VARCHAR(100),

    isActive BOOLEAN DEFAULT TRUE,

    CONSTRAINT fk_bodytype_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id),

    CONSTRAINT uk_body_types_admin_value UNIQUE (admin_id, value)

);