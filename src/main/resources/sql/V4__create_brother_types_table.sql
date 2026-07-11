CREATE TABLE brother_types (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    admin_id BIGINT,

    value VARCHAR(100) NOT NULL,

    isActive BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NULL,

    CONSTRAINT fk_brothertype_admin
        FOREIGN KEY (admin_id)
        REFERENCES admins(id),

    CONSTRAINT uk_brother_type_admin_value UNIQUE (admin_id, value)

);