CREATE TABLE roles (
                       id BIGSERIAL PRIMARY KEY,
                       admin_id BIGINT,
                       name VARCHAR(100),
                       CONSTRAINT fk_role_admin
                           FOREIGN KEY (admin_id)
                               REFERENCES admins(id),
                       CONSTRAINT uq_role_admin_name
                           UNIQUE (admin_id, name)
);